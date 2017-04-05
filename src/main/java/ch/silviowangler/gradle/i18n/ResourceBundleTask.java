package ch.silviowangler.gradle.i18n;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Silvio Wangler
 */
public class ResourceBundleTask extends DefaultTask {

  private static final String ISO_8859_1 = "ISO-8859-1";

  @InputFile
  private File csvFile;
  @OutputDirectory
  private File outputDir;
  private String inputEncoding = ISO_8859_1;
  private String outputEncoding = ISO_8859_1;
  private String separator = ",";
  private String bundleBaseName = "messages";
  private List<String> languages = new ArrayList<>();
  private List<Properties> propertiesStore = new ArrayList<>();

  public void setCsvFile(File csvFile) {
    this.csvFile = csvFile;
  }

  public void setOutputDir(File outputDir) {
    this.outputDir = outputDir;
  }

  public void setInputEncoding(String inputEncoding) {
    this.inputEncoding = inputEncoding;
  }

  public void setOutputEncoding(String outputEncoding) {
    this.outputEncoding = outputEncoding;
  }

  public void setBundleBaseName(String bundleBaseName) {
    this.bundleBaseName = bundleBaseName;
  }

  public void setSeparator(String separator) {
    this.separator = separator;
  }

  @TaskAction
  public void generateResourceBundle() throws IOException {

    // File einlesen
    List<String> lines = Files.readAllLines(this.csvFile.toPath(), Charset.forName(this.inputEncoding));

    // CSV Zeilen verarbeiten
    for (int i = 0; i < lines.size(); i++) {

      String[] tokens = lines.get(i).split(this.separator);

      if (i == 0) {
        processHeader(tokens);
      } else {
        processData(tokens);
      }
    }

    // Properties Dateien schreiben
    for (int i = 0; i < this.propertiesStore.size(); i++) {
      Properties properties = this.propertiesStore.get(i);
      File outputFile = new File(this.outputDir, this.bundleBaseName + "_" + this.languages.get(i) + ".properties");
      FileOutputStream outputStream = new FileOutputStream(outputFile);
      OutputStreamWriter writer = new OutputStreamWriter(outputStream, this.outputEncoding);
      properties.store(writer, "");
    }
  }

  private void processData(String[] tokens) {

    String key = tokens[0];
    for (int i = 1; i < tokens.length; i++) {
      this.propertiesStore.get(i - 1).put(key, tokens[i]);
    }
  }

  private void processHeader(String[] tokens) {
    for (int i = 1; i < tokens.length; i++) {
      String value = tokens[i];
      getLogger().info("Processing header cell with value " + value);
      this.languages.add(value);
      this.propertiesStore.add(new Properties());
    }
  }
}
