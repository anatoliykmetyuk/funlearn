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
        "com.lihaoyi" %%% "scalatags" % "0.13.1",  // https://javadoc.io/doc/com.lihaoyi/scalatags_3  https://com-lihaoyi.github.io/scalatags/
        "org.scala-js" %%% "scalajs-dom" % "2.8.1"  // https://javadoc.io/doc/org.scala-js/scalajs-dom_sjs1_3
      )
    )
  )
  .jsSettings(
    // scalaJSUseMainModuleInitializer := true,
    crossTarget in (Compile, fastOptJS) := baseDirectory.value / ".." / "static"
  )
  .jvmSettings(
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion, // https://javadoc.io/doc/com.softwaremill.sttp.tapir/tapir-core_3
      "com.softwaremill.sttp.tapir" %% "tapir-files" % tapirVersion, // https://javadoc.io/doc/com.softwaremill.sttp.tapir/tapir-files_3
      "com.softwaremill.sttp.tapir" %% "tapir-netty-server-sync" % tapirVersion, // https://javadoc.io/doc/com.softwaremill.sttp.tapir/tapir-netty-server-sync_3
      "com.softwaremill.ox" %% "core" % "1.0.0", // https://javadoc.io/doc/com.softwaremill.ox/core_3
      "com.lihaoyi" %%% "scalatags" % "0.13.1", // https://javadoc.io/doc/com.lihaoyi/scalatags_3
      "ch.qos.logback" % "logback-classic" % "1.5.18", // https://javadoc.io/doc/ch.qos.logback/logback-classic_3
      "com.augustnagro" %% "magnum" % "1.3.0", // https://javadoc.io/doc/com.augustnagro/magnum_3
      "org.xerial" % "sqlite-jdbc" % "3.50.3.0", // https://javadoc.io/doc/org.xerial/sqlite-jdbc_3
      "com.zaxxer" % "HikariCP" % "7.0.2", // https://javadoc.io/doc/com.zaxxer/HikariCP_3
    )
  )