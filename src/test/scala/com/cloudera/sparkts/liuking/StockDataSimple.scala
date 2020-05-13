package com.cloudera.sparkts.liuking
import breeze.linalg.DenseVector
import breeze.plot.{Figure, plot}
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


  /**
    *
    */
  private def loadData = {
    val dataFile1 = getClass.getClassLoader.getResourceAsStream("data/600237.json")
    val origin = JSON.parseFull(Source.fromInputStream(dataFile1).mkString)

    //    val path = getClass.getClassLoader.getResource("data/600237.json").getPath
    //    val origin = JSON.parseFull(Source.fromFile("/home/liuking/idea/workspace/spark-timeseries/data/600237.json").mkString)

    def extrJson1(json: Option[Any]) = json match {
      case Some(map: Map[String, Any]) => map
    }

    def extrJson2(json: Option[Any]) = json match {
      case Some(mat: List[List[Double]]) => mat
    }

    val dailyData = extrJson2(extrJson1(extrJson1(origin).get("data")).get("dailyData"))

    val price = dailyData.transpose.take(3)(2).toArray
    price
  }

  test("简单时间序列数据绘制") {
    //data api : https://api.doctorxiong.club/v1/stock/detail?code=600237&year=2020


    val price: _root_.scala.Array[Double] = loadData
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


  test("3-sigma rule"){
    val price: _root_.scala.Array[Double] = loadData
    println(price.toList)
    val meanAdnVari = meanAndVariance(price)

    val sigma3Positive = Array.fill(meanAdnVari.count.intValue())(meanAdnVari.mean+3*math.sqrt(meanAdnVari.variance))
    println(sigma3Positive.toList)
    val sigma3Nagitive = Array.fill(meanAdnVari.count.intValue())(meanAdnVari.mean-3*math.sqrt(meanAdnVari.variance))
    println(sigma3Nagitive.toList)

    val f = Figure()
    val p = f.subplot(0)
    p += plot(sigma3Positive.indices.map(_.toDouble).toArray, sigma3Positive, style = '-')
    p += plot(sigma3Nagitive.indices.map(_.toDouble).toArray, sigma3Nagitive, style = '-')
    p += plot(price.indices.map(_.toDouble).toArray, price, style = '.')
    p.title_=("3-sigma")
    p.xlabel_=("date")
    p.ylabel_=("price")

    f.saveas(picPath+"600237-3-sigma.png")
  }

  /**
    * 时间序列分析第二章-第一题
    */
  test("TimeSeriesAnalysis-2-1"){
    val data = Range(1,21).map(_*1.0).toArray
    println(data.toList)
//    val meanAndVari = meanAndVariance(data)
//    println(meanAndVari)

    //mean_t
    val sumt = data.scanLeft(0.0)(_+_).tail
    val meant = sumt.zip(1 until(sumt.length)).map{case (num,len)=>num/len}
    println("mean t:"+meant.toList)

    //variance_t
    val diffPower = data.zip(meant).map{case (d,m)=>(d-m)*(d-m)}
    var variancet = diffPower.scanLeft(0.0)(_+_).tail.zip(1 until(diffPower.length)).map{case (num,len)=>num/len}
    println("variance t:"+variancet.toList)

    val mat = UnivariateTimeSeries.lag(Vectors.dense(data),1,true)
    println(mat)

    val diff = UnivariateTimeSeries.differencesAtLag(Vectors.dense(data),1)
    println(diff)

    val corr = UnivariateTimeSeries.autocorr(data,1)
    println(corr.toList)


  }


}
