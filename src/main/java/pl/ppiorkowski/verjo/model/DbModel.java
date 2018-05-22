package pl.ppiorkowski.verjo.model;

import lombok.RequiredArgsConstructor;
import org.jooq.SQLDialect;
import pl.ppiorkowski.verjo.model.db_engine.DbEngineConverter;
import pl.ppiorkowski.verjo.xsd.DatabaseEngine;
import pl.ppiorkowski.verjo.xsd.DatabaseModel;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class DbModel {
    static final String SCHEMA_PROPERTY_NAME = "schema";

    private final DatabaseModel databaseModel;

    private Stream<TableModel> tables() {
        return databaseModel.getTables().getTable().stream()
                .map(TableModel::of);
    }

    private Stream<ViewModel> views() {
        return databaseModel.getViews().getView().stream()
                .map(ViewModel::of);
    }

    public SQLDialect getDialect() {
        DatabaseEngine dbEngine = databaseModel.getDatabaseEngine();
        return DbEngineConverter.asSQLDialect(dbEngine);
    }

    public Set<String> getSchemaNames() {
        HashSet<String> result = tables()
                .map(TableModel::getSchema)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toCollection(HashSet::new));

        Set<String> viewSchemas = views()
                .map(ViewModel::getSchema)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
        result.addAll(viewSchemas);

        return result;
    }

    public Stream<TableModel> selectTables(List<String> inputSchemas) {
        HashSet<String> schemasSet = new HashSet<>(inputSchemas);
        return tables()
                .filter(table -> {
                    String tableSchema = table.getSchemaString();
                    return schemasSet.contains(tableSchema);
                });
    }

    public Stream<ViewModel> selectViews(List<String> inputSchemas) {
        HashSet<String> schemasSet = new HashSet<>(inputSchemas);
        return views()
                .filter(view -> {
                    String viewSchema = view.getSchemaString();
                    return schemasSet.contains(viewSchema);
                });
    }

}
