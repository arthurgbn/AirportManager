name := """AirportManager"""

version := "1.0-SNAPSHOT"


lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.14"

libraryDependencies ++= Seq(
  jdbc, ws, evolutions, jodaForms,
  "org.playframework.anorm" %% "anorm" % "2.7.0",
  "org.playframework" %% "play-mailer" % "10.0.0",
  "org.playframework" %% "play-mailer-guice" % "10.0.0",
  "com.h2database" % "h2" % "1.4.200", // IMPORTANT! Keep on this version, evolutions scripts break otherwise
  "com.github.nscala-time" %% "nscala-time" % "2.32.0",
  "net.sf.biweekly" % "biweekly" % "0.6.8",
  "org.postgresql" % "postgresql" % "42.7.3",
  "io.github.samueleresca" %% "pekko-quartz-scheduler" % "1.2.0-pekko-1.0.x",
  "com.lunatech" %% "play-googleopenconnect" % "3.0.1",
  "org.json" % "json" % "20240303",
  "com.google.crypto.tink" % "tink" % "1.13.0"

)

Global / onChangedBuildSource := ReloadOnSourceChanges


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.example.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.example.binders._"
