package com.github.piorkowskiprzemyslaw.verjo.model;

import com.github.piorkowskiprzemyslaw.verjo.xsd.View;
import com.github.piorkowskiprzemyslaw.verjo.xsd.ViewColumn;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Value(staticConstructor = "of")
public class ViewModel extends ModelWithProperties {

    private static final String SCHEMA_PROPERTY_NAME = "schema";

    View view;

    public String getSchema(String defaultSchema) {
        return getPropertyValue(SCHEMA_PROPERTY_NAME, view.getProperties())
                .orElse(defaultSchema);
    }

    public String getName() {
        return view.getName();
    }

    public List<ViewColumn> getColumns() {
        return view.getViewColumns().getViewColumn();
    }
}
