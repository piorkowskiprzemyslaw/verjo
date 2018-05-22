package pl.ppiorkowski.verjo;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.*;
import pl.ppiorkowski.verjo.model.DbModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static pl.ppiorkowski.verjo.model.DbModelFactory.build;

public class VertabeloDbDefinition extends AbstractDatabase {

    private static final String XML_FILE_PROPERTY = "vertabeloXMLFile";

    private DbModel dbModel;

    private String getXmlFileProperty() {
        return getProperties().getProperty(XML_FILE_PROPERTY);
    }

    private DbModel getModel() {
        if (null == dbModel) {
            dbModel = build(getXmlFileProperty());
        }
        return dbModel;
    }

    @Override
    protected DSLContext create0() {
        SQLDialect dialect = getModel().getDialect();
        return DSL.using(dialect);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations r) {

    }

    @Override
    protected void loadUniqueKeys(DefaultRelations r) {

    }

    @Override
    protected void loadForeignKeys(DefaultRelations r) {

    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) {

    }

    @Override
    protected List<SchemaDefinition> getSchemata0() {
        Set<String> schemaNames = getModel().getSchemaNames();
        return schemaNames.stream()
                .map(name -> new SchemaDefinition(this, name, null))
                .collect(Collectors.toList());
    }

    @Override
    protected List<SequenceDefinition> getSequences0() {
        return null;
    }

    @Override
    protected List<TableDefinition> getTables0() {
        List<TableDefinition> result = new ArrayList<>();
        List<String> inputSchemata = getInputSchemata();

        getModel().selectTables(inputSchemata).forEach(table -> {
            String schemaName = table.getSchemaString();
            SchemaDefinition schema = getSchema(schemaName);
            result.add(new VertabeloTableDefinition(schema, table));
        });

        getModel().selectViews(inputSchemata).forEach(view -> {
            String schemaName = view.getSchemaString();
            SchemaDefinition schema = getSchema(schemaName);
            result.add(new VertabeloViewDefinition(schema, view));
        });

        return result;
    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() {
        return emptyList();
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() {
        return emptyList();
    }

    @Override
    protected List<PackageDefinition> getPackages0() {
        return emptyList();
    }

    @Override
    protected List<EnumDefinition> getEnums0() {
        return emptyList();
    }

    @Override
    protected List<DomainDefinition> getDomains0() {
        return emptyList();
    }

    @Override
    protected List<UDTDefinition> getUDTs0() {
        return emptyList();
    }

    @Override
    protected List<ArrayDefinition> getArrays0() {
        return emptyList();
    }
}
