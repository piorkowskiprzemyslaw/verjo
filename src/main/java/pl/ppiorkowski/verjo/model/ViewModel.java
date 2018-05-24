package pl.ppiorkowski.verjo.model;

import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.ppiorkowski.verjo.xsd.View;
import pl.ppiorkowski.verjo.xsd.ViewColumn;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ViewModel extends ModelWithProperties {

    private static final String SCHEMA_PROPERTY_NAME = "schema";

    private final View view;

    public static ViewModel of(View view) {
        return new ViewModel(view);
    }

    public Optional<String> getSchema() {
        return getPropertyValue(SCHEMA_PROPERTY_NAME, view.getProperties());
    }

    public String getSchemaString() {
        return getSchema().orElse("");
    }

    public String getName() {
        return view.getName();
    }

    public List<ViewColumn> getColumns() {
        return view.getViewColumns().getViewColumn();
    }

    public boolean isInsideOneOfSchemas(List<String> inputSchemas) {
        return inputSchemas.contains(getSchemaString());
    }
}
