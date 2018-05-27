package com.github.piorkowskiprzemyslaw.verjo.model;

import com.github.piorkowskiprzemyslaw.verjo.xsd.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBElement;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.github.piorkowskiprzemyslaw.verjo.model.VerJoTestUtil.buildJAXBWithColumn;
import static com.github.piorkowskiprzemyslaw.verjo.model.VerJoTestUtil.buildSchemaProperties;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DbModel")
class DbModelForeignKeysTest {
    private static final String DEFAULT_SCHEMA = "defaultSchema";
    private static final List<String> INPUT_SCHEMA = singletonList(DEFAULT_SCHEMA);

    private DatabaseModel databaseModel;
    private DbModel dbModel;

    @BeforeEach
    void setup() {
        databaseModel = new DatabaseModel()
                .withTables(new Tables())
                .withReferences(new References());
        dbModel = new DbModel(databaseModel);
    }

    @Test
    @DisplayName("returns empty foreign keys list when no references available")
    void shouldReturnEmptyWhenNoReferencesAvailable() {
        // when
        Set<ForeignKeyModel> foreignKeys = dbModel.getForeignKeys(INPUT_SCHEMA, DEFAULT_SCHEMA);

        // then
        Assertions.assertThat(foreignKeys).isEmpty();
    }

    @Nested
    @DisplayName("when reference in db")
    class ReferenceInDB {
        private Reference reference;
        private Table pkTable;
        private Table fkTable;

        @BeforeEach
        void setup() {
            pkTable = new Table().withAlternateKeys(new AlternateKeys());
            setTableSchema(pkTable, DEFAULT_SCHEMA);

            fkTable = new Table().withAlternateKeys(new AlternateKeys());
            setTableSchema(fkTable, DEFAULT_SCHEMA);

            reference = new Reference()
                    .withPKTable(pkTable)
                    .withFKTable(fkTable)
                    .withReferenceColumns(new ReferenceColumns());
            databaseModel.getReferences()
                    .withReference(reference);
            databaseModel.getTables()
                    .withTable(pkTable)
                    .withTable(fkTable);
        }

        void setTableSchema(Table t, String schema) {
            t.withProperties(buildSchemaProperties(schema));
        }

        void addTablePKColumn(Table t, String columnName) {
            Column column = new Column().withName(columnName);
            if (t.getPrimaryKey() == null) {
                t.withPrimaryKey(new PrimaryKey()
                        .withColumns(new PrimaryKeyColumns()));
            }
            t.getPrimaryKey().getColumns()
                    .withColumn(buildJAXBWithColumn(column));
        }

        void addTableAK(Table t, String akName, List<String> columnNames) {
            List<JAXBElement<Object>> jaxbElements = columnNames.stream()
                    .map(c -> new Column().withName(c))
                    .map(VerJoTestUtil::buildJAXBWithColumn)
                    .collect(Collectors.toList());

            AlternateKeyColumns akColumns = new AlternateKeyColumns()
                    .withColumn(jaxbElements);
            AlternateKey ak = new AlternateKey().withName(akName)
                    .withColumns(akColumns);
            t.getAlternateKeys().withAlternateKey(ak);
        }

        void addReferenceColumns(String pkColumn, String fkColumn) {
            ReferenceColumn referenceColumn = new ReferenceColumn()
                    .withPKColumn(new Column().withName(pkColumn))
                    .withFKColumn(new Column().withName(fkColumn));

            reference.getReferenceColumns().getReferenceColumn().add(referenceColumn);
        }

        @Test
        @DisplayName("but does not define reference columns then empty list is returned")
        void shouldReturnEmptyWhenReferenceDoesNotHaveColumns() {
            // given
            addTablePKColumn(pkTable, "col1");
            addTableAK(pkTable, "ak1", asList("col2", "col3"));

            // when
            Set<ForeignKeyModel> foreignKeys = dbModel.getForeignKeys(INPUT_SCHEMA, DEFAULT_SCHEMA);

            // then
            Assertions.assertThat(foreignKeys).isEmpty();
        }

