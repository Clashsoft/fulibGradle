package org.fulib.gradle;

import groovy.lang.Closure;
import org.gradle.api.Action;
import org.gradle.api.file.SourceDirectorySet;

public interface ScenariosVirtualDirectory
{
	String NAME = "scenarios";

	SourceDirectorySet getScenarios();

	ScenariosVirtualDirectory scenarios(Closure configureClosure);

	ScenariosVirtualDirectory scenarios(Action<? super SourceDirectorySet> configureAction);
}
