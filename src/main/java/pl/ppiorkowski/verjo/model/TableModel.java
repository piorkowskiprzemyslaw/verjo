package pl.ppiorkowski.verjo.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.ppiorkowski.verjo.xsd.Column;
import pl.ppiorkowski.verjo.xsd.Table;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TableModel extends ModelWithProperties {

    private static final String SCHEMA_PROPERTY_NAME = "schema";

    private final Table table;

    public static TableModel of(Table table) {
        return new TableModel(table);
    }

    public Optional<String> getSchema() {
        return getPropertyValue(SCHEMA_PROPERTY_NAME, table.getProperties());
    }

    public String getSchemaString() {
        return getSchema().orElse("");
    }

    public String getName() {
        return table.getName();
    }

    public List<Column> getColumns() {
        return table.getColumns().getColumn();
    }
}
