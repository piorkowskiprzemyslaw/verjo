package com.github.piorkowskiprzemyslaw.verjo.model;

import com.github.piorkowskiprzemyslaw.verjo.xsd.Properties;
import com.github.piorkowskiprzemyslaw.verjo.xsd.Property;

import java.util.Optional;

abstract class ModelWithProperties {

    Optional<String> getPropertyValue(String propertyName, Properties properties) {
        return properties.getProperty().stream()
                .filter(p -> propertyName.equals(p.getName()))
                .findAny()
                .map(Property::getValue);
    }
}
