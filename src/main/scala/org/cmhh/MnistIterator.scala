package org.cmhh

import java.util.zip.GZIPInputStream
import scala.io.Source

object MnistType {
  sealed trait Type
  case object TEST extends Type { override def toString = "TEST" }
  case object TRAIN extends Type { override def toString = "TRAIN" }
}

/**
  * MNIST image iterator
  *
  * @param mnistType [[MnistType.TEST]] or [[MnistType.TRAIN]]
  */
case class MnistIterator(mnistType: MnistType.Type) extends Iterator[MnistRecord] {
  private val (lblStr, imgStr) = mnistType match {
    case MnistType.TEST => 
      (
        getStream("/t10k-labels-idx1-ubyte.gz"), 
        getStream("/t10k-images-idx3-ubyte.gz")
      )
    case MnistType.TRAIN => 
      (
        getStream("/train-labels-idx1-ubyte.gz"), 
        getStream("/train-images-idx3-ubyte.gz")
      )
  }

  { val _: Int = getInt(lblStr.readNBytes(4)) }
  { val _: Int = getInt(imgStr.readNBytes(4)) }

  private val n1: Int = getInt(lblStr.readNBytes(4))
  private val n2: Int = getInt(imgStr.readNBytes(4))

  if (n1 != n2) {
    lblStr.close()
    imgStr.close()
    sys.error("Incompatible labels and features.")
  }

  override val size: Int = n1
  val rows: Int = getInt(imgStr.readNBytes(4))
  val cols: Int = getInt(imgStr.readNBytes(4))
  private var pos: Int = 0

  def hasNext: Boolean = pos < size

  def next(): MnistRecord = {
    pos = pos + 1
    MnistRecord(
      Image(imgStr.readNBytes(rows * cols), rows, cols, true),
      lblStr.read()
    )
  }

  private def getStream(resource: String): GZIPInputStream = 
    new GZIPInputStream(getClass.getResourceAsStream(resource))

  private def getInt(bytes: Array[Byte]): Int = {
    List(
      (bytes(3) & 0xff),
      (bytes(2) & 0xff) <<  8,
      (bytes(1) & 0xff) << 16,
      (bytes(0) & 0xff) << 24
    ).sum
  }
}