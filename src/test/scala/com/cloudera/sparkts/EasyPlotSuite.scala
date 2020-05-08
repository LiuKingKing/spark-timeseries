package com.cloudera.sparkts

import breeze.plot.Figure
import org.apache.spark.mllib.linalg.DenseVector
import org.scalatest.FunSuite

class EasyPlotSuite extends FunSuite{

  test("test 1"){
    val f = EasyPlot.ezplot(Array(1.0,3,3,1,1,3,4,5,6,7,8,9));
    f.saveas("/home/liuking/idea/workspace/spark-timeseries/pics/pic1.png")
  }
}
