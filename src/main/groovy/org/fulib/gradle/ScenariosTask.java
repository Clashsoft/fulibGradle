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

	private FileCollection classpath;
	private List<String>   imports;

	private boolean classDiagram;
	private boolean classDiagramSVG;

	private boolean objectDiagram;
	private boolean objectDiagramSVG;

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

	@Classpath
	public FileCollection getClasspath()
	{
		return this.classpath;
	}

	public void setClasspath(FileCollection classpath)
	{
		this.classpath = classpath;
	}

	@Input
	public List<String> getImports()
	{
		return this.imports;
	}

	public void setImports(List<String> imports)
	{
		this.imports = imports;
	}

	public void imports(Object... imports)
	{
		for (final Object anImport : imports)
		{
			this.imports.add(anImport.toString());
		}
	}

	public void imports(Iterable<?> imports)
	{
		for (final Object anImport : imports)
		{
			this.imports.add(anImport.toString());
		}
	}

	@Input
	public boolean isClassDiagram()
	{
		return this.classDiagram;
	}

	public void setClassDiagram(boolean classDiagram)
	{
		this.classDiagram = classDiagram;
	}

	@Input
	public boolean isClassDiagramSVG()
	{
		return this.classDiagramSVG;
	}

	public void setClassDiagramSVG(boolean classDiagramSVG)
	{
		this.classDiagramSVG = classDiagramSVG;
	}

	@Input
	public boolean isObjectDiagram()
	{
		return this.objectDiagram;
	}

	public void setObjectDiagram(boolean objectDiagram)
	{
		this.objectDiagram = objectDiagram;
	}

	@Input
	public boolean isObjectDiagramSVG()
	{
		return this.objectDiagramSVG;
	}

	public void setObjectDiagramSVG(boolean objectDiagramSVG)
	{
		this.objectDiagramSVG = objectDiagramSVG;
	}

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

			if (!this.getClasspath().isEmpty())
			{
				args.add("--classpath");
				args.add(this.getClasspath().getAsPath());
			}
			if (!this.getImports().isEmpty())
			{
				args.add("--imports");
				args.add(String.join(",", this.getImports()));
			}
			if (this.isClassDiagram())
			{
				args.add("--class-diagram");
			}
			if (this.isClassDiagramSVG())
			{
				args.add("--class-diagram-svg");
			}
			if (this.isObjectDiagram())
			{
				args.add("--object-diagram");
			}
			if (this.isObjectDiagramSVG())
			{
				args.add("--object-diagram-svg");
			}

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
