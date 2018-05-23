package pl.ppiorkowski.verjo.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.ppiorkowski.verjo.model.db_engine.DbEngineConverter;
import pl.ppiorkowski.verjo.xsd.Column;
import pl.ppiorkowski.verjo.xsd.PrimaryKey;
import pl.ppiorkowski.verjo.xsd.Table;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.jooq.tools.JooqLogger;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TableModel extends ModelWithProperties {

    private static final JooqLogger log = JooqLogger.getLogger(TableModel.class);
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

    public PrimaryKeyModel getPrimaryKey() {
        return PrimaryKeyModel.builder()
                .name(getPrimaryKeyName())
                .columnNames(getPkColumnNames())
                .build();
    }

    private String getPrimaryKeyName() {
        String pkName = table.getPrimaryKey().getName();
        if (pkName != null && !pkName.isEmpty()) {
            return pkName;
        }
        return String.format("%s_PK", table.getName());
    }

    private List<String> getPkColumnNames() {
        List<JAXBElement<Object>> pkColumns = table.getPrimaryKey().getColumns().getColumn();
        List<String> columns = pkColumns.stream()
                .map(jaxb -> (Column) jaxb.getValue())
                .map(Column::getName)
                .collect(Collectors.toList());
        if (columns.isEmpty()) {
            String tableName = getName();
            String schemaName = getSchema().orElse("default");
            log.warn("Empty Primary Key columns", "Empty columns list in primary key definition. " +
                    "Table: [" + tableName + "] schema:[" + schemaName + "]");
        }
        return columns;
    }
}
