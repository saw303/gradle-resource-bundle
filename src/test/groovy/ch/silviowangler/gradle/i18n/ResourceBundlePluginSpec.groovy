package ch.silviowangler.gradle.i18n

import groovy.io.FileType
import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Stepwise

import static ch.silviowangler.gradle.i18n.ResourceBundlePlugin.PLUGIN_ID

/**
 * @author Silvio Wangler
 */
@Stepwise
class ResourceBundlePluginSpec extends Specification {

    Project project = ProjectBuilder.builder().build()

    @Rule
    TemporaryFolder temporaryFolder

    void setup() {
        project.apply plugin: PLUGIN_ID
    }

    void "The plugins provides a task"() {

        expect:
        project.tasks.generateResourceBundle instanceof ResourceBundleTask
    }

    void "Generate ISO-8859-1 resource bundle"() {

        given:
        ResourceBundleTask task = project.tasks.generateResourceBundle

        and:
        task.csvFile = new File('src/test/resources/eka-backend.csv')
        task.outputDir = temporaryFolder.root

        and:
        def props = []

        when:
        task.execute()

        temporaryFolder.root.eachFile(FileType.FILES, { file ->
            props << file
        })

        then:
        props.size() == 5

        and:
        props.find { it.name == 'messages_de.properties' }
        props.find { it.name == 'messages_de_CH.properties' }
        props.find { it.name == 'messages_en.properties' }
        props.find { it.name == 'messages_fr.properties' }
        props.find { it.name == 'messages_it.properties' }
    }
}
