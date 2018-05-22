package pl.ppiorkowski.verjo;

import org.jooq.util.*;
import pl.ppiorkowski.verjo.model.TableModel;
import pl.ppiorkowski.verjo.xsd.Column;

import java.util.ArrayList;
import java.util.List;

class VertabeloTableDefinition extends AbstractTableDefinition {

    private final TableModel table;

    VertabeloTableDefinition(SchemaDefinition schemaDefinition, TableModel tableModel) {
        super(schemaDefinition, tableModel.getName(), null);
        table = tableModel;
    }

    @Override
    protected List<ColumnDefinition> getElements0() {
        ArrayList<ColumnDefinition> result = new ArrayList<>();
        SchemaDefinition schema = getSchema();
        int position = 0;

        for (Column column : table.getColumns()) {
            String typeName = column.getType();
            String defaultValue = column.getDefaultValue();

            DefaultDataTypeDefinition dataTypeDef = new DefaultDataTypeDefinition(getDatabase(), schema, typeName,
                    null, null, null, null, defaultValue);
            DefaultColumnDefinition columnDef = new DefaultColumnDefinition(this, column.getName(), ++position,
                    dataTypeDef, false, column.getDescription());
            result.add(columnDef);
        }

        return result;
    }
}
