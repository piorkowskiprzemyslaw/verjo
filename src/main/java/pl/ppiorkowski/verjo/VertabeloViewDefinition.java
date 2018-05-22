package pl.ppiorkowski.verjo;

import org.jooq.util.AbstractTableDefinition;
import org.jooq.util.SchemaDefinition;
import pl.ppiorkowski.verjo.model.ViewModel;

class VertabeloViewDefinition extends AbstractTableDefinition {

    VertabeloViewDefinition(SchemaDefinition schema, ViewModel view) {
        super(schema, view.getName(), null);
    }
}
