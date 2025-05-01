# Jewel IJP 251+ plugin template

This plugin shows how to build an IntelliJ Platform using the platform-provided Jewel and Compose dependencies.
Since Jewel and Compose have been added to the IJP in the 2025.1 version, if you plan on targeting earlier versions of
IJP, you'll need to bring in the same versions of Jewel and Compose manually for those other versions.

In a nutshell, on top of a basic IJ plugin setup, you need:

1. Kotlin and Compose Compiler Gradle plugins (Java not needed)
2. The `google()` and Jewel dev Maven repo in your `repositories`
3. Dependencies:
    * For 251+: the Compose, Skiko, and Jewel `bundledModule`s in your
      [`dependencies.intellijPlatform`](plugin-251/build.gradle.kts) block, and
      the same dependencies in your [`plugin.xml`](plugin-251/src/main/resources/META-INF/plugin.xml)) as modules
    * For 243: the Jewel dependency in your [`dependencies`](plugin-243/build.gradle.kts) block
4. For 251, make sure you're matching the Jewel and Compose versions used in the target IJP version
5. Put common code in a `core` module, and include the core `plugin.xml` into each leaf module
    * You may want to have interfaces in `core` for specific parts that have different implementations/behaviors for
      different IJP versions
    * If you do, make sure you register them as EPs so you can resolve their implementations at runtime
6. Some piece of code that contains Compose/Jewel UI to test
    * In this example plugin, you can use the _Jewel Demo Dialog_ action (you'll need to use Search Action to find it)

## On multi-module setups (251+)

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

Note that it's important to match the Jewel and Compose versions used by the target IJP version(s) to avoid problems.
While they should be forwards compatible, caution is never a bad thing.

Also, in the `commonUiModule` you MUST NOT use any leaf Jewel dependency (bridge or standalone) to avoid
incompatibilities. Only use dependencies up to the `ui` layer of Jewel (and `core` for its Markdown renderer).

You'll need to use the leaf-level (bridge or standalone) dependencies in the leaf modules — `intelliJPlugin` (provided
by the IJP) and `otherFrontend`.

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
