package ch.silviowangler.gradle.i18n

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

/**
 * @author Silvio Wangler
 */
class ResourceBundlePluginSpec extends Specification {

    Project project = ProjectBuilder.builder().build()

    @Rule
    TemporaryFolder temporaryFolder

    void setup() {
        project.apply plugin: 'ch.silviowangler.gradle.i18n'
    }

    void "Das Plugin stellt einen Task zur Verfügung"() {

        expect:
        project.tasks.generateResourceBundle instanceof ResourceBundleTask
    }
}
