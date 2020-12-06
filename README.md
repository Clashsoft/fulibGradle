# Fulib Gradle Plugin

[![Build Status](https://travis-ci.org/fujaba/fulibGradle.svg?branch=master)](https://travis-ci.org/fujaba/fulibGradle)
[![Java CI](https://github.com/fujaba/fulibTools/workflows/Java%20CI/badge.svg)](https://github.com/fujaba/fulibTools/actions)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/org/fulib/fulibGradle/org.fulib.fulibGradle.gradle.plugin/maven-metadata.xml.svg?colorB=blue&label=Gradle%20Plugin%20Portal)](https://plugins.gradle.org/plugin/org.fulib.fulibGradle)

The Gradle plugin for [fulib](https://github.com/fujaba/fulib) and the [fulibScenarios](https://github.com/fujaba/fulibScenarios) compiler.

## Installation

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

## Usage

The [fulibScenarios README](https://github.com/fujaba/fulibScenarios/blob/master/README.md) describes how to work with scenarios.
Check out the [fulib README](https://github.com/fujaba/fulib/blob/master/README.md) to learn how to generate model code.

## License

[MIT](LICENSE.md)
