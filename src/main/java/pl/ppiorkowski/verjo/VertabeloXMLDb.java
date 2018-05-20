package pl.ppiorkowski.verjo;

import org.jooq.DSLContext;
import org.jooq.util.*;
import pl.ppiorkowski.verjo.model_provider.DatabaseModelProvider;
import pl.ppiorkowski.verjo.xsd.DatabaseEngine;

import java.sql.SQLException;
import java.util.List;

public class VertabeloXMLDb extends AbstractDatabase {

    private static final String XML_FILE_PROPERTY = "vertabeloXML";

    private DatabaseModelProvider databaseModelProvider;

    private String getXmlFileProperty() {
        return getProperties().getProperty(XML_FILE_PROPERTY);
    }

    private DatabaseModelProvider getProvider() {
        if (databaseModelProvider == null) {
            databaseModelProvider = new DatabaseModelProvider(getXmlFileProperty());
        }
        return databaseModelProvider;
    }


    @Override
    protected DSLContext create0() {
        DatabaseEngine dbEngine = getProvider().getModel().getDatabaseEngine();

        return null;
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations r) throws SQLException {

    }

    @Override
    protected void loadUniqueKeys(DefaultRelations r) throws SQLException {

    }

    @Override
    protected void loadForeignKeys(DefaultRelations r) throws SQLException {

    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) throws SQLException {

    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() throws SQLException {
        return null;
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() throws SQLException {
        return null;
    }

    @Override
    protected List<SequenceDefinition> getSequences0() throws SQLException {
        return null;
    }

    @Override
    protected List<TableDefinition> getTables0() throws SQLException {
        return null;
    }

    @Override
    protected List<RoutineDefinition> getRoutines0() throws SQLException {
        return null;
    }

    @Override
    protected List<PackageDefinition> getPackages0() throws SQLException {
        return null;
    }

    @Override
    protected List<EnumDefinition> getEnums0() throws SQLException {
        return null;
    }

    @Override
    protected List<DomainDefinition> getDomains0() throws SQLException {
        return null;
    }

    @Override
    protected List<UDTDefinition> getUDTs0() throws SQLException {
        return null;
    }

    @Override
    protected List<ArrayDefinition> getArrays0() throws SQLException {
        return null;
    }
}
