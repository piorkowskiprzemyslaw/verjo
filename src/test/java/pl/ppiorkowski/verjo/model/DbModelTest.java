package pl.ppiorkowski.verjo.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import pl.ppiorkowski.verjo.xsd.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static pl.ppiorkowski.verjo.model.DbModel.SCHEMA_PROPERTY_NAME;

@DisplayName("DbModel should")
class DbModelTest {

    private DatabaseModel databaseModel;

    @BeforeEach
    void setup() {
        databaseModel = new DatabaseModel()
                .withTables(new Tables())
                .withViews(new Views());
    }

    @Nested
    @DisplayName("reply with schemas")
    class SchemaNames {

        @Test
        @DisplayName("which names are unique")
        void shouldReturnSchemeNames() {
            // given
            addTableWithSchema("schema1");
            addViewWithSchema("schema1");
            addTableWithSchema("schema2");
            addViewWithSchema("schema3");
            DbModel dbModel = new DbModel(databaseModel);

            // when
            Set<String> schemaNames = dbModel.getSchemaNames();

            // then
            assertEquals(schemaNames.size(), 3);
            assertTrue(schemaNames.containsAll(Arrays.asList("schema1", "schema2", "schema3")));
        }

        @Test
        @DisplayName("set which is empty when none of element defines custom schema")
        void shouldReturnEmptySet() {
            // given
            DbModel dbModel = new DbModel(databaseModel);

            // when
            Set<String> schemaNames = dbModel.getSchemaNames();

            // then
            assertIterableEquals(schemaNames, emptyList());
        }
    }

    @Nested
    @DisplayName("reply with filtered tables stream")
    class FilterTables {

        @BeforeEach
        void setup() {
            addTableWithSchema("schema1");
            addTableWithSchema("schema2");
        }

        @Test
        @DisplayName("which is empty when input schema list is empty")
        void shouldReturnEmptyStream1() {
            // given
            DbModel dbModel = new DbModel(databaseModel);

            // when
            Stream<TableModel> stream = dbModel.selectTables(emptyList());

            // then
            assertEquals(stream.toArray().length, 0);
        }

        @Test
        @DisplayName("which is empty when no element fulfills filtering criteria")
        void shouldReturnEmptyStream2() {
            // given
            DbModel dbModel = new DbModel(databaseModel);

            // when
            Stream<TableModel> stream = dbModel.selectTables(singletonList("schema3"));

            // then
            assertEquals(stream.toArray().length, 0);
        }

        @Test
        @DisplayName("which contains tables from given schemas")
        void shouldReturnFilteredTable() {
            // given
            DbModel dbModel = new DbModel(databaseModel);

            // when
            Stream<TableModel> stream = dbModel.selectTables(singletonList("schema1"));

            // then
            List<Optional<String>> schemas = stream.map(TableModel::schema).collect(Collectors.toList());
            assertEquals(schemas, singletonList(Optional.of("schema1")));
        }
    }

    @Nested
    @DisplayName("reply with filtered views stream")
    class FilterViews {

        @BeforeEach
        void setup() {
            addViewWithSchema("schema1");
            addViewWithSchema("schema2");
        }

        @Test
        @DisplayName("which is empty when input schema list is empty")
        void shouldReturnEmptyStream1() {
            // given
            DbModel dbModel = new DbModel(databaseModel);

            // when
            Stream<ViewModel> stream = dbModel.selectViews(emptyList());

            // then
            assertEquals(stream.toArray().length, 0);
        }

        @Test
        @DisplayName("which is empty when no element fulfills filtering criteria")
        void shouldReturnEmptyStream2() {
            // given
            DbModel dbModel = new DbModel(databaseModel);

            // when
            Stream<ViewModel> stream = dbModel.selectViews(singletonList("schema3"));

            // then
            assertEquals(stream.toArray().length, 0);
        }

        @Test
        @DisplayName("which contains views from given schemas")
        void shouldReturnFilteredTable() {
            // given
            DbModel dbModel = new DbModel(databaseModel);

            // when
            Stream<ViewModel> stream = dbModel.selectViews(singletonList("schema1"));

            // then
            List<Optional<String>> schemas = stream.map(ViewModel::schema).collect(Collectors.toList());
            assertEquals(schemas, singletonList(Optional.of("schema1")));
        }
    }

    private void addTableWithSchema(String schemaName) {
        databaseModel.getTables().getTable()
                .add(new Table().withProperties(buildSchemaProperties(schemaName)));
    }

    private void addViewWithSchema(String schemaName) {
        databaseModel.getViews().getView()
                .add(new View().withProperties(buildSchemaProperties(schemaName)));
    }

    private Properties buildSchemaProperties(String schemaName) {
        return new Properties()
                .withProperty(new Property()
                        .withName(SCHEMA_PROPERTY_NAME)
                        .withValue(schemaName));
    }

}