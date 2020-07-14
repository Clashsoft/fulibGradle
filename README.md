# Fulib Gradle Plugin

[![Build Status](https://travis-ci.org/fujaba/fulibGradle.svg?branch=master)](https://travis-ci.org/fujaba/fulibGradle)
[![Java CI](https://github.com/fujaba/fulibTools/workflows/Java%20CI/badge.svg)](https://github.com/fujaba/fulibTools/actions)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/org/fulib/fulibGradle/org.fulib.fulibGradle.gradle.plugin/maven-metadata.xml.svg?colorB=blue&label=Gradle%20Plugin%20Portal)](https://plugins.gradle.org/plugin/org.fulib.fulibGradle)

The Gradle plugin for the [Fulib Scenarios](https://github.com/fujaba/fulibScenarios) tool.

## Usage

The plugin is available on the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/org.fulib.fulibGradle)
and can be installed via the `plugins` DSL in `build.gradle`:

```groovy
plugins {
    // ...
    id 'java'
    id 'org.fulib.fulibGradle' version '0.4.0'
    // ...
}
```

See the [Fulib Scenarios README](https://github.com/fujaba/fulibScenarios/blob/master/README.md)
for instructions on how to use the tool.

> ⚠︎ In v0.4.0, there is a known issue that causes the build to fail
> if the `src/main/scenarios` and `src/test/scenarios` directories do not exist.
> The workaround is to create these directories.
> You can leave them empty, but it is advisable to create an empty `.gitkeep` file within them.
> This also avoids build failures on CI/CD systems like Travis CI.
