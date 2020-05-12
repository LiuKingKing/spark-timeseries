package com.cloudera.sparkts.liuking
import breeze.linalg.DenseVector
import breeze.stats._
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

    val price = dailyData.transpose.take(3)(2).toArray
    println(price.toList)
//    EasyPlot.ezplot(price,'-').saveas(picPath+"600237.png")

    //一阶差分
//    val diff1 = UnivariateTimeSeries.differencesOfOrderD(MatrixUtil.fromBreeze(DenseVector(price)),1)
//    val diff1 = UnivariateTimeSeries.differencesOfOrderD(Vectors.dense(price),1)
//    print(diff1)
//    println()
//    val diff2 = UnivariateTimeSeries.differencesOfOrderD(Vectors.dense(price),2)
//    print(diff2)
//    println()
//    val diff2lag = UnivariateTimeSeries.differencesAtLag(Vectors.dense(price),2)
//    print(diff2lag)



    //均值
    val u = price.sum/price.length
    println("mean:"+u)
//    println(mean(price))
    //均值2
    val sumt = price.scanLeft(0.0)(_+_).tail
//    println(sumt.toList)
    val ut = sumt.zip(1 until(sumt.length)).map{case (s,l)=>s/l}
    println("mean t:"+ut.toList);
//    EasyPlot.ezplot(ut,'-').saveas(picPath+"600237-u.png")

    println(meanAndVariance(price))

    //方差
//    price.map(p=>math.pow(p-u,2.0))
    val secondPower = price.map(p=>(p-u)*(p-u))
    val variance = secondPower.sum/(secondPower.length-1)
    println("variance:"+variance)

    //方差2
    val secondPowert = price.zip(ut).map{case (p,u) => (p-u)*(p-u)}
    val variancet = secondPowert.scanLeft(0.0)(_+_).tail.zip(1 until(secondPowert.length)).map{case (s,l)=>s/l}
    println("variance t:"+variancet.toList)
    //自相关系数 pk(k=1,2,3,4,5,6)

    val middle :Double = median(DenseVector(price))
    println("middle:"+middle)



  }

  /**
    * 时间序列分析第二章-第一题
    */
  test("TimeSeriesAnalysis-2-1"){

  }


}
