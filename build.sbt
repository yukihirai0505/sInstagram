import SonatypeKeys._

sonatypeSettings

name := "sInstagram"

version := "0.2.0"

scalaVersion := "2.11.8"

scalacOptions += "-feature"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "com.yukihirai0505" % "scala-request-json_2.11" % "1.6",
  "org.scalatest" % "scalatest_2.11" % "3.0.0"
)

// when library release, it should be false
coverageEnabled := false
publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

organization := "com.yukihirai0505"

organizationName := "com.yukihirai0505"

profileName := "com.yukihirai0505"

organizationHomepage := Some(url("https://yukihirai0505.github.io"))

description := "A Scala library for the Instagram API"

pomExtra :=
  <url>https://github.com/yukihirai0505/sInstagram</url>
    <licenses>
      <license>
        <name>MIT</name>
        <url>https://github.com/yukihirai0505/sInstagram/blob/master/LICENSE.txt</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:yukihirai0505/sInstagram.git</url>
      <connection>scm:git:git@github.com:yukihirai0505/sInstagram.git</connection>
    </scm>
    <developers>
      <developer>
        <id>yukihirai0505</id>
        <name>Yuki Hirai</name>
        <url>https://yukihirai0505.github.io</url>
      </developer>
    </developers>
