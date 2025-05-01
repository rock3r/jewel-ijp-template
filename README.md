# Jewel IJP 251+ plugin template

> [!NOTE]
> If you want to also target 243 at the same time, you can look at the solution in [this branch](https://github.com/rock3r/jewel-ijp-template/tree/243-and-251-compat)
             
This plugin shows how to build an IntelliJ Platform Plugin using the platform-provided Jewel and Compose dependencies.
Since Jewel and Compose have been added to the IJP in the 2025.1 version, if you plan on targeting earlier versions of
IJP, you'll need to bring in the same versions of Jewel and Compose manually for those other versions.

In a nutshell, on top of a basic IJ plugin setup, you need:
1. Kotlin, Compose Compiler, and Compose Multiplatform Gradle plugins (Java not needed)
2. The `google()` and Jewel dev Maven repo in your [`repositories`](build.gradle.kts)
3. The Compose, Skiko, and Jewel `bundledModule`s in your [`dependencies.intellijPlatform`](build.gradle.kts)
4. The same dependencies in your [`plugin.xml`](src/main/resources/META-INF/plugin.xml)) as modules
5. Making sure you're matching the Jewel and Compose versions used in the target IJP version (the easiest way to find
   those out is by looking [here](https://github.com/JetBrains/intellij-community/blob/master/platform/jewel/gradle/libs.versions.toml))
6. Some piece of code that contains Compose/Jewel UI 

## On multi-module setups
If you have multiple modules, where the plugin is a leaf node using the Compose UI coming from another module, you have
two options.

### 1. Make the parent node's dependencies compileOnly
If you have something like this:

```
commonUiModule/
├── intelliJPlugin
└── otherFrontend
```

Then you can make the Compose and Jewel dependencies in commonUiModule `compileOnly`, and then you can use the setup
showcased by this repo for the IJP plugin. You'd then also need to add `implementation` dependencies for the other
frontend module(s). Assuming the other frontend is _standalone_, you'll want to use the Jewel Standalone dependencies
there.

Note that's important to match the Jewel and Compose versions used by the target IJP version(s) to avoid problems. While
they should be forwards compatible, caution is never a bad thing. Also, in the `commonUiModule` you MUST NOT use any
leaf Jewel dependency (bridge or standalone) to avoid incompatibilities. Only use dependencies up to the `ui` layer of
Jewel (and `core` for its Markdown renderer). You'll need to use the leaf-level (bridge or standalone) dependencies in
the leaf modules — `intelliJPlugin` (provided by the IJP) and `otherFrontend`.

### 2. Exclude the parent's dependencies in the plugin module
If you have `implementation` and/or `api`-level dependencies in your ui module that other modules depend on, and can't
get rid of them, the best (or should we say, least painful) approach is to exclude all the transitive dependencies in
your plugin's module:

```kotlin
dependencies {
    //...

    implementation(projects.commonUiModule) {
        // Excluding dependencies that the IJP 251+ provides already
        exclude("androidx.annotation")
        exclude("androidx.arch.core")
        exclude("androidx.compose")
        exclude("androidx.lifecycle")
        exclude("org.jetbrains.compose")
        exclude("org.jetbrains.compose.foundation")
        exclude("org.jetbrains.compose.runtime")
        exclude("org.jetbrains.compose.ui")
        exclude("org.jetbrains.jewel")
        exclude("org.jetbrains.kotlin")
        exclude("org.jetbrains.kotlinx")
        exclude("org.jetbrains.skiko")
    }
}
```

---
Plugin based on the [IntelliJ Platform Plugin Template][template], minus the unnecessary stuff, to keep it small.

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
