/**
 * Copyright 2016 - 2017 Silvio Wangler (silvio.wangler@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.silviowangler.i18n;

import static ch.silviowangler.i18n.Consts.ISO_8859_1;

import groovy.json.StringEscapeUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class ResourceBundler {

  private File csvFile;
  private File outputDir;
  private String inputEncoding = ISO_8859_1;
  private String outputEncoding = ISO_8859_1;
  private String separator = ",";
  private String bundleBaseName = "messages";
  private List<String> languages = new ArrayList<>();
  private List<Map<String, String>> propertiesStore = new ArrayList<>();
  private boolean native2ascii = false;

  private static final Logger LOGGER = LogManager.getLogger(ResourceBundler.class);


  public void generateResourceBundle() throws IOException {

    // File einlesen
    List<String> lines = Files.readAllLines(this.csvFile.toPath(), Charset.forName(this.inputEncoding));

    // CSV Zeilen verarbeiten
    for (int i = 0; i < lines.size(); i++) {

      String line = lines.get(i);
      LOGGER.debug("Processing line {}", line);
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

      try (OutputStreamWriter writer = new OutputStreamWriter(outputStream, this.native2ascii ? Consts.ASCII : this.outputEncoding)) {
        properties.forEach((key, value) -> {
          try {
            writer.append(key).append("=").append(value).append("\n");
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
        writer.flush();
      }
    }
  }

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

  public void setSeparator(String separator) {
    this.separator = separator;
  }

  public void setBundleBaseName(String bundleBaseName) {
    this.bundleBaseName = bundleBaseName;
  }

  public void setLanguages(List<String> languages) {
    this.languages = languages;
  }

  public void setPropertiesStore(List<Map<String, String>> propertiesStore) {
    this.propertiesStore = propertiesStore;
  }

  public void setNative2ascii(boolean native2ascii) {
    this.native2ascii = native2ascii;
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
      throw new RuntimeException("Troubles convert '" + value + "' (" + this.inputEncoding + ") to " + convertedValue + " (" + this
          .outputEncoding + ")");
    }

    LOGGER.debug("Converted '{}' to '{}'", value, convertedValue);
    return convertedValue;
  }

  private void processHeader(String[] tokens) {
    for (int i = 1; i < tokens.length; i++) {
      String value = tokens[i];
      LOGGER.info("Processing header cell with value " + value);
      this.languages.add(value);
      this.propertiesStore.add(new HashMap<>());
    }
  }
}
