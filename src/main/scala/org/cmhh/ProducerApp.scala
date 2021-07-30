package org.cmhh

import org.apache.kafka.clients.producer._
import org.apache.kafka.common.serialization._
import scala.jdk.CollectionConverters._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import java.util.Properties

object ProducerApp extends App {
  val delay = if (args.size > 0) args(0).toInt else 0
  val trainIt: MnistIterator = MnistIterator(MnistType.TRAIN)
  val n = trainIt.size

  val props = new Properties()
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.cmhh.MnistRecordSerde")

  val producer = new KafkaProducer[String, MnistRecord](props)

  print("\n\nSending messages...\n\n0.0%...")

  val res = Future.sequence(trainIt.zipWithIndex.map(rec => {
    val msg = new ProducerRecord[String, MnistRecord](
      "mnist", 
      s"%05d".format(rec._2), 
      rec._1
    )

    if ((rec._2 + 1) % (n / 10) == 0) print(s"${(rec._2 + 1).toDouble / n * 100}%... ")
    if (delay > 0) Thread.sleep(delay)
    Future { producer.send(msg).get }
  }))

  Await.ready(res, Duration.Inf)

  print("\n\nDone.\n\n")
}