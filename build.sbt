organization := "com.github"

name := "scalacsv"

version := "0.1.1"

scalaVersion := "2.11.8"

lazy val shapelessV = "2.3.3"

lazy val scalatestV = "3.0.1"

libraryDependencies ++= Seq(
  "com.chuusai" %% "shapeless" % shapelessV,
  "org.scalatest" %% "scalatest" % scalatestV % Test
)

resolvers ++= Seq(
  Resolver.sonatypeRepo("releases"),
  Resolver.sonatypeRepo("snapshots")
)