package pl.ppiorkowski.verjo;

import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.SchemaDefinition;
import pl.ppiorkowski.verjo.model.TableModel;

public class VertabeloTableDefinition extends AbstractTableDefinition {

    public VertabeloTableDefinition(SchemaDefinition schemaDefinition, TableModel table) {
        super(schemaDefinition, table.getName(), null);
    }

}
