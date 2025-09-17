ThisBuild / scalaVersion := "3.7.2"

val tapirVersion = "1.11.42"

lazy val root = project.in(file(".")).
  aggregate(funlearn.js, funlearn.jvm).
  settings(
    publish := {},
    publishLocal := {},
  )

lazy val funlearn = crossProject(JSPlatform, JVMPlatform).in(file("."))
  .enablePlugins(ScalaJSPlugin)
  .settings(
    Seq(
      name := "funlearn",
      version := "0.1.0-SNAPSHOT",
      organization := "anatoliikmt",
      libraryDependencies ++= Seq(
        "com.lihaoyi" %%% "scalatags" % "0.13.1",
      )
    )
  )
  .jsSettings(
    scalaJSUseMainModuleInitializer := true,
    crossTarget in (Compile, fastOptJS) := baseDirectory.value / ".." / "static"
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-files" % tapirVersion,
      "com.softwaremill.sttp.tapir" %% "tapir-netty-server-sync" % tapirVersion,
      "com.softwaremill.ox" %% "core" % "1.0.0",
      "com.lihaoyi" %%% "scalatags" % "0.13.1",
      "ch.qos.logback" % "logback-classic" % "1.5.18",
      "com.augustnagro" %% "magnum" % "1.3.0",
      "org.xerial" % "sqlite-jdbc" % "3.50.3.0",
      "com.zaxxer" % "HikariCP" % "7.0.2",
    )
  )