# Fulib Gradle v0.1.0

+ The plugin now adds the `fulibScenarios` configuration.
+ The plugin now adds the `generateScenarioSources` and `generateTestScenarioSources` tasks.
+ The plugin now adds the `src/main/scenarios` and `src/test/scenarios` source directories.

# Fulib Gradle v0.1.1

* The tool is now invoked with relative paths. #3

# Fulib Gradle v0.2.0

+ Added a custom `Task` subtype for Scenario tasks. #4
* Scenario tasks are no longer debuggable. #5

# Fulib Gradle v0.3.0

+ Added the `ScenariosTask.copyTo` methods that allow converting them to debuggable `JavaExec` tasks.
* The plugin is now implemented with Java instead of Groovy.
