package org.cmhh

import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.serialization._
import scala.jdk.CollectionConverters._
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Success, Failure}
import java.util.Properties
import java.time.{Duration => JDuration}
import org.nd4j.evaluation.classification.Evaluation

object ConsumerApp extends App {
  val t = new Thread {
    override def run: Unit = try {
      val props = new Properties()
      props.put("bootstrap.servers", "localhost:9092")
      props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
      props.put("value.deserializer", "org.cmhh.MnistRecordSerde")
      props.put("group.id", "mnist-train")
      props.put("auto.offset.reset", "earliest")
      props.put("enable.auto.commit", false)

      val consumer = new KafkaConsumer[String, MnistRecord](props)
      consumer.subscribe(java.util.Collections.singletonList("mnist"))

      val network = model.ann() 
      val mnistTest = new MnistDataSetIterator(100, MnistType.TEST)

      while (true) {
        val recs = consumer.poll(JDuration.ofMillis(1000))
        if (recs.count() > 0) {
          val it = recs.iterator
          while(it.hasNext) { 
            val im = it.next().value.toNd4j
            network.fit(im._1, im._2)
          }

          val eval = network.evaluate[Evaluation](mnistTest)
          println(f"number read: %%d, accuracy: %%1.4f".format(recs.count(), eval.accuracy))
        }
      }
    } catch {
        case e: Throwable =>
          println("Program terminated.\n\n")
          println(e.getMessage())
    }
  }

  t.start
  scala.io.StdIn.readLine("\n\nPress ENTER to stop...\n\n")
  t.interrupt()
  t.join()
}