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
    id 'org.fulib.fulibGradle' version '0.5.0'
    // ...
}
```

## Usage

The [fulibScenarios README](https://github.com/fujaba/fulibScenarios/blob/master/README.md) describes how to work with scenarios.
Check out the [fulib README](https://github.com/fujaba/fulib/blob/master/README.md) to learn how to generate model code.

This plugin supports the following options for `ScenariosTask`s.
The default values are shown.

```gradle
generateScenarioSource { // or generateTestScenarioSource, or custom task
	tables = false // generates table class using fulibTables (https://github.com/fujaba/fulibTables)

	classDiagram = false // generates a classDiagram.png alongside the model java files
	classDiagramSVG = false // generates a classDiagram.svg alongside the model java files

	objectDiagram = false // scenario tests automatically create object diagrams (png) at the end
	objectDiagramSVG = false // see objectDiagram, but uses svg

	dryRun = false // does not change any files on disk, only outputs compilation problems and warnings

	extraArgs = [] // extra arguments to pass to the scenario compiler, can also be used like `extraArgs += ...`. See https://github.com/fujaba/fulibScenarios
}
```

## License

[MIT](LICENSE.md)
