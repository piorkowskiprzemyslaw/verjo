package com.github.piorkowskiprzemyslaw.verjo.model;

import com.github.piorkowskiprzemyslaw.verjo.model.table.AlternateKeyModel;
import com.github.piorkowskiprzemyslaw.verjo.model.table.ColumnCheckModel;
import com.github.piorkowskiprzemyslaw.verjo.model.table.PrimaryKeyModel;
import com.github.piorkowskiprzemyslaw.verjo.model.table.TableCheckModel;
import com.github.piorkowskiprzemyslaw.verjo.xsd.AlternateKey;
import com.github.piorkowskiprzemyslaw.verjo.xsd.Column;
import com.github.piorkowskiprzemyslaw.verjo.xsd.Table;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jooq.tools.JooqLogger;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = false)
@Value(staticConstructor = "of")
public class TableModel extends ModelWithProperties {

    private static final JooqLogger log = JooqLogger.getLogger(TableModel.class);
    private static final String SCHEMA_PROPERTY_NAME = "schema";

    Table table;

    public String getSchema(String defaultSchema) {
        return getPropertyValue(SCHEMA_PROPERTY_NAME, table.getProperties())
                .orElse(defaultSchema);
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
            log.warn("Empty Primary Key columns list", "Empty columns list in primary key definition. " +
                    "Table: [" + tableName + "]");
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
            log.warn("Empty Alternate Key columns list", "Empty columns list in alternate key definition. " +
                    "Table: [" + tableName + "]");
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
