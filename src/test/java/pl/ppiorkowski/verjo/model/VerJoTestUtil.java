package pl.ppiorkowski.verjo.model;

import static pl.ppiorkowski.verjo.model.DbModel.SCHEMA_PROPERTY_NAME;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import pl.ppiorkowski.verjo.xsd.Column;
import pl.ppiorkowski.verjo.xsd.Properties;
import pl.ppiorkowski.verjo.xsd.Property;

final class VerJoTestUtil {

    private VerJoTestUtil() { }

    static Properties buildSchemaProperties(String schemaName) {
        return new Properties()
                .withProperty(new Property()
                        .withName(SCHEMA_PROPERTY_NAME)
                        .withValue(schemaName));
    }

    static JAXBElement<Object> buildJAXBWithColumn(Column c) {
        QName qName = new QName("nsURI", "localPart");
        return new JAXBElement<>(qName, Object.class, c);
    }

    static <T> Set<T> asSet(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }
}
