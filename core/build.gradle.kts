import java.util.*

plugins {
  id("ijp-plugin-base")
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.intelliJModule)
}

repositories {
  google()
  maven("https://packages.jetbrains.team/maven/p/kpm/public/")
  mavenCentral()

  intellijPlatform { defaultRepositories() }
}

val rootProperties = Properties()
rootProject.file("gradle.properties").inputStream().use { rootProperties.load(it) }

dependencies {
  testImplementation(libs.junit)

  intellijPlatform {
    create(
      rootProperties.getProperty("platformType"),
      rootProperties.getProperty("platformVersion"),
    )

    // Add dependency on Compose and Jewel modules
    bundledModule("intellij.platform.jewel.foundation")
    bundledModule("intellij.platform.jewel.ui")
    bundledModule("intellij.platform.jewel.ideLafBridge")
    bundledModule("intellij.libraries.compose.foundation.desktop")
    bundledModule("intellij.libraries.skiko")

    // Any other IJP dependencies can go here (e.g., zipSigner(), etc)
  }
}

tasks.test { useJUnitPlatform() }
