package org.fulib.gradle

import groovy.transform.CompileStatic
import org.fulib.gradle.internal.ScenariosVirtualDirectoryImpl
import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencyResolveDetails
import org.gradle.api.internal.plugins.DslObject
import org.gradle.api.internal.tasks.DefaultSourceSet
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer

@CompileStatic
class FulibGradlePlugin implements Plugin<Project> {

	public static final String CONFIGURATION_NAME = 'fulibScenarios'
	public static final String MAIN_CLASS_NAME = 'org.fulib.scenarios.Main'
	public static final String SRC_DIR_NAME = ScenariosVirtualDirectory.NAME
	public static final String TASK_VERB = "generate"
	public static final String TASK_TARGET = "ScenarioSource"

	@Override
	void apply(Project project) {
		project.pluginManager.apply(JavaPlugin)

		// configuration
		project.configurations.register(CONFIGURATION_NAME, { Configuration it ->
			it.description = 'The Fulib Scenarios libraries to use for this project.'
			it.visible = false
			it.resolutionStrategy.eachDependency { DependencyResolveDetails details ->
				final String version = details.requested.version
				if (version == null) {
					details.useVersion('+')
					details.because('latest version')
				}
			}
		} as Action<Configuration>)

		final SourceSetContainer sourceSets = project.convention.getPlugin(JavaPluginConvention).sourceSets
		final SourceSet main = sourceSets.getByName('main')
		final SourceSet test = sourceSets.getByName('test')
		configureSourceSet(project, main, test)
		configureSourceSet(project, test, test)
	}

	static void configureSourceSet(Project project, SourceSet main, SourceSet test) {
		final File srcDir = project.file("src/$main.name/$SRC_DIR_NAME")
		final String taskName = main.getTaskName(TASK_VERB, TASK_TARGET)
		final File modelDir = project.file("src/$main.name/java")
		final File testDir = project.file("src/$test.name/java")

		// for each source set we will:
		// 1) Add a new 'scenarios' virtual directory mapping

		final ScenariosVirtualDirectoryImpl directoryDelegate = new ScenariosVirtualDirectoryImpl((
				(DefaultSourceSet) main).displayName, project.objects)

		new DslObject(main).convention.plugins[SRC_DIR_NAME] = directoryDelegate

		directoryDelegate.scenarios.srcDir(srcDir)

		main.allSource.source(directoryDelegate.scenarios)

		// 2) create a task for this sourceSet following the gradle naming conventions

		project.tasks.register(taskName, ScenariosTask, { ScenariosTask it ->
			it.description = "Compiles the $main.name Scenario files."

			// 4) set up convention mapping for default sources (allows user to not have to specify)
			it.modelDirectory = modelDir
			it.testDirectory = testDir
			it.inputDirectory = srcDir
			it.toolClasspath = project.configurations.getByName(CONFIGURATION_NAME)
		} as Action<ScenariosTask>)

		// 3) Set up the scenarios output directory (adding to javac inputs!)
		// main.java.srcDir(modelDir)
		// test.java.srcDir(testDir)

		// 4) register fact that scenarioc should be run before compiling
		project.tasks.named(main.compileJavaTaskName) { Task it ->
			it.dependsOn taskName
		}

		project.tasks.named(test.compileJavaTaskName) { Task it ->
			it.dependsOn taskName
		}
	}
}
