import scala.util.Failure

name := """Briventory"""
maintainer := "briventory@varani.ch"

// Common Settings
scalaVersion := "2.13.13"

organization := "ch.varani"
version := "0.1.0-SNAPSHOT"

// Disable the ScalaDoc generation
Compile / doc / sources := Seq.empty
Compile / packageDoc / publishArtifact := false

// Treat warning as error
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
javacOptions ++= Seq("-Xlint:all", "-Xlint:-processing", "-Werror")
javaOptions ++= Seq("--illegal-access=permit")

// Include public assets during tests
Test / unmanagedResourceDirectories += baseDirectory(_ / "target/web/public/test").value
Test / fork := true

// Make verbose tests
testOptions += Tests.Argument(jupiterTestFramework, "-v")

// Checkstyle Directives
checkstyleConfigLocation := CheckstyleConfigLocation.File("varani_java_checks.xml")
checkstyleSeverityLevel := Some(CheckstyleSeverityLevel.Error)

lazy val root = project.in(file(".")).enablePlugins(PlayJava, SbtWeb, BuildInfoPlugin).settings(
  buildInfoKeys := Seq[BuildInfoKey](name, version),
  buildInfoObject := "BriventoryBuildInfo",
  buildInfoPackage := "utils"
)

// To check the the updatable plugin version, do the following steps:
// 1. sbt project plugins
// 2. sbt dependencyUpdates
// 3. sbt project root
// Note: most of the plugins need Scala 2.12, it is not possible to switch to Scala 2.13, for now.
lazy val plugins = project in file("./project")

// JPA Dependencies
libraryDependencies ++= Seq(
  javaJdbc,

  guice,

  "javax.validation" % "validation-api" % "2.0.1.Final",
  "jakarta.persistence" % "jakarta.persistence-api" % "3.1.0",
  "jakarta.validation" % "jakarta.validation-api" % "3.0.2",
  "org.jooq" % "jooq" % "3.19.5",
  "org.jooq" % "jooq-codegen" % "3.19.5",
  "org.jooq" % "jooq-meta" % "3.19.5",
  "org.jooq" % "jool" % "0.9.15",

  /*"javax.cache" % "cache-api" % "1.1.1",*/
  "org.ehcache" % "ehcache" % "3.10.8",
  "io.dropwizard.metrics" % "metrics-core" % "4.2.25"
)

// Libraries
libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-text" % "1.11.0",
  "commons-validator" % "commons-validator" % "1.8.0",
  "org.postgresql" % "postgresql" % "42.7.2",
  "org.hsqldb" % "hsqldb" % "2.7.2",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.16.1",
  "com.fasterxml.jackson.module" % "jackson-module-scala_2.13" % "2.16.1",
  "org.semver" % "api" % "0.9.33",
  "me.gosimple" % "nbvcxz" % "1.5.1",
  "at.favre.lib" % "bcrypt" % "0.10.2"
)

// To remove when Play2.9 is released
libraryDependencies ++= Seq(
  "io.jsonwebtoken" % "jjwt-api" % "0.12.5",
  "io.jsonwebtoken" % "jjwt-impl" % "0.12.5",
  "io.jsonwebtoken" % "jjwt-jackson" % "0.12.5"
)

// WebJars
libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "3.0.1",
  "org.webjars" % "jquery" % "3.7.1",
  "org.webjars" % "jquery-ui" % "1.13.2",
  "org.webjars" % "popper.js" % "2.11.7",
  "org.webjars" % "bootstrap" % "5.3.3",
  "org.webjars" % "font-awesome" % "6.5.1",
  "org.webjars.bowergithub.dropbox" % "zxcvbn" % "4.4.2"
)

// Tests libraries
resolvers += Resolver.jcenterRepo
libraryDependencies ++= Seq(
  "org.assertj" % "assertj-core" % "3.25.3" % Test,
  "org.awaitility" % "awaitility" % "4.2.0" % Test,
  "net.aichler" % "jupiter-interface" % "0.11.1" % Test,
  "org.junit.jupiter" % "junit-jupiter-api" % "5.10.2" % Test,
  "org.junit.jupiter" % "junit-jupiter-engine" % "5.10.2" % Test,
  "org.junit.jupiter" % "junit-jupiter-params" % "5.10.2" % Test,
  "org.junit.platform" % "junit-platform-runner" % "1.10.2" % Test
)

// Dependencies Check Directives
dependencyUpdatesFailBuild := true
dependencyUpdatesFilter -= moduleFilter(organization = "org.jacoco", name = "org.jacoco.agent")

// SonarQube parameters
sonarProperties ++= Map(
  "sonar.projectName" -> System.getenv("CI_PROJECT_NAME"),
  "sonar.host.url" -> System.getenv("SONAR_HOST"),
  "sonar.java.source" -> "17",
  "sonar.java.binaries" -> "./target/scala-2.13/classes",
  "sonar.java.test.binaries" -> "./target/scala-2.13/test-classes",
  "sonar.login" -> System.getenv("SONAR_TOKEN"),
  "sonar.gitlab.commit_sha" -> System.getenv("CI_COMMIT_SHA"),
  "sonar.gitlab.ref_name" -> System.getenv("CI_COMMIT_REF_NAME"),
  "sonar.gitlab.project_id" -> System.getenv("CI_PROJECT_ID"),
  "sonar.gitlab.url" -> System.getenv("GL_SERVER"),
  "sonar.gitlab.user_token" -> System.getenv("GL_TOKEN"),
  "sonar.junit.reportsPath" -> "./target/test-reports",
  "sonar.jacoco.reportPaths" -> "./target/scala-2.13/jacoco/data/jacoco.exec"
)

addCommandAlias("pipeline", ";clean;dependencyUpdates;checkstyle;compile;test;jacoco")

val jooqDynamicCodegen = Def.taskDyn {
  val jooqFiles = ((sourceManaged.value / "main/jooq") ** "*.java").get
  if (jooqFiles.isEmpty)
    Def.task {
      val classpath = (Compile / managedClasspath).value.files
      val options = Seq("briventory.xml")
      val result = runner.value.run("org.jooq.codegen.GenerationTool", classpath, options, streams.value.log)
      result match {
        case Failure(e) => sys.error(e.getMessage)
        case _ => None
      }
      ((sourceManaged.value / "main/jooq") ** "*.java").get
    }
  else
    Def.task {
      jooqFiles
    }
}

lazy val jooqCodegen = taskKey[Seq[File]]("JOOQ Code Generation")
jooqCodegen := {
  jooqDynamicCodegen.value
}

// Compile / sourceGenerators += jooqCodegen
