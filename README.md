## VerJo [![Build Status](https://travis-ci.org/piorkowskiprzemyslaw/verjo.svg?branch=master)](https://travis-ci.org/piorkowskiprzemyslaw/verjo)[![codecov](https://codecov.io/gh/piorkowskiprzemyslaw/verjo/branch/master/graph/badge.svg)](https://codecov.io/gh/piorkowskiprzemyslaw/verjo)

VerJo is an jOOQ plugin providing code generation from Vertabelo XML files.

## Getting started

[Vertabelo](https://www.vertabelo.com) is a great tool for creating and sharing online ERD models, [jOOQ](https://www.jooq.org) is an orm framework well supported by Spring framework. VerJo supports jOOQ codegen version greater than 3.10.x and Vertabelo schema v2.3. It's inspired by old [vertabelo-jooq](https://github.com/Vertabelo/vertabelo-jooq) plugin which worked well for jOOQ 3.7.x. 

Right now VerJo is not available in any public artifact repository. You can install it locally by with the following command
```bash
./gradlew publishToMavenLocal
```

## Basic use
VerJo provides VertabeloXML aware Database implementation. Below [gradle-jooq-plugin](https://github.com/etiennestuder/gradle-jooq-plugin) configuration will generate sources for your model
```groovy
plugins {
    id 'nu.studer.jooq' version '2.0.9'
}

dependencies {
    compile 'org.jooq:jooq'
    jooqRuntime 'com.github.piorkowskiprzemyslaw:verjo:0.1.0'
}

jooq {
    version = '3.10.4'
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
`vertabelo-default-schema` | no | name of default schema which should be generated. Default value is `""`.

## License
**verjo** is published under MIT license.