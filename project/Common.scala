import play.sbt.PlayImport.{javaJdbc, javaJpa}
import sbt.Keys.{javaOptions, javacOptions, publishArtifact, scalacOptions, _}
import sbt._

object Common {
  val settings: Seq[Setting[_]] = Seq(
    scalaVersion := "2.13.1",

    organization := "ch.varani",
    version := "1.0.0-SNAPSHOT",

    // Disable the ScalaDoc generation
    sources in(Compile, doc) := Seq.empty,
    publishArtifact in(Compile, packageDoc) := false,

    // Treat warning as error
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings"),
    javacOptions ++= Seq("-Xlint:all", "-Xlint:-processing", "-Werror"),
    javaOptions ++= Seq("--illegal-access=warn")
  )

  val testSettings: Seq[Setting[_]] = Seq(
    // Make verbose tests
    testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))
  )

  val jpaDependency = Seq(
    javaJdbc,
    javaJpa,
    "org.hibernate" % "hibernate-core" % "5.4.14.Final"
  )

  val jooqDependencies = Seq(
    "org.jooq" % "jooq-codegen" % "3.13.1" % Compile,
    "org.jooq" % "jooq-meta" % "3.13.1" % Compile,
    "org.jooq" % "jooq-meta-extensions" % "3.13.1" % Compile
  )
}
