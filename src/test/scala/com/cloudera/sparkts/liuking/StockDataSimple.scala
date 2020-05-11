package com.cloudera.sparkts.liuking
import breeze.linalg.DenseVector
import com.cloudera.sparkts.{EasyPlot, MatrixUtil, UnivariateTimeSeries}
import org.apache.spark.mllib.linalg.Vectors
import org.scalatest.{FunSuite, ShouldMatchers}

import scala.io.Source
import scala.util.parsing.json.{JSON, JSONObject}

/**
  * 简单数据采样绘制
  */
class StockDataSimple extends FunSuite with ShouldMatchers {

//  val picPath = "/home/liuking/idea/workspace/spark-timeseries/pics/"
    val picPath = getClass.getClassLoader.getResource("pics/").getFile

  test("简单时间序列数据绘制") {
    //data api : https://api.doctorxiong.club/v1/stock/detail?code=600237&year=2020


    val dataFile1 = getClass.getClassLoader.getResourceAsStream("data/600237.json")
    val origin = JSON.parseFull(Source.fromInputStream(dataFile1).mkString)

    //    val path = getClass.getClassLoader.getResource("data/600237.json").getPath
//    val origin = JSON.parseFull(Source.fromFile("/home/liuking/idea/workspace/spark-timeseries/data/600237.json").mkString)

    def extrJson1(json : Option[Any])= json match {
      case Some(map: Map[String,Any]) => map
    }

    def extrJson2(json : Option[Any])= json match {
      case Some(mat: List[List[Double]])  => mat
    }

    val dailyData = extrJson2(extrJson1(extrJson1(origin).get("data")).get("dailyData"))

//    val test = dailyData.tail
//    val ddMat = new DenseMatrix(rows=dailyData.length, cols=dailyData.head.length, data=dailyData.transpose.toArray.flatten);
//    println(ddMat(::,2))
    val price = dailyData.transpose.take(3)(2).toArray
    println(price.toList)
    EasyPlot.ezplot(price,'-').saveas(picPath+"600237.png")

    //一阶差分
//    val diff1 = UnivariateTimeSeries.differencesOfOrderD(MatrixUtil.fromBreeze(DenseVector(price)),1)
    val diff1 = UnivariateTimeSeries.differencesOfOrderD(Vectors.dense(price),1)
    print(diff1)
    println()
    val diff2 = UnivariateTimeSeries.differencesOfOrderD(Vectors.dense(price),2)
    print(diff2)
    println()
    val diff2lag = UnivariateTimeSeries.differencesAtLag(Vectors.dense(price),2)
    print(diff2lag)



  }



}
