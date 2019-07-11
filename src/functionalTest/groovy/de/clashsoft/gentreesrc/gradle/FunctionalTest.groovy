package de.clashsoft.gentreesrc.gradle

import groovy.io.FileType
import groovy.transform.CompileStatic
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class FunctionalTest extends Specification {
	static String[] TEST_FILES = [
			'build.gradle',
			'settings.gradle',
			'src/main/scenarios/com/example/Foo.md',
			'src/test/scenarios/org/example/Baz.md',
	]

	@Rule
	TemporaryFolder testProjectDir = new TemporaryFolder()

	@CompileStatic
	void setup() {
		final Path rootPath = testProjectDir.root.toPath()
		for (final String fileName : TEST_FILES) {
			final Path source = Paths.get("src/functionalTest/testfiles", fileName)
			final Path target = rootPath.resolve(fileName)

			Files.createDirectories(target.parent)

			try {
				Files.createLink(target, source)
			}
			catch (UnsupportedOperationException ignored) {
				Files.copy(source, target)
			}
		}
	}

	@CompileStatic
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
