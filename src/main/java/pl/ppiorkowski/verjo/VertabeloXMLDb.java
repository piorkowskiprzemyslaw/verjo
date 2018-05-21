package pl.ppiorkowski.verjo;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.*;
import pl.ppiorkowski.verjo.model.VertabeloDbModel;
import pl.ppiorkowski.verjo.model.VertabeloDbModelFactory;

import java.util.List;

import static java.util.Collections.emptyList;

public class VertabeloXMLDb extends AbstractDatabase {

    private static final String XML_FILE_PROPERTY = "vertabeloXMLFile";

    private VertabeloDbModel dbModel;

    private String getXmlFileProperty() {
        return getProperties().getProperty(XML_FILE_PROPERTY);
    }

    private VertabeloDbModel getModel() {
        if (null == dbModel) {
            dbModel = VertabeloDbModelFactory.build(getXmlFileProperty());
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
        return null;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() {
        return null;
    }

    @Override
    protected List<TableDefinition> getTables0() {
        return null;
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
