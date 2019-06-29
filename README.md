# ksh
[ ![Download](https://api.bintray.com/packages/arturbosch/generic/ksh/images/download.svg) ](https://bintray.com/arturbosch/generic/ksh/_latestVersion)

A framework to build shell-like applications

![ksh](/img/ksh.png "Ksh in Action")

## Usage

ksh is published via bintray and needs my repository to get resolved by gradle.

```groovy
repositories {
    maven {
        url "https://dl.bintray.com/arturbosch/generic"
    } 
}

dependencies {
    compile 'io.gitlab.arturbosch:ksh-framework:[replace-version]'
}
```

Copy the `ksh-template` module for a ksh skeleton project.

## Bootstrap your shell

```kotlin
import io.gitlab.arturbosch.ksh.bootstrap

fun main(args: Array<String>) {
    bootstrap().runLooping()
}
```

## Write your first command and get tab completion for free

```kotlin
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.ShellMethod
import io.gitlab.arturbosch.ksh.api.ShellOption

class Hello : ShellClass {

    @ShellMethod(help = "Prints Hello (World|[Name]).")
    fun main(@ShellOption(["", "--name"]) name: String? = null) = "Hello ${name ?: "World"}"
}
```

Commands are bundled inside a provider which may wire up the commands with
additional dependencies via a dependency container.

```kotlin
import io.gitlab.arturbosch.ksh.api.ShellClass
import io.gitlab.arturbosch.ksh.api.provider.ShellClassesProvider
import io.gitlab.arturbosch.kutils.Container

class MyProvider : ShellClassesProvider {

    override fun provide(container: Container): List<ShellClass> {
        return listOf(Hello())
    }
}
```

Reference your provider implementations inside a file called `META-INF/services/io.gitlab.arturbosch.ksh.api.ShellClassesProvider` in your `main/resources` folder.

## Customize your prompt

Extend the `ShellSettings` class to customize the prompt. 
Write a ShellSettingsProvider which implementation is again referenced via a file this time called `META-INF/services/io.gitlab.arturbosch.ksh.api.ShellSettingsProvider`

```kotlin
import io.gitlab.arturbosch.ksh.api.ShellSettings
import io.gitlab.arturbosch.ksh.api.provider.ShellSettingsProvider
import io.gitlab.arturbosch.kutils.Container

class MySettings : ShellSettings {

    override val applicationName: String = APP_NAME
    override val historyFile: String = "~/.$APP_NAME/history"
    override fun prompt(): String = "$APP_NAME> "
}

const val APP_NAME = "app"

class MySettingsProvider : ShellSettingsProvider {

    override fun provide(container: Container): ShellSettings {
        return MySettings()
    }
}
```

## Dependency management inside your shell

As mentioned before `ksh` can manage your dependencies via a `Container`.
Create your own container and register dependencies before starting the `bootstrap` function:

```kotlin
fun main(args: Array<String>) {
    // parse arguments ...
    ...
    // create dependencies
    val container = DefaultContainer().apply {
        addSingletonFactory { ... }
        addSingleton( ... )
        addFactory { ... }
    }
    // start shell loop
    bootstrap(container).runLooping()
}
```

## Quick run commands and exit shell

The `Bootstrap` class has three different running modi:
- runLooping()
- runOnce(line: String)
- runFile(path: Path)

To support single command evaluation or execution of ksh commands inside a file
you could parse the application arguments as follow:

```kotlin
fun main(args: Array<String>) {
    val bootstrap = bootstrap(/* initContainer() */)
    when {
        args.isEmpty() -> bootstrap.runLooping()
        args.size == 1 && path(args[0]).exists() -> bootstrap.runFile(path(args[0]))
        else -> bootstrap.runOnce(args.joinToString(" "))
    }
}
```

## Pitfalls

ksh uses heavily the ServiceLoader-Pattern, so make sure to merge all services files. With the shadow plugin you would add to your build.gradle file:

```gradle
shadowJar {
    mergeServiceFiles()
}
``` 
