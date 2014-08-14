import sbt._
import Keys._

object MacroBuild extends Build {
  val buildOrganization = "edu.cmu.cs"
  val buildVersion      = "0.1-SNAPSHOT"
  val buildScalaVersion = "2.11.0"

  val buildSettings = Defaults.defaultSettings ++ Seq (
    organization := buildOrganization,
    version      := buildVersion,
    scalaVersion := buildScalaVersion,
    fork         := true,
    autoScalaLibrary := true,
    scalacOptions ++= Seq("-feature", "-deprecation", "-Ymacro-debug-verbose", "-Yshow-trees-stringified")
  )

  val mavenResolver = "Maven Central Server" at "http://central.maven.org/maven2"

  val commonDeps = Seq (
    "org.scalaz"                 %% "scalaz-core"          % "7.0.6"
  )

  val mkapp = TaskKey[File]("mkapp")

  lazy val root = Project (
    "root",
    file(".")
  ) aggregate(macros, core)

  lazy val core = Project (
    "core",
    file("core"),
    settings = buildSettings ++ Seq (
      libraryDependencies ++= commonDeps,
      resolvers += mavenResolver,
      javaOptions += "-Xss128M",
      mkapp := {
        val classpath = (fullClasspath in Runtime).value.files.absString
        val template = """#!/bin/sh
        java -Xmx2g -Xss4m -classpath "%s" %s $@
        """

        val master = template.format(classpath, "scalaMacros.application.Main")
        val masterOut = baseDirectory.value / "../bin/application.sh"
        IO.write(masterOut, master)
        masterOut.setExecutable(true)

        masterOut
      }
    )
  ) dependsOn(macros)

  lazy val macros = Project(
    "macros",
    file("macros"),
    settings = buildSettings ++ Seq (
      libraryDependencies ++= (commonDeps
                          ++ Seq("org.scala-lang" % "scala-compiler" % "2.11.1",
                                 "org.scala-lang" % "scala-reflect" % "2.11.1"))
    )
  )
}
