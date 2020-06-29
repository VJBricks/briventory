name := """Briventory"""
maintainer := "briventory@varani.ch"

// Common Settings
scalaVersion := "2.13.3"

organization := "ch.varani"
version := "1.0.0-SNAPSHOT"

// Disable the ScalaDoc generation
sources in(Compile, doc) := Seq.empty
publishArtifact in(Compile, packageDoc) := false

// Treat warning as error
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
javacOptions ++= Seq("-Xlint:all", "-Xlint:-processing", "-Werror")
javaOptions ++= Seq("--illegal-access=warn")

// Tests Settings
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

// Checkstyle Directives
checkstyleConfigLocation := CheckstyleConfigLocation.File("varani_java_checks.xml")
checkstyleSeverityLevel := Some(CheckstyleSeverityLevel.Error)

lazy val root = (project in file(".")).enablePlugins(PlayJava, BuildInfoPlugin).settings(
  buildInfoKeys := Seq[BuildInfoKey](name, version),
  buildInfoObject := "BriventoryBuildInfo",
  buildInfoPackage := "ch.varani.briventory"
)

// Dependency Injection
libraryDependencies += guice

// JPA Dependencies
libraryDependencies ++= Seq(
  javaJdbc,
  javaJpa,
  "org.hibernate" % "hibernate-core" % "5.4.18.Final"
)

// Libraries
libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-text" % "1.8",
  "commons-validator" % "commons-validator" % "1.6",
  "org.postgresql" % "postgresql" % "42.2.14",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.3",
  "org.semver" % "api" % "0.9.33",
  "me.gosimple" % "nbvcxz" % "1.4.3",
  "at.favre.lib" % "bcrypt" % "0.9.0"
)

// WebJars
libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.8.0",
  "org.webjars" % "jquery" % "3.5.1",
  "org.webjars" % "jquery-ui" % "1.12.1",
  "org.webjars" % "popper.js" % "2.0.2",
  "org.webjars" % "bootstrap" % "4.5.0",
  "org.webjars" % "font-awesome" % "5.13.0",
  "org.webjars.bowergithub.dropbox" % "zxcvbn" % "4.4.2"
)

// Tests libraries
resolvers += Resolver.jcenterRepo
libraryDependencies ++= Seq(
  "org.assertj" % "assertj-core" % "3.16.1" % Test,
  "org.awaitility" % "awaitility" % "4.0.3" % Test,
  "net.aichler" % "jupiter-interface" % "0.8.3" % Test,
  "org.junit.jupiter" % "junit-jupiter-api" % "5.6.2" % Test,
  "org.junit.jupiter" % "junit-jupiter-engine" % "5.6.2" % Test,
  "org.junit.jupiter" % "junit-jupiter-params" % "5.6.2" % Test,
  "org.junit.platform" % "junit-platform-runner" % "1.6.2" % Test
)

// Dependencies Check Directives
dependencyUpdatesFailBuild := true
dependencyUpdatesFilter -= moduleFilter(organization = "org.jacoco", name = "org.jacoco.agent")
dependencyUpdatesFilter -= moduleFilter(organization = "com.fasterxml.jackson.core", name = "jackson-databind")

// SonarQube parameters
sonarProperties ++= Map(
  "sonar.projectName" -> System.getenv("CI_PROJECT_NAME"),
  "sonar.host.url" -> System.getenv("SONAR_HOST"),
  "sonar.java.source" -> "11",
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

addCommandAlias("pipeline", ";clean;dependencyUpdates;checkstyle;compile;test")
