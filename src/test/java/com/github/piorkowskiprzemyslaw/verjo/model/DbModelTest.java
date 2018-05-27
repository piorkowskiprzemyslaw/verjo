package com.github.piorkowskiprzemyslaw.verjo.model;

import com.github.piorkowskiprzemyslaw.verjo.xsd.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("DbModel should")
class DbModelTest {
    private static final String DEFAULT_SCHEMA = "defaultSchema";

    private DatabaseModel xmlDatabaseModel;

    @BeforeEach
    void setup() {
        xmlDatabaseModel = new DatabaseModel()
                .withTables(new Tables())
                .withViews(new Views())
                .withSequences(new Sequences());
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
            DbModel dbModel = new DbModel(xmlDatabaseModel);

            // when
            Set<String> schemaNames = dbModel.getSchemaNames(DEFAULT_SCHEMA);

            // then
            assertThat(schemaNames).containsExactlyInAnyOrder("schema1", "schema2", "schema3");
        }

        @Test
        @DisplayName("set with defaultSchema when none of element defines custom getSchema")
        void shouldReturnDefaultSchema() {
            // given
            addTableWithSchema(null);
            addViewWithSchema(null);
            addSequenceWithSchema(null);
            DbModel dbModel = new DbModel(xmlDatabaseModel);

            // when
            Set<String> schemaNames = dbModel.getSchemaNames(DEFAULT_SCHEMA);

            // then
            assertThat(schemaNames).containsExactly(DEFAULT_SCHEMA);
        }

        @Test
        @DisplayName("empty set when database has no views, tables or sequences")
        void shouldReturnEmptySet() {
            // given
            DbModel dbModel = new DbModel(xmlDatabaseModel);

            // when
            Set<String> schemaNames = dbModel.getSchemaNames(DEFAULT_SCHEMA);

            // then
            assertThat(schemaNames).isEmpty();
        }
    }

    @Nested
    @DisplayName("reply with filtered tables stream")
    class FilterTables {

        private DbModel dbModel;

        @BeforeEach
        void setup() {
            addTableWithSchema("schema1");
            addTableWithSchema("schema2");
            dbModel = new DbModel(xmlDatabaseModel);
        }

        @Test
        @DisplayName("which is empty when input schema list is empty")
        void shouldReturnEmptyStream1() {
            // when
            Stream<TableModel> stream = dbModel.selectTables(emptyList(), DEFAULT_SCHEMA);

            // then
            assertEquals(stream.toArray().length, 0);
        }

        @Test
        @DisplayName("which is empty when no element fulfills filtering criteria")
        void shouldReturnEmptyStream2() {
            // when
            Stream<TableModel> stream = dbModel.selectTables(singletonList("schema3"), DEFAULT_SCHEMA);

            // then
            assertEquals(stream.toArray().length, 0);
        }

        @Test
        @DisplayName("which contains tables from given schemas")
        void shouldReturnFilteredTable() {
            // when
            Stream<TableModel> stream = dbModel.selectTables(singletonList("schema1"), DEFAULT_SCHEMA);

            // then
            List<String> schemas = stream.map(t -> t.getSchema(DEFAULT_SCHEMA)).collect(Collectors.toList());
            assertEquals(schemas, singletonList("schema1"));
        }
    }

    @Nested
    @DisplayName("reply with filtered views stream")
    class FilterViews {

        private DbModel dbModel;

        @BeforeEach
        void setup() {
            addViewWithSchema("viewSchema1");
            addViewWithSchema("viewSchema2");
            dbModel = new DbModel(xmlDatabaseModel);
        }

        @Test
        @DisplayName("which is empty when input schema list is empty")
        void shouldReturnEmptyStream1() {
            // when
            Stream<ViewModel> stream = dbModel.selectViews(emptyList(), DEFAULT_SCHEMA);

            // then
            assertEquals(stream.toArray().length, 0);
        }

        @Test
        @DisplayName("which is empty when no element fulfills filtering criteria")
        void shouldReturnEmptyStream2() {
            // when
            Stream<ViewModel> stream = dbModel.selectViews(singletonList("schema3"), DEFAULT_SCHEMA);

            // then
            assertEquals(stream.toArray().length, 0);
        }

        @Test
        @DisplayName("which contains views from given schemas")
        void shouldReturnFilteredTable() {
            // when
            Stream<ViewModel> stream = dbModel.selectViews(singletonList("viewSchema1"), DEFAULT_SCHEMA);

            // then
            List<String> schemas = stream.map(v -> v.getSchema(DEFAULT_SCHEMA)).collect(Collectors.toList());
            assertEquals(schemas, singletonList("viewSchema1"));
        }
    }

    @Nested
    @DisplayName("reply with filtered sequences stream")
    class FilterSequences {

        private DbModel dbModel;

        @BeforeEach
        void setup() {
            addSequenceWithSchema("sequenceSchema1");
            addSequenceWithSchema("sequenceSchema2");
            dbModel = new DbModel(xmlDatabaseModel);
        }

        @Test
        @DisplayName("which is empty when input schema list is empty")
        void shouldReturnEmptyStream1() {
            // when
            Stream<SequenceModel> stream = dbModel.selectSequences(emptyList(), DEFAULT_SCHEMA);

            // then
            assertEquals(stream.toArray().length, 0);
        }

        @Test
        @DisplayName("which is empty when no element fulfills filtering criteria")
        void shouldReturnEmptyStream2() {
            // when
            Stream<SequenceModel> stream = dbModel.selectSequences(singletonList("anotherSchema1"), DEFAULT_SCHEMA);

            // then
            assertEquals(stream.toArray().length, 0);
        }

        @Test
        @DisplayName("which contains views from given schemas")
        void shouldReturnFilteredStream() {
            // when
            Stream<SequenceModel> stream = dbModel.selectSequences(singletonList("sequenceSchema2"), DEFAULT_SCHEMA);

            // then
            List<String> schemas = stream.map(s -> s.getSchema(DEFAULT_SCHEMA)).collect(Collectors.toList());
            assertEquals(schemas, singletonList("sequenceSchema2"));
        }
    }

    private void addTableWithSchema(String schemaName) {
        xmlDatabaseModel.getTables().getTable()
                .add(new Table().withProperties(VerJoTestUtil.buildSchemaProperties(schemaName)));
    }

    private void addViewWithSchema(String schemaName) {
        xmlDatabaseModel.getViews().getView()
                .add(new View().withProperties(VerJoTestUtil.buildSchemaProperties(schemaName)));
    }

    private void addSequenceWithSchema(String schemaName) {
        xmlDatabaseModel.getSequences().getSequence()
                .add(new Sequence().withProperties(VerJoTestUtil.buildSchemaProperties(schemaName)));
    }

}