        @Test
        @DisplayName("but PK table does not fit passed input schema")
        void shouldReturnEmptyWhenPkColumnDoesNotFitSchema() {
            // given
            addTablePKColumn(pkTable, "col1");
            addTableAK(pkTable, "ak1", asList("col2", "col3"));
            setTableSchema(pkTable, "other_fancy_schema");
            addReferenceColumns("col1", "coln");

            // when
            Set<ForeignKeyModel> foreignKeys = dbModel.getForeignKeys(INPUT_SCHEMA, DEFAULT_SCHEMA);

            // then
            Assertions.assertThat(foreignKeys).isEmpty();
        }

        @Nested
        @DisplayName("has two columns")
        class ReferenceWithTwoColumns {

            @BeforeEach
            void setup() {
                addReferenceColumns("RefCol1", "FkCol1");
                addReferenceColumns("RefCol2", "FkCol2");
            }

            @Test
            @DisplayName("but only one matches pkTable primary key columns then result is empty")
            void shouldReturnEmptyWhenNotAllPKColumnsMatchReference() {
                // given
                addTablePKColumn(pkTable, "RefCol1");
                addTablePKColumn(pkTable, "SomeOtherCol");

                // when
                Set<ForeignKeyModel> foreignKeys = dbModel.getForeignKeys(INPUT_SCHEMA, DEFAULT_SCHEMA);

                // then
                Assertions.assertThat(foreignKeys).isEmpty();
            }

            @Test
            @DisplayName("but only one matches pkTable alternate key columns then result is empty")
            void shouldReturnEmptyWhenNotAllAKColumnsMatchReference() {
                // given
                addTablePKColumn(pkTable, "SomeOtherCol");
                addTableAK(pkTable, "table_ak", asList("someFancyAKTable", "RefCol2"));
                addTableAK(pkTable, "table_ak", singletonList("RefCol2"));

                // when
                Set<ForeignKeyModel> foreignKeys = dbModel.getForeignKeys(INPUT_SCHEMA, DEFAULT_SCHEMA);

                // then
                Assertions.assertThat(foreignKeys).isEmpty();
            }

            @Test
            @DisplayName("primary key match is preferred over alternate key match")
            void primaryKeyHasPrecedenceOverAlternateKey() {
                // given
                addTablePKColumn(pkTable, "RefCol1");
                addTablePKColumn(pkTable, "RefCol2");
                pkTable.getPrimaryKey().withName("primary_key_name");
                reference.withName("reference_name");
                addTableAK(pkTable, "table_ak", asList("RefCol1", "RefCol2"));

                // when
                Set<ForeignKeyModel> foreignKeys = dbModel.getForeignKeys(INPUT_SCHEMA, DEFAULT_SCHEMA);

                // then
                Assertions.assertThat(foreignKeys).hasSize(1);

                ForeignKeyModel keyModel = foreignKeys.iterator().next();
                assertThat(keyModel.getFkTable().getTable()).isEqualTo(fkTable);
                assertThat(keyModel.getFkTableReferenceColumnNames()).containsExactly("FkCol1", "FkCol2");
                assertThat(keyModel.getForeignKeyName()).isEqualTo("reference_name");
                assertThat(keyModel.getUniqueKeyName()).isEqualTo("primary_key_name");
                assertThat(keyModel.getUniqueKeySchemaName()).isEqualTo(DEFAULT_SCHEMA);
            }

            @Test
            @DisplayName("returns alternate key when it's columns match")
            void alternateKeyIsChosen() {
                // given
                addTablePKColumn(pkTable, "RefCol1");
                addTablePKColumn(pkTable, "anotherColumn");
                addTableAK(pkTable, "table_ak", asList("RefCol1", "RefCol2"));
                reference.withName("reference_name");

                // when
                Set<ForeignKeyModel> foreignKeys = dbModel.getForeignKeys(INPUT_SCHEMA, DEFAULT_SCHEMA);

                // then
                Assertions.assertThat(foreignKeys).hasSize(1);

                ForeignKeyModel keyModel = foreignKeys.iterator().next();
                assertThat(keyModel.getFkTable().getTable()).isEqualTo(fkTable);
                assertThat(keyModel.getFkTableReferenceColumnNames()).containsExactly("FkCol1", "FkCol2");
                assertThat(keyModel.getForeignKeyName()).isEqualTo("reference_name");
                assertThat(keyModel.getUniqueKeyName()).isEqualTo("table_ak");
                assertThat(keyModel.getUniqueKeySchemaName()).isEqualTo(DEFAULT_SCHEMA);
            }
        }
    }
}
