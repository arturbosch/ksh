# ksh
[ ![Download](https://api.bintray.com/packages/arturbosch/generic/ksh/images/download.svg) ](https://bintray.com/arturbosch/generic/ksh/_latestVersion)

A commandline framework to build shell-like applications

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

## Bootstrap your shell

```kotlin
import io.gitlab.arturbosch.ksh.bootstrap

fun main(args: Array<String>) {
    bootstrap()
}
```

## Write your first command and get tab completion for free

```kotlin
class Test : ShellClass {

    @ShellMethod(help = "Prints Hello World.")
    fun main() = "Hello World"
}
```

Reference your commands in with the full qualified name in a file called `META-INF/services/io.gitlab.arturbosch.ksh.api.ShellClass` in your `main/resources` folder.

## Customize your prompt

Extend the `ShellSettings` class and place its reference in `META-INF/services/io.gitlab.arturbosch.ksh.api.ShellSettings`

```kotlin
class MySettings : ShellSettings {

    override val applicationName: String = APP_NAME
    override val historyFile: String = "~/.$APP_NAME/history"
    override fun prompt(): String = "$APP_NAME> "
}

const val APP_NAME = "app"
```
