package org.cmhh

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j

case class MnistRecord(image: Image, label: Int) {
  def toArray: (Array[Double], Array[Double]) = {
    (
      image.data.map(x => (x & 0xff).toInt / 255f),
      (0 to 9).map(i => if (i == label) 1f else 0.0).toArray
    )
  }

  def toNd4j: (INDArray, INDArray) = {
    (
      Nd4j.createFromArray(Array(image.data.map(x => (x & 0xff).toInt / 255f))),
      Nd4j.createFromArray(Array((0 to 9).map(i => if (i == label) 1f else 0).toArray))
    )
  }
}