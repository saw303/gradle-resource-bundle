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
package ch.silviowangler.i18n.cmd;

import static ch.silviowangler.i18n.Consts.ISO_8859_1;

import com.beust.jcommander.Parameter;

import java.io.File;

/**
 * @author Silvio Wangler
 */
public class Args {

  @Parameter(names = {"input"}, description = "Absolute path to the CSV file to process")
  private File csvFile;
  @Parameter(names = {"output"}, description = "Absolute path to the output directory")
  private File outputDir;
  @Parameter(names = {"inputEncoding"}, description = "Input encoding (default ISO-8859-1)")
  private String inputEncoding = ISO_8859_1;
  @Parameter(names = {"outputEncoding"}, description = "Output encoding (default ISO-8859-1)")
  private String outputEncoding = ISO_8859_1;
  @Parameter(names = {"separator"}, description = "Defines the CSV separator (default ,)")
  private String separator = ",";
  @Parameter(names = {"basename"}, description = "Defines the resource bundle base name (default messages)")
  private String bundleBaseName = "messages";
  @Parameter(names = {"native2ascii"}, description = "Force native2ascii (default flase)")
  private boolean native2ascii = false;

  public File getCsvFile() {
    return csvFile;
  }

  public File getOutputDir() {
    return outputDir;
  }

  public String getInputEncoding() {
    return inputEncoding;
  }

  public String getOutputEncoding() {
    return outputEncoding;
  }

  public String getSeparator() {
    return separator;
  }

  public String getBundleBaseName() {
    return bundleBaseName;
  }

  public boolean isNative2ascii() {
    return native2ascii;
  }

  @Override
  public String toString() {
    final StringBuffer sb = new StringBuffer("Args{");
    sb.append("csvFile=").append(csvFile);
    sb.append(", outputDir=").append(outputDir);
    sb.append(", inputEncoding='").append(inputEncoding).append('\'');
    sb.append(", outputEncoding='").append(outputEncoding).append('\'');
    sb.append(", separator='").append(separator).append('\'');
    sb.append(", bundleBaseName='").append(bundleBaseName).append('\'');
    sb.append(", native2ascii=").append(native2ascii);
    sb.append('}');
    return sb.toString();
  }
}
