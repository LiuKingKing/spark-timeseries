/**
 * Copyright (c) 2015, Cloudera, Inc. All Rights Reserved.
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

import com.cloudera.sparkts.Lag
import com.cloudera.sparkts.MatrixUtil.{matToRowArrs, toBreeze}
import org.apache.commons.math3.random.RandomGenerator
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression
import org.apache.spark.mllib.linalg.{DenseVector, Vector}

/**
  * 自回归 AR
  */
object Autoregression {
  /**
    * 一阶自回归模型
    *
    * 描述当前值与历史值之间的关系
    *
   * Fits an AR(1) model to the given time series
   */
  def fitModel(ts: Vector): ARModel = fitModel(ts, 1)

  /**
    * n阶自回归模型
    *
   * Fits an AR(n) model to a given time series
    *
    * OLS-最小二乘法,
    *
    * 这里的intercept标示截距
    *
   * @param ts Data to fit
   * @param maxLag The autoregressive factor, terms t - 1 through t - maxLag are included
   * @param noIntercept A boolean to indicate if the regression should be run without an intercept,
   *                    the default is set to false, so that the OLS includes an intercept term
   * @return AR(n) model
   */
  def fitModel(ts: Vector, maxLag: Int, noIntercept: Boolean = false): ARModel = {
    // This is loosely based off of the implementation in statsmodels:
    // https://github.com/statsmodels/statsmodels/blob/master/statsmodels/tsa/ar_model.py

    // Make left hand side
    val Y = toBreeze(ts)(maxLag until ts.size)

    // Make lagged right hand side
    val X = Lag.lagMatTrimBoth(ts, maxLag)

    val regression = new OLSMultipleLinearRegression()
    regression.setNoIntercept(noIntercept) // drop intercept in regression
    regression.newSampleData(Y.toArray, matToRowArrs(X))
    //最小二乘的参数估计
    val params = regression.estimateRegressionParameters()
    val (c, coeffs) = if (noIntercept) (0.0, params) else (params.head, params.tail)
    new ARModel(c, coeffs)
  }
}

/**
  * 自回归模型
  * @param c
  * @param coefficients 系数
  */
class ARModel(val c: Double, val coefficients: Array[Double]) extends TimeSeriesModel {

  def this(c: Double, coef: Double) = this(c, Array(coef))

  def removeTimeDependentEffects(
      ts: Vector,
      destTs: Vector = null): Vector = {
    val dest = if (destTs == null) new Array[Double](ts.size) else destTs.toArray
    var i = 0
    while (i < ts.size) {
      //减去c
      dest(i) = ts(i) - c
      var j = 0
      while (j < coefficients.length && i - j - 1 >= 0) {
        dest(i) -= ts(i - j - 1) * coefficients(j)
        j += 1
      }
      i += 1
    }
    new DenseVector(dest)
  }

  def addTimeDependentEffects(ts: Vector, destTs: Vector): Vector = {
    val dest = if (destTs == null) new Array[Double](ts.size) else destTs.toArray
    var i = 0
    while (i < ts.size) {
      dest(i) = c + ts(i)
      var j = 0
      while (j < coefficients.length && i - j - 1 >= 0) {
        dest(i) += dest(i - j - 1) * coefficients(j)
        j += 1
      }
      i += 1
    }
    new DenseVector(dest)
  }

  /**
    * 采样
    * @param n
    * @param rand
    * @return
    */
  def sample(n: Int, rand: RandomGenerator): Vector = {
    val vec = new DenseVector(Array.fill[Double](n)(rand.nextGaussian()))
    addTimeDependentEffects(vec, vec)
  }
}
