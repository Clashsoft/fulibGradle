package org.fulib.gradle

import groovy.transform.CompileStatic
import org.fulib.gradle.internal.ScenariosVirtualDirectoryImpl
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencyResolveDetails
import org.gradle.api.internal.plugins.DslObject
import org.gradle.api.internal.tasks.DefaultSourceSet
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.JavaExec
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
		final Configuration configuration = project.configurations.create(CONFIGURATION_NAME)

		configuration.resolutionStrategy.eachDependency { DependencyResolveDetails details ->
			final String version = details.requested.version
			if (version == null) {
				details.useVersion('+')
				details.because('latest version')
			}
		}

		SourceSetContainer sourceSets = project.convention.getPlugin(JavaPluginConvention).sourceSets
		SourceSet main = sourceSets.getByName('main')
		SourceSet test = sourceSets.getByName('test')
		configureSourceSet(project, main, test)
		configureSourceSet(project, test, test)
	}

	static void configureSourceSet(Project project, SourceSet main, SourceSet test) {
		// for each source set we will:
		// 1) Add a new 'scenarios' virtual directory mapping

		final String srcDirName = "src/$main.name/$SRC_DIR_NAME"
		final File srcDir = project.file(srcDirName)

		final ScenariosVirtualDirectoryImpl directoryDelegate = new ScenariosVirtualDirectoryImpl((
				(DefaultSourceSet) main).displayName, project.objects)

		new DslObject(main).convention.plugins.put(SRC_DIR_NAME, directoryDelegate)

		directoryDelegate.scenarios.srcDir(srcDirName)

		main.allSource.source(directoryDelegate.scenarios)

		// 2) create a task for this sourceSet following the gradle naming conventions

		final String taskName = main.getTaskName(TASK_VERB, TASK_TARGET)
		final String modelDirName = "src/$main.name/java"
		final String testDirName = "src/$test.name/java"
		final File modelDir = project.file(modelDirName)
		final File testDir = project.file(testDirName)

		project.tasks.register(taskName, JavaExec) {
			configureTask(it, srcDir, modelDir, testDir)
		}

		// 3) Set up the scenarios output directory (adding to javac inputs!)
		// main.java.srcDir(modelDir)
		// test.java.srcDir(testDir)

		// 5) register fact that scenarioc should be run before compiling
		project.tasks.named(main.compileJavaTaskName) { Task it ->
			it.dependsOn taskName
		}

		project.tasks.named(test.compileJavaTaskName) { Task it ->
			it.dependsOn taskName
		}
	}

	static void configureTask(JavaExec it, File inputDir, File modelDir, File testDir) {
		final File workingDir = it.workingDir
		final String relInputDir = workingDir.relativePath(inputDir)
		final String relModelDir = workingDir.relativePath(modelDir)
		final String relTestDir = workingDir.relativePath(testDir)

		it.classpath = it.project.configurations.getByName(CONFIGURATION_NAME)
		it.main = MAIN_CLASS_NAME
		it.args = [ '-m', relModelDir, '-t', relTestDir, relInputDir ]

		it.inputs.dir(inputDir)
		it.outputs.dir(modelDir)

		it.onlyIf { inputDir.exists() }
	}
}
