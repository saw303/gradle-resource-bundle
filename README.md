# Gradle Resource Bundle Plugin

[![Build Status](https://travis-ci.org/saw303/gradle-resource-bundle.svg?branch=master)](https://travis-ci.org/saw303/gradle-resource-bundle)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/c7c0aaca629f456390643b957844548a)](https://www.codacy.com/app/saw303/gradle-resource-bundle?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=saw303/gradle-resource-bundle&amp;utm_campaign=Badge_Grade)

## Introduction 
The purpose of this plugin is to create Properties files for Java Resource Bundle using a CSV (comma separated value) file as source.

## Usage
In order to use the plugin make sure you add and apply the Gradle Resource Bundle plugin to your build script.

    buildscript {
        repositories {
            // your repositories
            maven { url 'https://dl.bintray.com/saw303/releases' }
        }
    
        dependencies {
            classpath 'ch.silviowangler.gradle.i18n:gradle-resource-bundle:0.1.1'
        }
    }

    apply plugin: 'ch.silviowangler.gradle.i18n'
    
After applying the plugin you need to configure the plugins task `generateResourceBundle`.
    
    // configure the task
    generateResourceBundle {
        csvFile file("your-csv-source.csv")
        outputDir file("${projectDir}/build/resources")
        bundleBaseName = 'MyMessages'
    }

The plugins task currently provides the following properties.

Task property | Description | Default value
--- | --- | ---
csvFile | absolute Path to your CSV file | -
outputDir | Target folder where the plugin will write the resource bundle into | -
inputEncoding | source encoding | _ISO-8859-1_
outputEncoding | target encoding | _ISO-8859-1_
bundleBaseName | Resource bundle base name (messages_de.properties, messages_en.properties,...) | _messages_
separator | CSV separator | _,_

## CSV structure

The first line is the header that contains at least two columns. First column represents the `message key`.

    Message Key,de,de_CH,en,fr,it
    key1,Hallo,Hoi,Hello,Salut,Ciao
    key2,Auf wiedersehen,Adieu,Good bye,Au revoir,Ciao


