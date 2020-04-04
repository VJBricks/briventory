// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-play-enhancer" % "1.2.2")

// Retrieves sbt variables values
addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.9.0")

// Test coverage
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.1.0")
dependencyOverrides += "org.jacoco" % "org.jacoco.agent" % "0.8.3"

// SonarQube Scanner
addSbtPlugin("com.olaq" % "sbt-sonar-scanner-plugin" % "1.3.0")

// Dependencies check
addSbtPlugin("com.timushev.sbt" % "sbt-updates" % "0.4.0")

// Checkstyle Scanner
addSbtPlugin("com.etsy" % "sbt-checkstyle-plugin" % "3.1.1")
dependencyOverrides += "com.puppycrawl.tools" % "checkstyle" % "8.30"

// SASS and CSS plugins
addSbtPlugin("org.irundaia.sbt" % "sbt-sassify" % "1.4.12")
addSbtPlugin("com.typesafe.sbt" % "sbt-jshint" % "1.0.6")