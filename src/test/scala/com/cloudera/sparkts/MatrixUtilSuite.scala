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

package com.cloudera.sparkts

import com.cloudera.sparkts.MatrixUtil._
import org.apache.spark.mllib.linalg.{Matrices, Vectors}
import org.scalatest._

class MatrixUtilSuite extends FunSuite with ShouldMatchers {


  test("transpose"){
    val arr = Array(Array(2.0,1.0,4.0),Array(3.0,2.0,1.0),Array(5.0,2.0,1.0),Array(5.0,2.0,3.0))
    arr.head.length should be (3)
    arr.length should be (4)

    arr.head should be (Array(2.0,1.0,4.0))

    arr.head.indices should be (Range(0,3,1))

    arr(0) should be (Array(2.0,1.0,4.0))

    //
    arr.map(_(0)) should be (Array(2.0,3.0,5.0,5.0))


    transpose(arr) should be (Array(Array(2.0,3.0,5.0,5.0),Array(1.0,2.0,2.0,2.0),Array(4.0,1.0,1.0,3.0)))
  }


  test("modifying toBreeze version modifies original tensor") {
    val vec = Vectors.dense(1.0, 2.0, 3.0)
    val breezeVec = toBreeze(vec)
    breezeVec(1) = 4.0
    vec(1) should be (4.0)

    val mat = Matrices.zeros(3, 4)
    val breezeMat = toBreeze(mat)
    breezeMat(0, 1) = 2.0
    mat(0, 1) should be (2.0)
  }
}
