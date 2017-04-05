package ch.silviowangler.gradle.i18n;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.File;

/**
 * @author Silvio Wangler
 * @since 0.1
 */
public class ResourceBundlePlugin implements Plugin<Project> {

  public static final String PLUGIN_ID = "ch.silviowangler.gradle.i18n";

  @Override
  public void apply(Project project) {

    project.getTasks().create("generateResourceBundle", ResourceBundleTask.class, (task) -> {
      task.setOutputDir(new File(System.getProperty("java.io.tmpdir")));
    });
  }
}