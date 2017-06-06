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

import com.beust.jcommander.JCommander;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import ch.silviowangler.i18n.ResourceBundler;


/**
 * @author Silvio Wangler
 */
public class CommandLineProcessor {

  private final static Logger LOGGER = LogManager.getFormatterLogger(CommandLineProcessor.class);
  private static ResourceBundler resourceBundler;

  public static void main(String[] argv) {

    Args args = new Args();

    JCommander.newBuilder().addObject(args).build().parse(argv);

    LOGGER.debug("Params are %s", args);

    resourceBundler = new ResourceBundler();
    resourceBundler.setCsvFile(args.getCsvFile());
    resourceBundler.setOutputDir(args.getOutputDir());
    resourceBundler.setNative2ascii(args.isNative2ascii());
    resourceBundler.setSeparator(args.getSeparator());
    resourceBundler.setBundleBaseName(args.getBundleBaseName());
    resourceBundler.setInputEncoding(args.getInputEncoding());

    try {
      resourceBundler.generateResourceBundle();
    } catch (IOException e) {
      LOGGER.error("Unable to generate resource bundle", e);
      System.exit(-1);
    }

    LOGGER.info("Successfully ended process");
  }
}
