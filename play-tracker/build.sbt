name := """play-tracker"""
organization := "org.bjason"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.3"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
libraryDependencies += "com.netflix.eureka" % "eureka-client" % "1.9.13" 
libraryDependencies += ws
libraryDependencies += ehcache

lazy val root = (project in file(".")).enablePlugins(PlayScala)

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "org.bjason.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "org.bjason.binders._"

