package pl.ppiorkowski.verjo.model;

import pl.ppiorkowski.verjo.xsd.Properties;
import pl.ppiorkowski.verjo.xsd.Property;

import java.util.Optional;

abstract class ModelWithProperties {

    Optional<String> getPropertyValue(String propertyName, Properties properties) {
        return properties.getProperty().stream()
                .filter(p -> propertyName.equals(p.getName()))
                .findAny()
                .map(Property::getValue);
    }
}
