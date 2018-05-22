package pl.ppiorkowski.verjo;

import org.jooq.util.*;
import pl.ppiorkowski.verjo.model.ViewModel;
import pl.ppiorkowski.verjo.xsd.ViewColumn;

import java.util.ArrayList;
import java.util.List;

class VertabeloViewDefinition extends AbstractTableDefinition {

    private final ViewModel view;

    VertabeloViewDefinition(SchemaDefinition schema, ViewModel viewModel) {
        super(schema, viewModel.getName(), null);
        view = viewModel;
    }

    @Override
    protected List<ColumnDefinition> getElements0() {
        ArrayList<ColumnDefinition> result = new ArrayList<>();
        SchemaDefinition schema = getSchema();
        int position = 0;

        for (ViewColumn column : view.getColumns()) {
            String typeName = column.getType();

            DefaultDataTypeDefinition dataTypeDef = new DefaultDataTypeDefinition(getDatabase(), schema, typeName);
            DefaultColumnDefinition columnDef = new DefaultColumnDefinition(this, column.getName(), ++position,
                    dataTypeDef, false, column.getDescription());
            result.add(columnDef);
        }
        return result;
    }
}
