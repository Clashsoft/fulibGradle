package org.fulib.gradle;

import org.gradle.api.DefaultTask;
import org.gradle.api.file.FileCollection;
import org.gradle.api.tasks.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.gradle.util.RelativePathUtil.relativePath;

public class ScenariosTask extends DefaultTask
{
	// =============== Fields ===============

	private FileCollection toolClasspath;

	private File modelDirectory;
	private File testDirectory;
	private File inputDirectory;

	private List<String> extraArgs = new ArrayList<>();

	// =============== Properties ===============

	@Classpath
	public FileCollection getToolClasspath()
	{
		return this.toolClasspath;
	}

	public void setToolClasspath(FileCollection toolClasspath)
	{
		this.toolClasspath = toolClasspath;
	}

	@OutputDirectory
	public File getModelDirectory()
	{
		return this.modelDirectory;
	}

	public void setModelDirectory(File modelDirectory)
	{
		this.modelDirectory = modelDirectory;
	}

	@OutputDirectory
	public File getTestDirectory()
	{
		return this.testDirectory;
	}

	public void setTestDirectory(File testDirectory)
	{
		this.testDirectory = testDirectory;
	}

	@InputDirectory
	@SkipWhenEmpty
	public File getInputDirectory()
	{
		return this.inputDirectory;
	}

	public void setInputDirectory(File inputDirectory)
	{
		this.inputDirectory = inputDirectory;
	}

	// --------------- Options ---------------

	// --------------- Extra Args ---------------

	@Input
	public List<String> getExtraArgs()
	{
		return this.extraArgs;
	}

	public void setExtraArgs(List<String> extraArgs)
	{
		Objects.requireNonNull(extraArgs);
		this.extraArgs = extraArgs;
	}

	public void extraArgs(Object... extraArgs)
	{
		for (final Object extraArg : extraArgs)
		{
			this.extraArgs.add(extraArg.toString());
		}
	}

	public void extraArgs(Iterable<?> extraArgs)
	{
		for (final Object extraArg : extraArgs)
		{
			this.extraArgs.add(extraArg.toString());
		}
	}

	// =============== Methods ===============

	@TaskAction
	public void execute()
	{
		this.getProject().javaexec(spec -> {
			spec.setClasspath(this.getToolClasspath());
			spec.setMain(FulibGradlePlugin.MAIN_CLASS_NAME);

			final List<String> args = new ArrayList<>(this.getExtraArgs());

			args.add("-m");

			args.add(relativePath(spec.getWorkingDir(), this.getModelDirectory()));
			args.add("-t");
			args.add(relativePath(spec.getWorkingDir(), this.getTestDirectory()));
			args.add("--");
			args.add(relativePath(spec.getWorkingDir(), this.getInputDirectory()));

			spec.setArgs(args);
		});
	}
}
