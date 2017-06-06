/**
 * Copyright 2016 - 2017 Silvio Wangler (silvio.wangler@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.silviowangler.gradle.i18n;

import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.InputFile;
import org.gradle.api.tasks.OutputDirectory;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;

import ch.silviowangler.i18n.ResourceBundler;

/**
 * @author Silvio Wangler
 */
public class ResourceBundleTask extends DefaultTask {


  @InputFile
  private File csvFile;
  @OutputDirectory
  private File outputDir;
  private ResourceBundler resourceBundler;

  public ResourceBundleTask() {
    this.resourceBundler = new ResourceBundler();
  }

  public void setCsvFile(File csvFile) {
    this.csvFile = csvFile;
    this.resourceBundler.setCsvFile(getCsvFile());
  }

  public void setOutputDir(File outputDir) {
    this.outputDir = outputDir;
    this.resourceBundler.setOutputDir(getOutputDir());
  }

  public File getCsvFile() {
    return csvFile;
  }

  public File getOutputDir() {
    return outputDir;
  }

  public void setInputEncoding(String inputEncoding) {
    this.resourceBundler.setInputEncoding(inputEncoding);
  }

  public void setOutputEncoding(String outputEncoding) {
    this.resourceBundler.setOutputEncoding(outputEncoding);
  }

  public void setBundleBaseName(String bundleBaseName) {
    this.resourceBundler.setBundleBaseName(bundleBaseName);
  }

  public void setSeparator(String separator) {
    this.resourceBundler.setSeparator(separator);
  }

  public void setNative2ascii(boolean native2ascii) {
    this.resourceBundler.setNative2ascii(native2ascii);
  }

  @TaskAction
  public void generateResourceBundle() throws IOException {
    this.resourceBundler.generateResourceBundle();
  }
}
