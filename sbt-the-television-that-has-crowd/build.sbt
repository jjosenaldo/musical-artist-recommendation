name := """recommender-system"""
organization := "br.ufrn"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.8"

libraryDependencies += guice
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.6"
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-core" % "3.6.0"
libraryDependencies += "com.datastax.cassandra" % "cassandra-driver-mapping" % "3.6.0"
//libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.18"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor"   % "2.5.18",
  "com.typesafe.akka" %% "akka-slf4j"   % "2.5.18",
  "com.typesafe.akka" %% "akka-remote"  % "2.5.18",
  "com.typesafe.akka" %% "akka-agent"   % "2.5.18",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.18" % "test"
)