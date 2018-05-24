package pl.ppiorkowski.verjo.model;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static pl.ppiorkowski.verjo.model.VerJoTestUtil.asSet;
import static pl.ppiorkowski.verjo.model.VerJoTestUtil.buildJAXBWithColumn;
import static pl.ppiorkowski.verjo.model.VerJoTestUtil.buildSchemaProperties;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.bind.JAXBElement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import pl.ppiorkowski.verjo.xsd.AlternateKey;
import pl.ppiorkowski.verjo.xsd.AlternateKeyColumns;
import pl.ppiorkowski.verjo.xsd.AlternateKeys;
import pl.ppiorkowski.verjo.xsd.Column;
import pl.ppiorkowski.verjo.xsd.PrimaryKey;
import pl.ppiorkowski.verjo.xsd.PrimaryKeyColumns;
import pl.ppiorkowski.verjo.xsd.Reference;
import pl.ppiorkowski.verjo.xsd.ReferenceColumn;
import pl.ppiorkowski.verjo.xsd.ReferenceColumns;
import pl.ppiorkowski.verjo.xsd.Table;

class ReferenceModelTest {

    private Reference xmlReference;
    private ReferenceModel referenceModel;
    private Table pkTable;
    private Table fkTable;

    @BeforeEach
    void setup() {
        pkTable = new Table().withAlternateKeys(new AlternateKeys());
        fkTable = new Table().withAlternateKeys(new AlternateKeys());
        xmlReference = new Reference()
                .withPKTable(pkTable)
                .withFKTable(fkTable)
                .withReferenceColumns(new ReferenceColumns());
        referenceModel = ReferenceModel.of(xmlReference);
    }

    private void setTableSchema(Table t, String schemaName) {
        t.withProperties(buildSchemaProperties(schemaName));
    }

    private void setPkSchema(String schemaName) {
        setTableSchema(pkTable, schemaName);
    }

    private void setFkSchema(String schemaName) {
        setTableSchema(fkTable, schemaName);
    }

    @ParameterizedTest
    @MethodSource("isInAllowedSchemaSource")
    void isInAllowedSchemas(String pkSchema, String fkSchema, String defaultSchema, Set<String> allowedSchemas,
            boolean expected) {
        // given
        setPkSchema(pkSchema);
        setFkSchema(fkSchema);

        // when
        boolean referenceInSchema = referenceModel.isReferenceInSchema(defaultSchema, allowedSchemas);

        // then
        assertThat(referenceInSchema).isEqualTo(expected);
    }

    private static Stream<Arguments> isInAllowedSchemaSource() {
        return Stream.of(
                Arguments.of("schema1", "schema2", "", asSet("schema1", "schema2"), true),
                Arguments.of("schema1", "schema2", "", asSet("schema1"), false),
                Arguments.of("schema1", "schema2", "", asSet("schema2"), false),
                Arguments.of("schema1", "schema2", "", asSet("customSchema1", "customSchema2"), false),
                Arguments.of(null, "schema2", "schema1", asSet("schema1", "schema2"), true),
                Arguments.of(null, "schema2", "schema", asSet("schema1", "schema2"), false),
                Arguments.of("schema1", null, "schema2", asSet("schema1", "schema2"), true),
                Arguments.of("schema1", null, "schema", asSet("schema1", "schema2"), false),
                Arguments.of(null, null, "schema", asSet("schema"), true),
                Arguments.of(null, null, "schema", asSet("schema1"), false)
        );
    }

    @Nested
    class UniqueKeyName {

        @BeforeEach
        void setup() {
            pkTable.withName("pkTable");
            fkTable.withName("fkTable");
            addReferenceColumns("refCol1", "fkCol1");
            addReferenceColumns("refCol2", "fkCol2");
        }

        void addReferenceColumns(String pkColumn, String fkColumn) {
            ReferenceColumn referenceColumn = new ReferenceColumn()
                    .withPKColumn(new Column().withName(pkColumn))
                    .withFKColumn(new Column().withName(fkColumn));

            xmlReference.getReferenceColumns().getReferenceColumn().add(referenceColumn);
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

        @Test
        void shouldReturnEmptyWhenNotAllPKColumnsMatch() {
            // given
            addTablePKColumn(pkTable, "refCol1");
            addTablePKColumn(pkTable, "anotherCol1");

            // when
            Optional<String> opt = referenceModel.getUniqueKeyName();

            // then
            assertThat(opt).isEmpty();
        }

        @Test
        void shouldReturnEmptyWhenNotAllAKColumnsMatch() {
            // given
            addTablePKColumn(pkTable, "anotherCol1");
            addTableAK(pkTable, "ak_table", asList("boringCol1", "refCol2"));

            // when
            Optional<String> opt = referenceModel.getUniqueKeyName();

            // then
            assertThat(opt).isEmpty();
        }

        @Test
        void shouldPreferPKMatchOverAKMatch() {
            // given
            addTablePKColumn(pkTable, "refCol1");
            addTablePKColumn(pkTable, "refCol2");
            addTableAK(pkTable, "ak_table", asList("boringCol1", "refCol2"));

            // when
            Optional<String> opt = referenceModel.getUniqueKeyName();

            // then
            assertThat(opt).hasValue("pkTable_PK");
        }

        @Test
        void shouldReturnAKMatch() {
            // given
            addTablePKColumn(pkTable, "refCol1");
            addTableAK(pkTable, "ak_table", asList("refCol1", "refCol2"));

            // when
            Optional<String> opt = referenceModel.getUniqueKeyName();

            // then
            assertThat(opt).hasValue("ak_table");
        }
    }
}