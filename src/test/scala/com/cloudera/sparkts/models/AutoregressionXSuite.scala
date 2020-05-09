/**
 * Copyright (c) 2016, Cloudera, Inc. All Rights Reserved.
 *
 * Cloudera, Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"). You may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * This software is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the
 * License.
 */

package com.cloudera.sparkts.models

import breeze.linalg._
import breeze.plot._
import breeze.plot.Figure
import org.apache.commons.math3.random.MersenneTwister
import com.cloudera.sparkts.{EasyPlot, Lag, MatrixUtil}
import org.scalatest.FunSuite
import org.scalatest.Matchers._

class AutoregressionXSuite extends FunSuite {
  val rand = new MersenneTwister(10L)
  val nRows = 1000
  val nCols = 2
  val X = Array.fill(nRows, nCols)(rand.nextGaussian())
  val intercept = rand.nextGaussian * 10
  val picPath = "/home/liuking/idea/workspace/spark-timeseries/pics/"


  test("plot X"){
    val f = Figure()
    val p = f.subplot(0)
//    val matX = MatrixUtil.arrsToMat(X.iterator)
    val line = linspace(1,1000,1000)
    val xCoeffs = Array(0.8, 0.2)
    val rawY = X.map(_.zip(xCoeffs).map { case (b, v) => b * v }.sum + intercept)
    val arCoeff = 0.4
    val y = rawY.scanLeft(0.0) { case (priorY, currY) => currY + priorY * arCoeff }.tail
    val dy = new DenseVector(y)
    val dx = new DenseMatrix(rows = X.length, cols = X.head.length, data = X.transpose.flatten)

//    p += plot(dx(::,0).toDenseVector,dy,'.')
//    p += plot(dx(::,1).toDenseVector,dy,'+')

//    EasyPlot.ezplot(dx(::,0).toArray,'.').saveas(picPath+"xCol0.png")
    val arr0 = dx(::,0).toArray
    val arr1 = dx(::,1).toArray
    p += plot(arr0.indices.map(_.toDouble).toArray, arr0, style = '.')
    p += plot(arr1.indices.map(_.toDouble).toArray, arr1, style = '+')

    p.xlabel_=("x")
    p.ylabel_=("y")
    p.title_=("Data Show")
    f.saveas(picPath+"series.png")
  }

  // tests an autoregressive model where the exogenous variables are not lagged
  /**
    * 模拟ARX模型
    */
  test("fit ARX(1, 0, true)") {
    val xCoeffs = Array(0.8, 0.2)
    //zip:拉链处理  (a,b) zip (c,d) ---> (tuple2(a,c),tuple2(b,d))
    //(tuple2(a,c),tuple2(b,d)) ---> (a*c,b*d) ---> a*c+b*d ---> a*c+b*d + intercept
    val rawY = X.map(_.zip(xCoeffs).map { case (b, v) => b * v }.sum + intercept)
    val arCoeff = 0.4
    //scanLeft:从初始值开始应用op进行累计操作。
    // eg:Array(1, 2, 3, 4).scanLeft(0)(_ + _) == Array(0, 0+1, 0+1+2, 0+1+2+3, 0+1+2+3+4) == Array(0, 1, 3, 6, 10)
    val y = rawY.scanLeft(0.0) { case (priorY, currY) => currY + priorY * arCoeff }.tail
    val dy = new DenseVector(y)
    //X转置并进行flatten。 将X转换成Matrix，方便操作
    val dx = new DenseMatrix(rows = X.length, cols = X.head.length, data = X.transpose.flatten)
    //ARX模型
    val model = AutoregressionX.fitModel(dy, dx, 1, 0, includeOriginalX = true)
    val combinedCoeffs = Array(arCoeff) ++ xCoeffs

    model.c should be (intercept +- 1e-4)
    for (i <- combinedCoeffs.indices) {
      model.coefficients(i) should be (combinedCoeffs(i) +- 1e-4)
    }
  }

