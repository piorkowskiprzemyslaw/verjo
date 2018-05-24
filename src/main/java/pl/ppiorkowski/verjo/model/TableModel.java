package pl.ppiorkowski.verjo.model;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.jooq.tools.JooqLogger;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import pl.ppiorkowski.verjo.model.table.AlternateKeyModel;
import pl.ppiorkowski.verjo.model.table.ColumnCheckModel;
import pl.ppiorkowski.verjo.model.table.PrimaryKeyModel;
import pl.ppiorkowski.verjo.model.table.TableCheckModel;
import pl.ppiorkowski.verjo.xsd.AlternateKey;
import pl.ppiorkowski.verjo.xsd.Column;
import pl.ppiorkowski.verjo.xsd.Table;

@EqualsAndHashCode(callSuper = false)
@Value(staticConstructor = "of")
public class TableModel extends ModelWithProperties {

    private static final JooqLogger log = JooqLogger.getLogger(TableModel.class);
    private static final String SCHEMA_PROPERTY_NAME = "schema";

    private final Table table;

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
                .columns(getPkColumns())
                .build();
    }

    private String getPrimaryKeyName() {
        String pkName = table.getPrimaryKey().getName();
        if (pkName != null && !pkName.isEmpty()) {
            return pkName;
        }
        return String.format("%s_PK", table.getName());
    }

    private List<Column> getPkColumns() {
        List<JAXBElement<Object>> pkColumns = table.getPrimaryKey().getColumns().getColumn();
        List<Column> columns = pkColumns.stream()
                .map(jaxb -> (Column) jaxb.getValue())
                .collect(Collectors.toList());
        if (columns.isEmpty()) {
            String tableName = getName();
            String schemaName = getSchema().orElse("default");
            log.warn("Empty Primary Key columns list", "Empty columns list in primary key definition. " +
                    "Table: [" + tableName + "] schema: [" + schemaName + "]");
        }
        return columns;
    }

    public List<AlternateKeyModel> getAlternateKeys() {
        return table.getAlternateKeys().getAlternateKey().stream()
                .map(this::toAlternateKeyModel)
                .collect(Collectors.toList());
    }

    private AlternateKeyModel toAlternateKeyModel(AlternateKey ak) {
        List<Column> akColumns = ak.getColumns().getColumn().stream()
                .map(jaxb -> (Column) jaxb.getValue())
                .collect(Collectors.toList());
        if (akColumns.isEmpty()) {
            String tableName = getName();
            String schemaName = getSchema().orElse("default");
            log.warn("Empty Alternate Key columns list", "Empty columns list in alternate key definition. " +
                    "Table: [" + tableName + "] schema: [" + schemaName + "]");
        }

        return AlternateKeyModel.builder()
                .name(ak.getName())
                .columns(akColumns)
                .build();
    }

    public List<TableCheckModel> getTableChecks() {
        return table.getTableChecks().getTableCheck().stream()
                .filter(tc -> tc.getCheckExpression() != null)
                .map(tc -> new TableCheckModel(tc.getName(), tc.getCheckExpression()))
                .collect(Collectors.toList());
    }

    public List<ColumnCheckModel> getColumnChecks() {
        String tableName = table.getName();
        return table.getColumns().getColumn().stream()
                .map(c -> new ColumnCheckModel(tableName, c))
                .filter(ColumnCheckModel::isMeaningful)
                .collect(Collectors.toList());
    }
}
