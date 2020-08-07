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
	void addsExtension() {
		assertThat(project.fulibGradle, instanceOf(FulibGradleExtension))
	}

	@Test
	void useGeneratedSourceDirectories() {
		project.fulibGradle.useGeneratedSourceDirectories()
		def mainDir = project.file("$project.buildDir/generated/sources/scenarios/main")
		def testDir = project.file("$project.buildDir/generated/sources/scenarios/test")
		assertThat(project.sourceSets.main.java.srcDirs, hasItem(mainDir))
		assertThat(project.sourceSets.test.java.srcDirs, hasItem(testDir))
		assertThat(project.tasks.generateScenarioSource.modelDirectory, equalTo(mainDir))
		assertThat(project.tasks.generateScenarioSource.testDirectory, equalTo(testDir))
		assertThat(project.tasks.generateTestScenarioSource.modelDirectory, equalTo(testDir))
		assertThat(project.tasks.generateTestScenarioSource.testDirectory, equalTo(testDir))
	}

	@Test
	void addsConfiguration() {
		assertThat(project.configurations.fulibScenarios, notNullValue())
		assertThat(project.configurations.genImplementation, notNullValue())
		assertThat(project.configurations.genImplementation.extendsFrom, hasItem(project.configurations
				.fulibScenarios))
		assertThat(project.configurations.testGenImplementation, notNullValue())
		assertThat(project.configurations.testGenImplementation.extendsFrom, hasItem(project.configurations
				.fulibScenarios))
	}

	@Test
	void addsSourceSets() {
		assertThat(project.sourceSets.main.scenarios, notNullValue())
		assertThat(project.sourceSets.test.scenarios, notNullValue())
		assertThat(project.sourceSets.gen, notNullValue())
		assertThat(project.sourceSets.testGen, notNullValue())
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
