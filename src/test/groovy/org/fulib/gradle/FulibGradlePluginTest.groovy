package org.fulib.gradle

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Before
import org.junit.Test

import static org.hamcrest.CoreMatchers.*
import static org.junit.Assert.assertThat

class FulibGradlePluginTest {
	private Project project

	@Before
	void setup() {
		project = ProjectBuilder.builder().withName('test').build()
		project.pluginManager.apply 'java'
		project.pluginManager.apply 'org.fulib.fulibGradle'
	}

	@Test
	void addsConfiguration() {
		assertThat(project.configurations.fulibScenarios, notNullValue())
	}

	@Test
	void addsSourceSets() {
		assertThat(project.sourceSets.main.scenarios, notNullValue())
		assertThat(project.sourceSets.test.scenarios, notNullValue())
	}

	@Test
	void addsTasks() {
		assertThat(project.tasks.generateScenarioSource, notNullValue())
		assertThat(project.tasks.generateTestScenarioSource, notNullValue())
	}

	@Test
	void addsTaskDependencies() {
		assertThat(project.tasks.compileJava.dependsOn, hasItem('generateScenarioSource'))
		assertThat(project.tasks.compileTestJava.dependsOn, hasItems('generateScenarioSource',
				'generateTestScenarioSource'))
	}
}
