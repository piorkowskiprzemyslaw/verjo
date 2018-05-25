package pl.ppiorkowski.verjo;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static pl.ppiorkowski.verjo.model.DbModelFactory.build;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.jooq.util.AbstractDatabase;
import org.jooq.util.ArrayDefinition;
import org.jooq.util.CatalogDefinition;
import org.jooq.util.ColumnDefinition;
import org.jooq.util.DefaultCheckConstraintDefinition;
import org.jooq.util.DefaultDataTypeDefinition;
import org.jooq.util.DefaultRelations;
import org.jooq.util.DefaultSequenceDefinition;
import org.jooq.util.DomainDefinition;
import org.jooq.util.EnumDefinition;
import org.jooq.util.PackageDefinition;
import org.jooq.util.RoutineDefinition;
import org.jooq.util.SchemaDefinition;
import org.jooq.util.SequenceDefinition;
import org.jooq.util.TableDefinition;
import org.jooq.util.UDTDefinition;

import pl.ppiorkowski.verjo.model.DbModel;
import pl.ppiorkowski.verjo.model.SequenceModel;
import pl.ppiorkowski.verjo.model.TableModel;
import pl.ppiorkowski.verjo.model.ViewModel;
import pl.ppiorkowski.verjo.model.table.AlternateKeyModel;
import pl.ppiorkowski.verjo.model.table.PrimaryKeyModel;

public class VertabeloDbDefinition extends AbstractDatabase {

    private static final String XML_FILE_PROPERTY = "vertabelo-xml-file";
    private static final String DEFAULT_SCHEMA_PROPERTY = "vertabelo-default-schema";
    private static final String DEFAULT_SCHEMA_DEFAULT_VALUE = "";

    private DbModel dbModel;

    private String getXmlFilePath() {
        return getProperties().getProperty(XML_FILE_PROPERTY);
    }

    private String getDefaultSchema() {
        return Optional.ofNullable(getProperties().getProperty(DEFAULT_SCHEMA_PROPERTY))
                .orElse(DEFAULT_SCHEMA_DEFAULT_VALUE);
    }

    private DbModel getModel() {
        if (null == dbModel) {
            dbModel = build(getXmlFilePath());
        }
        return dbModel;
    }

    private SchemaDefinition getTableSchema(TableModel t) {
        String schemaName = t.getSchema(getDefaultSchema());
        return getSchema(schemaName);
    }

    private SchemaDefinition getViewSchema(ViewModel v) {
        String schemaName = v.getSchema(getDefaultSchema());
        return getSchema(schemaName);
    }

    private SchemaDefinition getSequenceSchema(SequenceModel s) {
        String schemaName = s.getSchema(getDefaultSchema());
        return getSchema(schemaName);
    }

    @Override
    protected DSLContext create0() {
        SQLDialect dialect = getModel().getDialect();
        return DSL.using(dialect);
    }

    @Override
    protected void loadPrimaryKeys(DefaultRelations r) {
        getModel().selectTables(getInputSchemata(), getDefaultSchema()).forEach(table -> {
            SchemaDefinition schemaDef = getTableSchema(table);
            TableDefinition tableDef = getTable(schemaDef, table.getName());
            PrimaryKeyModel pk = table.getPrimaryKey();
            pk.getColumnNames()
                    .forEach(pkColumn -> r.addPrimaryKey(pk.getName(), tableDef.getColumn(pkColumn)));
        });
    }

    @Override
    protected void loadUniqueKeys(DefaultRelations r) {
        getModel().selectTables(getInputSchemata(), getDefaultSchema()).forEach(table -> {
            SchemaDefinition schemaDef = getTableSchema(table);
            TableDefinition tableDef = getTable(schemaDef, table.getName());
            table.getAlternateKeys().forEach(ak -> loadAlternateKey(r, tableDef, ak));
        });
    }

    private void loadAlternateKey(DefaultRelations relations, TableDefinition tableDefinition,
            AlternateKeyModel alternateKey) {
        alternateKey.getColumnNames()
                .forEach(columnName -> relations.addUniqueKey(alternateKey.getName(), tableDefinition.getColumn(columnName)));
    }

    @Override
    protected void loadForeignKeys(DefaultRelations r) {
        getModel().getForeignKeys(getInputSchemata(), getDefaultSchema()).forEach(fk -> {
            SchemaDefinition schemaDef = getSchema(fk.getUniqueKeySchemaName());
            TableDefinition fkTableDef = getTable(schemaDef, fk.getFkTableName());

            fk.getFkTableReferenceColumnNames().forEach(referencedColumn -> {
                ColumnDefinition columnDef = fkTableDef.getColumn(referencedColumn);
                r.addForeignKey(fk.getForeignKeyName(), fk.getUniqueKeyName(), columnDef, schemaDef);
            });
        });
    }

    @Override
    protected void loadCheckConstraints(DefaultRelations r) {
        getModel().selectTables(getInputSchemata(), getDefaultSchema()).forEach(t -> loadTableCheckConstraints(r, t));
    }

    private void loadTableCheckConstraints(DefaultRelations r, TableModel table) {
        SchemaDefinition schema = getTableSchema(table);
        TableDefinition tableDef = getTable(schema, table.getName());

        table.getTableChecks().forEach(tc -> {
            DefaultCheckConstraintDefinition constraint = new DefaultCheckConstraintDefinition(schema,
                    tableDef, tc.getName(), tc.getCheckExpression());
            r.addCheckConstraint(tableDef, constraint);
        });

        table.getColumnChecks().forEach(cc -> {
            DefaultCheckConstraintDefinition constraint = new DefaultCheckConstraintDefinition(schema,
                    tableDef, cc.getName(), cc.getCheckExpression());
            r.addCheckConstraint(tableDef, constraint);
        });
    }

    @Override
    protected List<SchemaDefinition> getSchemata0() {
        Set<String> schemaNames = getModel().getSchemaNames(getDefaultSchema());
        return schemaNames.stream()
                .map(name -> new SchemaDefinition(this, name, null))
                .collect(Collectors.toList());
    }

    @Override
    protected List<SequenceDefinition> getSequences0() {
        return getModel().selectSequences(getInputSchemata(), getDefaultSchema())
                .map(sequence -> {
                    SchemaDefinition schema = getSequenceSchema(sequence);
                    DefaultDataTypeDefinition typeDef = new DefaultDataTypeDefinition(this, schema, "BIGINT");
                    return new DefaultSequenceDefinition(schema, sequence.getName(), typeDef);
                })
                .collect(Collectors.toList());
    }

    @Override
    protected List<TableDefinition> getTables0() {
        List<TableDefinition> result = new ArrayList<>();
        List<String> inputSchemata = getInputSchemata();

        getModel().selectTables(inputSchemata, getDefaultSchema()).forEach(table -> {
            SchemaDefinition schema = getTableSchema(table);
            result.add(new VertabeloTableDefinition(schema, table));
        });

        getModel().selectViews(inputSchemata, getDefaultSchema()).forEach(view -> {
            SchemaDefinition schema = getViewSchema(view);
            result.add(new VertabeloViewDefinition(schema, view));
        });

        return result;
    }

    @Override
    protected List<CatalogDefinition> getCatalogs0() {
        return singletonList(new CatalogDefinition(this, "", ""));
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
