# Fulib Gradle Plugin

[![Build Status](https://travis-ci.org/fujaba/fulibGradle.svg?branch=master)](https://travis-ci.org/fujaba/fulibGradle)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/org/fulib/fulibGradle/org.fulib.fulibGradle.gradle.plugin/maven-metadata.xml.svg?colorB=blue&label=Gradle%20Plugin%20Portal)](https://plugins.gradle.org/plugin/org.fulib.fulibGradle)

The Gradle plugin for the [Fulib Scenarios](https://github.com/fujaba/fulibScenarios) tool.

## Usage

The plugin is available on the [Gradle Plugin Portal](https://plugins.gradle.org/plugin/org.fulib.fulibGradle)
and can be installed via the `plugins` DSL in `build.gradle`:

```groovy
plugins {
    // ...
    id 'java'
    id 'org.fulib.fulibGradle' version '0.3.0'
    // ...
}
```

See the [Fulib Scenarios README](https://github.com/fujaba/fulibScenarios/blob/master/README.md)
for instructions on how to use the tool.
