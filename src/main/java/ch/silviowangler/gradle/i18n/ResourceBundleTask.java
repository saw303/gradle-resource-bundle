package ch.silviowangler.gradle.i18n;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.TaskAction;

import java.io.File;

/**
 * @author Silvio Wangler
 */
public class ResourceBundleTask extends DefaultTask {


  private File csvFile;
  private File outputDir;

  public void setCsvFile(File csvFile) {
    this.csvFile = csvFile;
  }

  public void setOutputDir(File outputDir) {
    this.outputDir = outputDir;
  }

  @TaskAction
  public void generateResourceBundle() {

  }
}
