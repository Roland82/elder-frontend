name := """play-scala"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.3",
  "org.scalaj"    %% "scalaj-http" % "2.0.0",
  "net.liftweb"   %% "lift-json" % "3.0-M6",


  "de.leanovate.play-mockws" %% "play-mockws" % "2.4.2" % "test",

  specs2 % Test
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


//fork in run := true