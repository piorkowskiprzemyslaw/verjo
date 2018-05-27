package com.github.piorkowskiprzemyslaw.verjo.model;

import com.github.piorkowskiprzemyslaw.verjo.xsd.Column;
import com.github.piorkowskiprzemyslaw.verjo.xsd.Properties;
import com.github.piorkowskiprzemyslaw.verjo.xsd.Property;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.github.piorkowskiprzemyslaw.verjo.model.DbModel.SCHEMA_PROPERTY_NAME;

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
