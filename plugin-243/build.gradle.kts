import java.util.*

plugins {
  id("ijp-plugin-base")
  alias(libs.plugins.composeCompiler)
  alias(libs.plugins.intelliJPlugin)
}

val pluginProperties = Properties()

project.file("plugin.properties").inputStream().use { pluginProperties.load(it) }

val rootProperties = Properties()

rootProject.file("gradle.properties").inputStream().use { rootProperties.load(it) }

version = "$version-${pluginProperties.getProperty("ijpTarget")}"

repositories {
  google()
  maven("https://packages.jetbrains.team/maven/p/kpm/public/")
  mavenCentral()

  intellijPlatform { defaultRepositories() }
}

dependencies {
  implementation(libs.jewel.ideLafBridge)
  implementation(projects.core)

  testImplementation(libs.junit)

  intellijPlatform {
    create(
      rootProperties.getProperty("platformType"),
      pluginProperties.getProperty("platformVersion"),
    )

    pluginVerifier()
    zipSigner()

    pluginModule(projects.core)
  }
}

intellijPlatform {
  pluginConfiguration {
    version = project.version.toString()

    ideaVersion {
      sinceBuild = pluginProperties.getProperty("pluginSinceBuild")
      untilBuild = pluginProperties.getProperty("pluginUntilBuild")
    }
  }

  signing {
    certificateChain = providers.environmentVariable("CERTIFICATE_CHAIN")
    privateKey = providers.environmentVariable("PRIVATE_KEY")
    password = providers.environmentVariable("PRIVATE_KEY_PASSWORD")
  }

  publishing { token = providers.environmentVariable("PUBLISH_TOKEN") }

  pluginVerification { ides { recommended() } }
}

tasks.test { useJUnitPlatform() }
