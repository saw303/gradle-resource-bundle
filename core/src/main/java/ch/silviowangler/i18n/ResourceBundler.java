/**
 * Copyright 2016 - 2018 Silvio Wangler (silvio.wangler@gmail.com)
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

import groovy.json.StringEscapeUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;

import static ch.silviowangler.i18n.Consts.ISO_8859_1;

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

        CSVParser records = CSVFormat.RFC4180
                .withDelimiter(separator.charAt(0))
                .withFirstRecordAsHeader()
                .withQuoteMode(QuoteMode.ALL)
                .parse(new InputStreamReader(new FileInputStream(this.csvFile), this.inputEncoding));

        final Map<String, Integer> headers = records.getHeaderMap();

        processHeader(headers.keySet());

        for (CSVRecord record : records) {
            processData(record);
        }

        final int propertiesFilesAmount = this.propertiesStore.size();
        LOGGER.debug("Will generate {} properties files", propertiesFilesAmount);

        // Properties Dateien schreiben
        for (int i = 0; i < propertiesFilesAmount; i++) {
            Map<String, String> properties = this.propertiesStore.get(i);
            File outputFile = new File(this.outputDir, this.bundleBaseName + "_" + this.languages.get(i) + ".properties");

            LOGGER.debug("Writing {} to {}", outputFile.getName(), outputFile.getParentFile().getAbsolutePath());

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

    public void setNative2ascii(boolean native2ascii) {
        this.native2ascii = native2ascii;
    }

    private void processData(CSVRecord record) throws UnsupportedEncodingException {

        String key = record.get(0);

        if (key.isEmpty()) return;
        for (int i = 1; i < record.size(); i++) {
            this.propertiesStore.get(i - 1).put(key, convertIfNecessary(record.get(i)));
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
            throw new ConversionException(String.format("Troubles converting '%s' (%s) to '%s' (%s)", value, this.inputEncoding, convertedValue,
                    this.outputEncoding));
        }

        LOGGER.debug("Converted '{}' to '{}'", value, convertedValue);
        return convertedValue;
    }

    private void processHeader(Set<String> headerNames) {

        final Iterator<String> iterator = headerNames.iterator();

        for (int i = 0; iterator.hasNext(); i++) {
            String value = iterator.next();

            if (value.length() != 2 && value.length() != 5) {
                continue;
            }

            LOGGER.info("Processing header cell with value {}", value);
            this.languages.add(value);
            this.propertiesStore.add(new HashMap<>());
        }
    }
}