  // tests a model with no autoregressive term but with lagged exogenous variables
  test("fit ARX(0, 1, false) model") {
    val xCoeffs = Array(0.4, 0.15)
    val xLagged = Lag.lagMatTrimBoth(X, 1)
    val y = xLagged.map(_.zip(xCoeffs).map { case (b, v) => b * v }.sum + intercept)
    val dy = new DenseVector(Array(0.0) ++ y)
    // note that we provide the original X matrix to the fitting functiond
    val dx = new DenseMatrix(rows = X.length, cols = X.head.length, data = X.transpose.flatten)
    val model = AutoregressionX.fitModel(dy, dx, 0, 1, includeOriginalX = false)

    model.c should be (intercept +- 1e-4)
    for (i <- xCoeffs.indices) {
      model.coefficients(i) should be (xCoeffs(i) +- 1e-4)
    }
  }

  // this test simply reduces to a normal regression model
  test("fit ARX(0, 0, true) model") {
    // note that
    val xCoeffs = Array(0.8, 0.2)
    val y = X.map(_.zip(xCoeffs).map { case (b, v) => b * v }.sum + intercept)
    val dy = new DenseVector(y)
    val dx = new DenseMatrix(rows = X.length, cols = X.head.length, data = X.transpose.flatten)
    val model = AutoregressionX.fitModel(dy, dx, 0, 0, includeOriginalX = true)

    model.c should be (intercept +- 1e-4)
    for (i <- xCoeffs.indices) {
      model.coefficients(i) should be (xCoeffs(i) +- 1e-4)
    }
  }

  // tests a model with no autoregressive term but with lagged exogenous variables
  // of order 2 and inclusive of the original X values
  test("fit ARX(0, 2, true) model") {
    val xLagCoeffs = Array(0.4, 0.15, 0.2, 0.7)
    val xLagged = Lag.lagMatTrimBoth(X, 2)
    val yLaggedPart = xLagged.map(_.zip(xLagCoeffs).map { case (b, v) => b * v }.sum )
    val xNormalCoeffs = Array(0.3, 0.5)
    val yNormalPart = X.map(_.zip(xNormalCoeffs).map { case (b, v) => b * v }.sum )
    val y = yLaggedPart.zip(yNormalPart.drop(2)).map { case (l, n) => l + n + intercept }

    val dy = new DenseVector(Array(0.0, 0.0) ++ y)
    val dx = new DenseMatrix(rows = X.length, cols = X.head.length, data = X.transpose.flatten)
    val model = AutoregressionX.fitModel(dy, dx, 0, 2, includeOriginalX = true)
    val combinedCoeffs = xLagCoeffs ++ xNormalCoeffs

    model.c should be (intercept +- 1e-4)
    for (i <- combinedCoeffs.indices) {
      model.coefficients(i) should be (combinedCoeffs(i) +- 1e-4)
    }
  }

  test("fit ARX(1, 1, false) model") {
    val xCoeffs = Array(0.8, 0.2)
    val xLagged = Lag.lagMatTrimBoth(X, 1)
    val rawY = xLagged.map(_.zip(xCoeffs).map { case (b, v) => b * v }.sum + intercept)
    val arCoeff = 0.4
    val y = rawY.scanLeft(0.0) { case (priorY, currY) => currY + priorY * arCoeff }.tail
    val dy = new DenseVector(Array(0.0) ++ y)
    val dx = new DenseMatrix(rows = X.length, cols = X.head.length, data = X.transpose.flatten)
    val model = AutoregressionX.fitModel(dy, dx, 1, 1, includeOriginalX = false)
    val combinedCoeffs = Array(arCoeff) ++ xCoeffs

    model.c should be (intercept +- 1e-4)
    for (i <- combinedCoeffs.indices) {
      model.coefficients(i) should be (combinedCoeffs(i) +- 1e-4)
    }
  }

  /**
    * 使用ARX预测
    */
  test("predict using ARX model") {
    val c = 0
    val xCoeffs = Array(-1.136026484226831e-08, 8.637677568908233e-07,
      15238.143039368977, -7.993535860373772e-09, -5.198597570089805e-07,
      1.5691547009557947e-08, 7.409621376205488e-08)
    val yMaxLag = 0
    val xMaxLag = 0
    val arxModel = new ARXModel(c, xCoeffs, yMaxLag, xMaxLag, includesOriginalX = true)

    val y = new DenseVector(Array(100.0))
    val x = new DenseMatrix(rows = 1, cols = 7, data = Array(465,1,0.006562479,24,1,0,51))

    val results = arxModel.predict(y, x)
    results.length should be (1)
    results(0) should be (y(0) +- 1e-4)
  }
}

