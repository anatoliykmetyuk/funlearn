val tapirVersion = "1.11.42"

lazy val rootProject = (project in file(".")).settings(
  Seq(
    name := "funlearn",
    version := "0.1.0-SNAPSHOT",
    organization := "anatoliikmt",
    scalaVersion := "3.7.2",
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-netty-server-sync" % tapirVersion,
      "com.softwaremill.ox" %% "core" % "1.0.0",
      "com.lihaoyi" %% "scalatags" % "0.13.1",
      "ch.qos.logback" % "logback-classic" % "1.5.18"
    )
  )
)