package org.fulib.gradle;

import org.gradle.api.Project;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;

import java.io.File;

import static org.fulib.gradle.FulibGradlePlugin.TASK_TARGET;
import static org.fulib.gradle.FulibGradlePlugin.TASK_VERB;

public class FulibGradleExtension
{
	private final Project project;

	public FulibGradleExtension(Project project)
	{
		this.project = project;
	}

	public void useGeneratedSourceDirectories()
	{
		final File modelDir = new File(this.project.getBuildDir(), "generated/sources/scenarios/main");
		final File testDir = new File(this.project.getBuildDir(), "generated/sources/scenarios/test");

		final SourceSetContainer sourceSets = this.project
			.getConvention()
			.getPlugin(JavaPluginConvention.class)
			.getSourceSets();

		final SourceSet main = sourceSets.getByName("main");
		final SourceSet test = sourceSets.getByName("test");

		main.getJava().srcDir(modelDir);
		test.getJava().srcDir(testDir);

		this.project.getTasks().named(main.getTaskName(TASK_VERB, TASK_TARGET), ScenariosTask.class, task -> {
			task.setModelDirectory(modelDir);
			task.setTestDirectory(testDir);
		});

		this.project.getTasks().named(test.getTaskName(TASK_VERB, TASK_TARGET), ScenariosTask.class, task -> {
			task.setModelDirectory(testDir);
			task.setTestDirectory(testDir);
		});
	}
}
