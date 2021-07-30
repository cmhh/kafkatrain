package org.cmhh

import org.nd4j.linalg.dataset.api.iterator.DataSetIterator
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import scala.jdk.CollectionConverters._

class MnistDataSetIterator(batchSize: Int, mnistType: MnistType.Type) extends DataSetIterator {
  private var it = MnistIterator(mnistType)
  val asyncSupported: Boolean = true
  val batch: Int = batchSize
  val getLabels: java.util.List[String] = (0 to 9).map(_.toString).asJava
  val getPreProcessor: org.nd4j.linalg.dataset.api.DataSetPreProcessor = null // like, wtf?
  val inputColumns: Int = 28 * 28
  val totalOutcomes: Int = 10
  val resetSupported: Boolean = true
  def next(): org.nd4j.linalg.dataset.DataSet = next(batch)
  def hasNext(): Boolean = it.hasNext
  def setPreProcessor(p: org.nd4j.linalg.dataset.api.DataSetPreProcessor): Unit = ()

  def next(n: Int): DataSet = {
    def loop(n: Int, accum: Seq[MnistRecord]): Seq[MnistRecord] = {
      if (!it.hasNext || n < 1) accum
      else loop (n - 1, accum :+ it.next())
    }

    val batch = loop(n, Seq.empty).map(_.toArray)

    new DataSet(
      Nd4j.createFromArray(batch.map(_._1.toArray).toArray),
      Nd4j.createFromArray(batch.map(_._2.toArray).toArray)
    )
  }

  def reset(): Unit = {
    it = MnistIterator(mnistType)
  }
}