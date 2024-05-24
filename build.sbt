name := """AirportManager"""

version := "1.0-SNAPSHOT"


lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies ++= Seq(
  jdbc, evolutions,guice,
  "org.playframework.anorm" %% "anorm" % "2.7.0",
  "com.h2database" % "h2" % "1.4.200",
)

Global / onChangedBuildSource := ReloadOnSourceChanges

