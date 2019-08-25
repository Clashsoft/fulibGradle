package org.fulib.gradle;

import org.fulib.gradle.internal.ScenariosVirtualDirectoryImpl;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.internal.plugins.DslObject;
import org.gradle.api.internal.tasks.DefaultSourceSet;
import org.gradle.api.plugins.JavaPlugin;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

import java.io.File;

public class FulibGradlePlugin implements Plugin<Project>
{
	public static final String CONFIGURATION_NAME = "fulibScenarios";
	public static final String MAIN_CLASS_NAME    = "org.fulib.scenarios.Main";
	public static final String SRC_DIR_NAME       = ScenariosVirtualDirectory.NAME;
	public static final String TASK_VERB          = "generate";
	public static final String TASK_TARGET        = "ScenarioSource";

	@Override
	public void apply(Project project)
	{
		project.getPluginManager().apply(JavaPlugin.class);

		// configuration
		project.getConfigurations().register(CONFIGURATION_NAME, it -> {
			it.setDescription("The Fulib Scenarios libraries to use for this project.");
			it.setVisible(false);
			it.getResolutionStrategy().eachDependency(details -> {
				final String version = details.getRequested().getVersion();
				if (version == null)
				{
					details.useVersion("+");
					details.because("latest version");
				}
			});
		});

		final SourceSetContainer sourceSets = project.getConvention().getPlugin(JavaPluginConvention.class)
		                                             .getSourceSets();
		final SourceSet main = sourceSets.getByName("main");
		final SourceSet test = sourceSets.getByName("test");
		configureSourceSet(project, main, test);
		configureSourceSet(project, test, test);
	}

	static void configureSourceSet(Project project, SourceSet main, SourceSet test)
	{
		final File srcDir = project.file("src/" + main.getName() + "/" + SRC_DIR_NAME);
		final String taskName = main.getTaskName(TASK_VERB, TASK_TARGET);
		final File modelDir = project.file("src/" + main.getName() + "/java");
		final File testDir = project.file("src/" + test.getName() + "/java");

		// for each source set we will:
		// 1) Add a new 'scenarios' virtual directory mapping

		final ScenariosVirtualDirectoryImpl directoryDelegate = new ScenariosVirtualDirectoryImpl(
			((DefaultSourceSet) main).getDisplayName(), project.getObjects());

		new DslObject(main).getConvention().getPlugins().put(SRC_DIR_NAME, directoryDelegate);

		directoryDelegate.getScenarios().srcDir(srcDir);

		main.getAllSource().source(directoryDelegate.getScenarios());

		// 2) create a task for this sourceSet following the gradle naming conventions

		project.getTasks().register(taskName, ScenariosTask.class, it -> {
			it.setDescription("Compiles the " + main.getName() + " Scenario files.");

			// 3) set up convention mapping for default sources (allows user to not have to specify)
			it.setModelDirectory(modelDir);
			it.setTestDirectory(testDir);
			it.setInputDirectory(srcDir);
			it.setToolClasspath(project.getConfigurations().getByName(CONFIGURATION_NAME));
		});

		// 4) Set up the scenarios output directory (adding to javac inputs!)
		// main.java.srcDir(modelDir)
		// test.java.srcDir(testDir)

		// 5) register fact that scenarioc should be run before compiling
		project.getTasks().named(main.getCompileJavaTaskName(), it -> it.dependsOn(taskName));
		project.getTasks().named(test.getCompileJavaTaskName(), it -> it.dependsOn(taskName));
	}
}
