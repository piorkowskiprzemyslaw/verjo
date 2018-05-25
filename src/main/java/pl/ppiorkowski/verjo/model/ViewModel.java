package pl.ppiorkowski.verjo.model;

import java.util.List;
import java.util.Optional;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import pl.ppiorkowski.verjo.xsd.View;
import pl.ppiorkowski.verjo.xsd.ViewColumn;

@EqualsAndHashCode(callSuper = false)
@Value(staticConstructor = "of")
public class ViewModel extends ModelWithProperties {

    private static final String SCHEMA_PROPERTY_NAME = "schema";

    private final View view;

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
