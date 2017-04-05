package ch.silviowangler.gradle.i18n;

import groovy.json.StringEscapeUtils;

import org.gradle.api.DefaultTask;
import org.gradle.api.UnknownTaskException;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private List<Map<String, String>> propertiesStore = new ArrayList<>();
  private boolean native2ascii = false;

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

  public void setNative2ascii(boolean native2ascii) {
    this.native2ascii = native2ascii;
  }

  @TaskAction
  public void generateResourceBundle() throws IOException {

    // File einlesen
    List<String> lines = Files.readAllLines(this.csvFile.toPath(), Charset.forName(this.inputEncoding));

    // CSV Zeilen verarbeiten
    for (int i = 0; i < lines.size(); i++) {

      String line = lines.get(i);
      getLogger().debug("Processing line {}", line);
      String[] tokens = line.split(this.separator);

      if (i == 0) {
        processHeader(tokens);
      } else {
        processData(tokens);
      }
    }

    // Properties Dateien schreiben
    for (int i = 0; i < this.propertiesStore.size(); i++) {
      Map<String, String> properties = this.propertiesStore.get(i);
      File outputFile = new File(this.outputDir, this.bundleBaseName + "_" + this.languages.get(i) + ".properties");
      FileOutputStream outputStream = new FileOutputStream(outputFile);
      OutputStreamWriter writer = new OutputStreamWriter(outputStream, this.native2ascii ? "ASCII" : this.outputEncoding);


      properties.forEach( (key, value) -> {
        try {
          writer.append(key).append("=").append(value).append("\n");
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
      writer.flush();
      writer.close();
    }
  }

  private void processData(String[] tokens) throws UnsupportedEncodingException {

    String key = tokens[0];
    for (int i = 1; i < tokens.length; i++) {
      this.propertiesStore.get(i - 1).put(key, convertIfNecessary(tokens[i]));
    }
  }

  private String convertIfNecessary(String value) throws UnsupportedEncodingException {

    if (this.inputEncoding.equals(this.outputEncoding)) {
      return value;
    }

    String convertedValue;

    if (this.native2ascii) {
      convertedValue = StringEscapeUtils.escapeJava(value);
    } else {
      convertedValue = new String(value.getBytes(this.outputEncoding), this.outputEncoding);
    }

    if (convertedValue.indexOf('\uFFFD') != -1) {
      throw new UnknownTaskException("Troubles convert '" + value + "' (" + this.inputEncoding + ") to " + convertedValue + " (" + this
          .outputEncoding + ")");
    }

    getLogger().debug("Converted '{}' to '{}'", value, convertedValue);
    return convertedValue;
  }

  private void processHeader(String[] tokens) {
    for (int i = 1; i < tokens.length; i++) {
      String value = tokens[i];
      getLogger().info("Processing header cell with value " + value);
      this.languages.add(value);
      this.propertiesStore.add(new HashMap<>());
    }
  }
}
