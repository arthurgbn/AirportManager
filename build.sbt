name := """AirportManager"""

version := "1.0-SNAPSHOT"


lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies ++= Seq(
  jdbc, evolutions,guice,
  "org.playframework.anorm" %% "anorm" % "2.7.0",
  "com.h2database" % "h2" % "1.4.200",
  "org.playframework.silhouette" %% "play-silhouette" % "10.0.0",
  "org.playframework.silhouette" %% "play-silhouette-persistence" % "10.0.0",
  "org.playframework.silhouette" %% "play-silhouette-crypto-jca" % "10.0.0",
  "org.playframework.silhouette" %% "play-silhouette-password-bcrypt" % "10.0.0",
  "net.codingwell" %% "scala-guice" % "6.0.0",
  "org.playframework.silhouette" %% "play-silhouette-testkit" % "10.0.0" % "test"
)

Global / onChangedBuildSource := ReloadOnSourceChanges

