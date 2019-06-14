package de.clashsoft.gentreesrc.gradle

import groovy.io.FileType
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import static org.gradle.testkit.runner.TaskOutcome.*

class FunctionalTest extends Specification {
	@Rule
	TemporaryFolder testProjectDir = new TemporaryFolder()

	def setup() {
		testProjectDir.newFile('settings.gradle') << /* language=Groovy */ """
		rootProject.name = 'test'
		"""

		testProjectDir.newFile('build.gradle') << /* language=Groovy */ """
		plugins {
			id 'java'
			id 'org.fulib.fulibGradle'
		}
		
		repositories {
			jcenter()
		}
		
		dependencies {
			fulibScenarios group: 'org.fulib', name: 'fulibScenarios', version: '0.3.2'
			
			// https://mvnrepository.com/artifact/junit/junit
			testCompile group: 'junit', name: 'junit', version: '4.12'
		}
		"""

		testProjectDir.newFolder('src', 'main', 'scenarios', 'com', 'example')
		testProjectDir.newFile('src/main/scenarios/com/example/Foo.md') << /* language=markdown */ """
		# Scenario Bar.
		
		There is a Car with name Herbie.
		"""

		testProjectDir.newFolder('src', 'test', 'scenarios', 'org', 'example')
		testProjectDir.newFile('src/test/scenarios/org/example/Baz.md') << /* language=markdown */ """
		# Scenario Boo.
		
		There is a City with name LA.
		"""
	}

	BuildResult run() {
		try {
			BuildResult result = GradleRunner.create()
					.withProjectDir(testProjectDir.root)
					.withArguments('check')
					.withPluginClasspath()
					.build()

			println "-" * 30 + " Gradle Output " + "-" * 30
			println result.output
			println "-" * 30 + " Project Files " + "-" * 30
			return result
		}
		finally {
			testProjectDir.root.eachFileRecurse(FileType.FILES) {
				println it
			}
			println "-" * 75
		}
	}

	def generatesClasses() {
		when:
		def result = run()

		then:
		result.task(":check").outcome == SUCCESS

		def mainOutputDir = new File(testProjectDir.root, "src/main/java/")
		new File(mainOutputDir, 'com/example/Car.java').exists()

		def testOutputDir = new File(testProjectDir.root, "src/test/java/")
		new File(testOutputDir, 'com/example/FooTest.java').exists()
		new File(testOutputDir, 'org/example/BazTest.java').exists()
		new File(testOutputDir, 'org/example/City.java').exists()
	}
}
