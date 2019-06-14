package org.fulib.gradle.internal;

import groovy.lang.Closure;
import org.fulib.gradle.ScenariosVirtualDirectory;
import org.gradle.api.Action;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.reflect.HasPublicType;
import org.gradle.api.reflect.TypeOf;
import org.gradle.util.ConfigureUtil;

public class ScenariosVirtualDirectoryImpl implements ScenariosVirtualDirectory, HasPublicType
{
	private final SourceDirectorySet scenarios;

	public ScenariosVirtualDirectoryImpl(String parentDisplayName, ObjectFactory objectFactory)
	{
		final String name = parentDisplayName + ".scenarios";
		final String displayName = parentDisplayName + " scenarios source";
		this.scenarios = objectFactory.sourceDirectorySet(name, displayName);
		this.scenarios.getFilter().include("**/*.md");
	}

	@Override
	public SourceDirectorySet getScenarios()
	{
		return this.scenarios;
	}

	@Override
	public ScenariosVirtualDirectory scenarios(Closure configureClosure)
	{
		ConfigureUtil.configure(configureClosure, this.getScenarios());
		return this;
	}

	@Override
	public ScenariosVirtualDirectory scenarios(Action<? super SourceDirectorySet> configureAction)
	{
		configureAction.execute(this.getScenarios());
		return this;
	}

	@Override
	public TypeOf<?> getPublicType()
	{
		return TypeOf.typeOf(ScenariosVirtualDirectory.class);
	}
}
