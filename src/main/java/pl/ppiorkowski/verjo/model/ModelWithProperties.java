package pl.ppiorkowski.verjo.model;

import java.util.Optional;

import pl.ppiorkowski.verjo.xsd.Properties;
import pl.ppiorkowski.verjo.xsd.Property;

abstract class ModelWithProperties {

    Optional<String> getPropertyValue(String propertyName, Properties properties) {
        return properties.getProperty().stream()
                .filter(p -> propertyName.equals(p.getName()))
                .findAny()
                .map(Property::getValue);
    }
}
