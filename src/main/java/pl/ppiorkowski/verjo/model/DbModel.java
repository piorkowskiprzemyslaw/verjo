package pl.ppiorkowski.verjo.model;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jooq.SQLDialect;

import lombok.RequiredArgsConstructor;
import pl.ppiorkowski.verjo.model.db_engine.DbEngineConverter;
import pl.ppiorkowski.verjo.xsd.DatabaseEngine;
import pl.ppiorkowski.verjo.xsd.DatabaseModel;

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

    private Stream<SequenceModel> sequences() {
        return databaseModel.getSequences().getSequence().stream()
                .map(SequenceModel::of);
    }

    private Stream<ReferenceModel> references() {
        return databaseModel.getReferences().getReference().stream()
                .map(ReferenceModel::of);
    }

    public SQLDialect getDialect() {
        DatabaseEngine dbEngine = databaseModel.getDatabaseEngine();
        return DbEngineConverter.asSQLDialect(dbEngine);
    }

    public Set<String> getSchemaNames(String defaultSchema) {
        HashSet<String> result = tables()
                .map(t -> t.getSchema(defaultSchema))
                .collect(Collectors.toCollection(HashSet::new));

        Set<String> viewSchemas = views()
                .map(v -> v.getSchema(defaultSchema))
                .collect(Collectors.toSet());
        result.addAll(viewSchemas);

        return result;
    }

    public Stream<TableModel> selectTables(List<String> inputSchemas, String defaultSchema) {
        HashSet<String> schemasSet = new HashSet<>(inputSchemas);
        return tables()
                .filter(table -> {
                    String tableSchema = table.getSchema(defaultSchema);
                    return schemasSet.contains(tableSchema);
                });
    }

    public Stream<ViewModel> selectViews(List<String> inputSchemas, String defaultSchema) {
        HashSet<String> schemasSet = new HashSet<>(inputSchemas);
        return views()
                .filter(view -> {
                    String viewSchema = view.getSchema(defaultSchema);
                    return schemasSet.contains(viewSchema);
                });
    }

    public Stream<SequenceModel> selectSequences(List<String> inputSchemas, String defaultSchema) {
        HashSet<String> schemasSet = new HashSet<>(inputSchemas);
        return sequences()
                .filter(sequence -> {
                    String sequenceSchema = sequence.getSchema(defaultSchema);
                    return schemasSet.contains(sequenceSchema);
                });
    }

    public Set<ForeignKeyModel> getForeignKeys(List<String> inputSchemas, String defaultSchema) {
        return references()
                .filter(reference -> reference.isReferenceInSchema(defaultSchema, inputSchemas))
                .map(reference -> buildFKFromReference(reference, defaultSchema))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    private Optional<ForeignKeyModel> buildFKFromReference(ReferenceModel referenceModel, String defaultSchema) {
        return referenceModel.getUniqueKeyName()
                .map(uniqueKeyName -> buildFKFromReferenceAndUK(uniqueKeyName, referenceModel, defaultSchema));
    }

    private ForeignKeyModel buildFKFromReferenceAndUK(String uk, ReferenceModel referenceModel, String defaultSchema) {
        return ForeignKeyModel.builder()
                .uniqueKeyName(uk)
                .uniqueKeySchemaName(referenceModel.getPKTable().getSchema(defaultSchema))
                .foreignKeyName(referenceModel.getName())
                .fkTable(referenceModel.getFKTable())
                .fkTableReferenceColumns(referenceModel.getFKTableReferenceColumns())
                .build();
    }
}
