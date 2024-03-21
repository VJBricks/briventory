scalaVersion := "2.12.19"

// The Play plugin
addSbtPlugin("org.playframework" % "sbt-plugin" % "3.0.1")
addSbtPlugin("com.github.sbt" % "sbt-web" % "1.5.4")

// Retrieves sbt variables values
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.11.0")

// Test coverage
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.4.0")
dependencyOverrides += "org.jacoco" % "org.jacoco.agent" % "0.8.11"

// Tests using JUnit 5
addSbtPlugin("net.aichler" % "sbt-jupiter-interface" % "0.11.1")

// SonarQube Scanner
addSbtPlugin("com.sonar-scala" % "sbt-sonar" % "2.3.0")

// Dependencies check
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.6.4")

// Checkstyle Scanner
addSbtPlugin("com.etsy" % "sbt-checkstyle-plugin" % "3.1.1")
dependencyOverrides += "com.puppycrawl.tools" % "checkstyle" % "10.12.7"

// SASS and CSS plugins
addSbtPlugin("io.github.irundaia" % "sbt-sassify" % "1.5.2")

addSbtPlugin("com.github.sbt" % "sbt-jshint" % "2.0.1")

// Automatic Digest for Assets
addSbtPlugin("com.github.sbt" % "sbt-digest" % "2.0.0")