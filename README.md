## VerJo [![Build Status](https://travis-ci.org/piorkowskiprzemyslaw/verjo.svg?branch=master)](https://travis-ci.org/piorkowskiprzemyslaw/verjo)[![codecov](https://codecov.io/gh/piorkowskiprzemyslaw/verjo/branch/master/graph/badge.svg)](https://codecov.io/gh/piorkowskiprzemyslaw/verjo)[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.piorkowskiprzemyslaw/verjo/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.github.piorkowskiprzemyslaw/verjo)

VerJo is an jOOQ plugin providing code generation from Vertabelo XML files.

## Getting started

[Vertabelo](https://www.vertabelo.com) is a great tool for creating and sharing online ERD models, [jOOQ](https://www.jooq.org) is an orm framework well supported by Spring. VerJo supports jOOQ codegen version greater than 3.11.x and Vertabelo schema v2.3. For jOOQ codegen version 3.10.x use 0.1.x.

VerJo is inspired by old [vertabelo-jooq](https://github.com/Vertabelo/vertabelo-jooq) plugin which worked well for jOOQ 3.7.x.

## Basic use
VerJo provides VertabeloXML aware Database implementation. Below [gradle-jooq-plugin](https://github.com/etiennestuder/gradle-jooq-plugin) configuration will generate sources for your model
```groovy

buildscript {
    classpath 'javax.xml.bind:jaxb-api:2.3.0'
    classpath 'com.sun.xml.bind:jaxb-core:2.3.0'
    classpath 'com.sun.xml.bind:jaxb-impl:2.3.0'
    classpath 'javax.activation:activation:1.1.1'
}

repositories {
    mavenCentral()
}

plugins {
    id 'nu.studer.jooq' version '3.0.2'
}

dependencies {
    compile 'org.jooq:jooq'
    jooqRuntime 'javax.xml.bind:jaxb-api:2.3.0'
    jooqRuntime 'com.sun.xml.bind:jaxb-core:2.3.0'
    jooqRuntime 'com.sun.xml.bind:jaxb-impl:2.3.0'
    jooqRuntime 'javax.activation:activation:1.1.1'
    jooqRuntime 'com.github.piorkowskiprzemyslaw:verjo:0.2.0'
}

jooq {
    version = '3.11.2'
    edition = 'OSS'
    sample(sourceSets.main) {
        generator {
            database {
                name = 'com.github.piorkowskiprzemyslaw.verjo.VertabeloDbDefinition'
                properties {
                    property {
                        key = "vertabelo-xml-file"
                        value = file("path/to/your/vertabelo-xml-file.xml")
                    }
                    
                    property {
                        key = "vertabelo-default-schema"
                        value = "default-schema-to-generate"
                    }
                }
            }
        }
    }
}
```

## Properties
VerJo is configurable with listed below properties

property name | obligatory | description
:---: | :---: | :---:
`vertabelo-xml-file` | yes | path to vertabelo xml file
`vertabelo-default-schema` | no | name of default schema which should be generated. Default value is empty string `""`.

## Demo
Working VerJo sample configuration can be found [here](https://github.com/piorkowskiprzemyslaw/verjo-test).

## License
**verjo** is published under MIT license.