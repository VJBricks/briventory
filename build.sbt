name := """Briventory"""
maintainer := "briventory@varani.ch"

// Common Settings
scalaVersion := "2.13.6"

organization := "ch.varani"
version := "1.0.0-SNAPSHOT"

// Disable the ScalaDoc generation
Compile / doc / sources := Seq.empty
Compile / packageDoc / publishArtifact := false

// Treat warning as error
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
javacOptions ++= Seq("-Xlint:all", "-Xlint:-processing", "-Werror")
javaOptions ++= Seq("--illegal-access=warn")

// Include public assets during tests
Test / unmanagedResourceDirectories += baseDirectory(_ / "target/web/public/test").value
Test / fork := false

// Make verbose tests
testOptions += Tests.Argument(jupiterTestFramework, "-v")

// Checkstyle Directives
checkstyleConfigLocation := CheckstyleConfigLocation.File("varani_java_checks.xml")
checkstyleSeverityLevel := Some(CheckstyleSeverityLevel.Error)

lazy val root = project.in(file(".")).enablePlugins(PlayJava, BuildInfoPlugin).settings(
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
  "org.hibernate" % "hibernate-core" % "5.5.6",

  /* Both dependencies below are necessary to have validator working. */
  "org.glassfish" % "javax.el" % "3.0.1-b12",
/*"org.hibernate" % "hibernate-validator" % "7.0.1.Final",*/
  "javax.validation" % "validation-api" % "2.0.1.Final",

  /*"javax.cache" % "cache-api" % "1.1.1",*/
  "org.hibernate" % "hibernate-jcache" % "5.5.5.Final",
  "org.ehcache" % "ehcache" % "3.9.5",
  "io.dropwizard.metrics" % "metrics-core" % "4.2.3"
)

// Libraries
libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-text" % "1.9",
  "commons-validator" % "commons-validator" % "1.7",
  "org.postgresql" % "postgresql" % "42.2.23",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.3",
  "org.semver" % "api" % "0.9.33",
  "me.gosimple" % "nbvcxz" % "1.5.0",
  "at.favre.lib" % "bcrypt" % "0.9.0"
)

// WebJars
libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.8.8",
  "org.webjars" % "jquery" % "3.6.0",
  "org.webjars" % "jquery-ui" % "1.12.1",
  "org.webjars" % "popper.js" % "2.9.3",
  "org.webjars" % "bootstrap" % "5.1.0",
  "org.webjars" % "font-awesome" % "5.15.3",
  "org.webjars.bowergithub.dropbox" % "zxcvbn" % "4.4.2"
)

// Tests libraries
resolvers += Resolver.jcenterRepo
libraryDependencies ++= Seq(
  "org.assertj" % "assertj-core" % "3.20.2" % Test,
  "org.awaitility" % "awaitility" % "4.1.0" % Test,
  "net.aichler" % "jupiter-interface" % JupiterKeys.jupiterVersion.value % Test,
  "org.junit.jupiter" % "junit-jupiter-api" % "5.7.2" % Test,
  "org.junit.jupiter" % "junit-jupiter-engine" % "5.7.2" % Test,
  "org.junit.jupiter" % "junit-jupiter-params" % "5.7.2" % Test,
  "org.junit.platform" % "junit-platform-runner" % "1.7.2" % Test
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

addCommandAlias("pipeline", ";clean;dependencyUpdates;checkstyle;compile;test;jacoco")
