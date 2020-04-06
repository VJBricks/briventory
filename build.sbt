import scala.util.Failure

name := """Briventory"""
organization := "ch.varani"

version := "1.0.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, BuildInfoPlugin).settings(
  buildInfoKeys := Seq[BuildInfoKey](name, version),
  buildInfoObject := "BriventoryBuildInfo",
  buildInfoPackage := "ch.varani.briventory"
)

scalaVersion := "2.13.1"

// Disable the ScalaDoc generation
sources in (Compile,doc) := Seq.empty
publishArtifact in (Compile, packageDoc) := false

libraryDependencies ++= Seq(
  guice,
  javaJdbc
)

// Libraries
libraryDependencies ++= Seq(
  "org.apache.commons" % "commons-text" % "1.8",
  "commons-validator" % "commons-validator" % "1.6",
  "org.postgresql" % "postgresql" % "42.2.12",
  "org.jooq" % "jooq" % "3.13.1",
  "org.jooq" % "jooq-codegen" % "3.13.1",
  "org.jooq" % "jooq-meta" % "3.13.1",
  "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.3",
  "org.semver" % "api" % "0.9.33",
  "com.nulab-inc" % "zxcvbn" % "1.3.0"
)

// WebJars
libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.8.0",
  "org.webjars" % "jquery" % "3.4.1",
  "org.webjars" % "jquery-ui" % "1.12.1",
  "org.webjars" % "popper.js" % "2.0.2",
  "org.webjars" % "bootstrap" % "4.4.1",
  "org.webjars" % "font-awesome" % "5.13.0",
  "org.webjars.bowergithub.dropbox" % "zxcvbn" % "4.4.2"
)

// Tests libraries
resolvers += Resolver.jcenterRepo
libraryDependencies ++= Seq(
  "org.assertj" % "assertj-core" % "3.15.0" % Test,
  "org.awaitility" % "awaitility" % "4.0.2" % Test,
  "net.aichler" % "jupiter-interface" % "0.8.3" % Test,
  "org.junit.jupiter" % "junit-jupiter-api" % "5.6.1" % Test,
  "org.junit.jupiter" % "junit-jupiter-engine" % "5.6.1" % Test,
  "org.junit.jupiter" % "junit-jupiter-params" % "5.6.1" % Test,
  "org.junit.platform" % "junit-platform-runner" % "1.6.1" % Test
)

// Dependencies Check Directives
dependencyUpdatesFailBuild := true

dependencyUpdatesFilter -= moduleFilter(organization = "org.jacoco", name = "org.jacoco.agent")

// Checkstyle Directives
checkstyleConfigLocation := CheckstyleConfigLocation.File("varani_java_checks.xml")
checkstyleSeverityLevel := Some(CheckstyleSeverityLevel.Error)

// Make verbose tests
testOptions in Test := Seq(Tests.Argument(TestFrameworks.JUnit, "-a", "-v"))

// Treat warning as error
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
javacOptions ++= Seq("-Xlint:all", "-Xlint:-processing", "-Werror")
javaOptions ++= Seq("--illegal-access=warn")

// SonarQube parameters
sonarProperties ++= Map(
  "sonar.projectName"               -> System.getenv("CI_PROJECT_NAME"),
  "sonar.host.url"                  -> System.getenv("SONAR_HOST"),
  "sonar.java.source"               -> "11",
  "sonar.java.binaries"             -> "./target/scala-2.13/classes",
  "sonar.java.test.binaries"        -> "./target/scala-2.13/test-classes",
  "sonar.login"                     -> System.getenv("SONAR_TOKEN"),
  "sonar.gitlab.commit_sha"         -> System.getenv("CI_COMMIT_SHA"),
  "sonar.gitlab.ref_name"           -> System.getenv("CI_COMMIT_REF_NAME"),
  "sonar.gitlab.project_id"         -> System.getenv("CI_PROJECT_ID"),
  "sonar.gitlab.url"                -> System.getenv("GL_SERVER"),
  "sonar.gitlab.user_token"         -> System.getenv("GL_TOKEN"),
  "sonar.junit.reportsPath"         -> "./target/test-reports",
  "sonar.jacoco.reportPaths"        -> "./target/scala-2.13/jacoco/data/jacoco.exec"
)

val jooqDynamicCodegen = Def.taskDyn {
  val jooqFiles = ((sourceManaged.value / "main/jooq") ** "*.java").get
  if (jooqFiles.isEmpty)
    Def.task {
      val classpath = (managedClasspath in Compile).value.files
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

sourceGenerators in Compile += jooqCodegen

addCommandAlias("pipeline", ";dependencyUpdates;checkstyle;clean;compile")
