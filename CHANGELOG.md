# fulibGradle v0.1.0

+ The plugin now adds the `fulibScenarios` configuration.
+ The plugin now adds the `generateScenarioSources` and `generateTestScenarioSources` tasks.
+ The plugin now adds the `src/main/scenarios` and `src/test/scenarios` source directories.

# fulibGradle v0.1.1

* The tool is now invoked with relative paths. #3

# fulibGradle v0.2.0

+ Added a custom `Task` subtype for Scenario tasks. #4
* Scenario tasks are no longer debuggable. #5

# fulibGradle v0.3.0

+ Added the `ScenariosTask.copyTo` methods that allow converting them to debuggable `JavaExec` tasks.
* The plugin is now implemented with Java instead of Groovy.

# fulibGradle v0.4.0

## New Features

+ The plugin now creates the `gen` and `testGen` source sets.
+ Added support for Decorators with the `--decorator-classes` fulibScenarios compiler option. #7
  > It is not required to set the option manually.
  > The decorator classes are automatically determined from the source files in the `gen` and `testGen` source sets.
+ Added support for the `--tables` and `--dry-run` options of the fulibScenarios compiler. #8

## Known Issues

- The `generateScenarioSource` task fails if the `src/main/scenarios` directory does not exist.
  W/A: Create the directory (it can be empty).
- The `generateTestScenarioSource` task fails if the `src/test/scenarios` directory does not exist.
  W/A: Create the directory (it can be empty).
