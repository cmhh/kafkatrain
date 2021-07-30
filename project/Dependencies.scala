import sbt._

object Dependencies {
  lazy val kafka = "org.apache.kafka" %% "kafka" % "2.8.0"
  lazy val kafkaclients = "org.apache.kafka" % "kafka-clients" % "2.8.0"
  /*
  lazy val dl4jcore = "org.deeplearning4j" % "deeplearning4j-core" % "1.0.0-M1.1"
  lazy val nd4j = "org.nd4j" % "nd4j-native-platform" % "1.0.0-M1.1"
  */
  lazy val dl4jcore = "org.deeplearning4j" % "deeplearning4j-cuda-11.2" % "1.0.0-M1.1"
  lazy val nd4j = "org.nd4j" % "nd4j-cuda-11.2-platform" % "1.0.0-M1.1"
  lazy val slf4japi = "org.slf4j" % "slf4j-api" % "1.7.32"
  lazy val slf4jlog4j = "org.slf4j" % "slf4j-log4j12" % "1.7.32"
  lazy val log4j = "org.apache.logging.log4j" % "log4j-core" % "2.14.1" 
}
