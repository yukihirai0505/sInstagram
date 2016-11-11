name := """sInstagram"""
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.play" % "play-json_2.11" % "2.5.9",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.3",
  "com.netaporter" %% "scala-uri" % "0.4.16",
  "org.slf4j" % "slf4j-nop" % "1.7.21",
  "com.github.tototoshi" %% "play-json-naming" % "1.1.0",
  "org.scalatest" % "scalatest_2.11" % "3.0.0"
)

lazy val sInstagram = project in file(".")