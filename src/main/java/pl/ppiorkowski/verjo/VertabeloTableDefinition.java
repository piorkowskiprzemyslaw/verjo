package pl.ppiorkowski.verjo;

import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.SchemaDefinition;
import pl.ppiorkowski.verjo.model.TableModel;

class VertabeloTableDefinition extends AbstractTableDefinition {

    VertabeloTableDefinition(SchemaDefinition schemaDefinition, TableModel table) {
        super(schemaDefinition, table.getName(), null);
    }

}
