package org.cmhh

import org.apache.kafka.common.serialization.{Serializer, Deserializer, Serde, Serdes}

class MnistRecordSerde() extends Serde[MnistRecord] 
with Serializer[MnistRecord] with Deserializer[MnistRecord] with Serializable {
  override def serializer(): Serializer[MnistRecord] = this
  override def deserializer(): Deserializer[MnistRecord] = this
  override def close(): Unit = ()
  override def configure(configs: java.util.Map[String, _], isKey: Boolean): Unit = ()
  val Serde: Serde[MnistRecord] = Serdes.serdeFrom(this, this)

  override def serialize(topic: String, data: MnistRecord): Array[Byte] = 
    data.image.data :+ data.label.toByte

  override def deserialize(topic: String, data: Array[Byte]): MnistRecord = {
    val m = data.size - 1
    val n = math.sqrt(m).toInt
    if (n * n != m) sys.error("oops.")
    MnistRecord(
      Image(data.take(m), n, n, true),
      data(m).toInt
    )
  }
}