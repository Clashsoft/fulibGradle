package de.clashsoft.gentreesrc.gradle

import groovy.io.FileType
import groovy.transform.CompileStatic
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static org.gradle.testkit.runner.TaskOutcome.SUCCESS

class FunctionalTest extends Specification {
	static String[] TEST_FILES = [
			'build.gradle',
			'settings.gradle',
			'src/gen/java/com/example/CarModel.java',
			'src/gen/java/com/example2/PlaneModel.java',
			'src/main/scenarios/com/example/Foo.md',
			'src/test/scenarios/org/example/Baz.md',
	]

	@TempDir
	File testProjectDir

	@CompileStatic
	void setup() {
		final Path rootPath = testProjectDir.toPath()
		for (final String fileName : TEST_FILES) {
			final Path source = Paths.get("src/functionalTest/testfiles", fileName)
			final Path target = rootPath.resolve(fileName)

			Files.createDirectories(target.parent)

			try {
				Files.createLink(target, source)
			}
			catch (Exception ignored) {
				Files.copy(source, target)
			}
		}
	}

	@CompileStatic
	BuildResult run(String... args) {
		try {
			BuildResult result = GradleRunner.create()
					.withProjectDir(testProjectDir)
					.withArguments(args)
					.withPluginClasspath()
					.build()

			println "-" * 30 + " Gradle Output " + "-" * 30
			println result.output
			println "-" * 30 + " Project Files " + "-" * 30
			return result
		}
		finally {
			testProjectDir.eachFileRecurse(FileType.FILES) {
				println it
			}
			println "-" * 75
		}
	}

	def generatesClasses() {
		when:
		def result = run('check')

		then:
		result.task(":check").outcome == SUCCESS

		def mainOutputDir = new File(testProjectDir, 'src/main/java/')
		new File(mainOutputDir, 'com/example/Car.java').exists()
		new File(mainOutputDir, 'com/example/SuperCar.java').exists()
		new File(mainOutputDir, 'com/example2/Plane.java').exists()

		def testOutputDir = new File(testProjectDir, 'src/test/java/')
		new File(testOutputDir, 'com/example/FooTest.java').exists()
		new File(testOutputDir, 'org/example/BazTest.java').exists()
		new File(testOutputDir, 'org/example/City.java').exists()

		def srcDir = new File(testProjectDir, 'src/main/scenarios')
		new File(srcDir, 'com/example/example.html.png').exists()
		new File(srcDir, 'com/example/example.html').exists()
	}

	def generatesClassesWithGeneratedSource() {
		when:
		def result = run('check', '-PuseGeneratedSourceDirectories')

		then:
		result.task(":check").outcome == SUCCESS

		def mainOutputDir = new File(testProjectDir, 'build/generated/sources/scenarios/main/')
		new File(mainOutputDir, 'com/example/Car.java').exists()
		new File(mainOutputDir, 'com/example/SuperCar.java').exists()
		new File(mainOutputDir, 'com/example2/Plane.java').exists()

		def testOutputDir = new File(testProjectDir, 'build/generated/sources/scenarios/test/')
		new File(testOutputDir, 'com/example/FooTest.java').exists()
		new File(testOutputDir, 'org/example/BazTest.java').exists()
		new File(testOutputDir, 'org/example/City.java').exists()

		def srcDir = new File(testProjectDir, 'src/main/scenarios')
		new File(srcDir, 'com/example/example.html.png').exists()
		new File(srcDir, 'com/example/example.html').exists()
	}
}
