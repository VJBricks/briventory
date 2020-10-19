// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.2")

// Retrieves sbt variables values
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.10.0")

// Test coverage
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.2.0")
dependencyOverrides += "org.jacoco" % "org.jacoco.agent" % "0.8.6"

// SonarQube Scanner
addSbtPlugin("com.olaq" % "sbt-sonar-scanner-plugin" % "1.3.0")

// Dependencies check
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.5.1")

// Checkstyle Scanner
addSbtPlugin("com.etsy" % "sbt-checkstyle-plugin" % "3.1.1")
dependencyOverrides += "com.puppycrawl.tools" % "checkstyle" % "8.36.1"

// SASS and CSS plugins
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.5.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.6")
