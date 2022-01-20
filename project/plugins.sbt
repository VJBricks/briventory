// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.13")

// Retrieves sbt variables values
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.10.0")

// Test coverage
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.4.0")
dependencyOverrides += "org.jacoco" % "org.jacoco.agent" % "0.8.7"

// Tests using JUnit 5
addSbtPlugin("net.aichler" % "sbt-jupiter-interface" % "0.9.1")

// SonarQube Scanner
addSbtPlugin("com.olaq" % "sbt-sonar-scanner-plugin" % "1.3.0")

// Dependencies check
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.6.0")

// Checkstyle Scanner
addSbtPlugin("com.etsy" % "sbt-checkstyle-plugin" % "3.1.1")
dependencyOverrides += "com.puppycrawl.tools" % "checkstyle" % "9.2"

// SASS and CSS plugins
val sassifyLocation: File = file("project/lib/sbt-sassify.jar").getAbsoluteFile
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.5.2-SNAPSHOT" from "file://" + sassifyLocation)

addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.6")

// Automatic Digest for Assets
addSbtPlugin("com.typesafe.sbt" % "sbt-digest" % "1.1.4")