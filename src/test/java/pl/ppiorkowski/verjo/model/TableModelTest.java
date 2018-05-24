package pl.ppiorkowski.verjo.model;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static pl.ppiorkowski.verjo.model.VerJoTestUtil.buildJAXBWithColumn;

import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pl.ppiorkowski.verjo.model.table.AlternateKeyModel;
import pl.ppiorkowski.verjo.model.table.PrimaryKeyModel;
import pl.ppiorkowski.verjo.xsd.AlternateKey;
import pl.ppiorkowski.verjo.xsd.AlternateKeyColumns;
import pl.ppiorkowski.verjo.xsd.AlternateKeys;
import pl.ppiorkowski.verjo.xsd.Column;
import pl.ppiorkowski.verjo.xsd.PrimaryKey;
import pl.ppiorkowski.verjo.xsd.PrimaryKeyColumns;
import pl.ppiorkowski.verjo.xsd.Properties;
import pl.ppiorkowski.verjo.xsd.Table;

@DisplayName("Table model should")
class TableModelTest {

    private TableModel tableModel;
    private Table table;

    @BeforeEach
    void setup() {
        table = new Table()
                .withProperties(new Properties());
        tableModel = TableModel.of(table);
    }

    @Nested
    @DisplayName("return primary key data")
    class PrimaryKeyTest {

        @BeforeEach
        void setup() {
            PrimaryKey pk = new PrimaryKey()
                    .withColumns(new PrimaryKeyColumns())
                    .withName("pkName");
            table.withName("SampleTable")
                    .withPrimaryKey(pk);
        }

        void addPkColumn(String columnName) {
            Column col = new Column().withName(columnName);
            table.getPrimaryKey().getColumns()
                    .withColumn(buildJAXBWithColumn(col));
        }

        @Nested
        @DisplayName("with name")
        class Name {

            void setPkName(String name) {
                table.getPrimaryKey().withName(name);
            }

            @Test
            @DisplayName("set to value from xml model when not null")
            void shouldSetToValueFromXML() {
                // given
                setPkName("cool_pk_name");

                // when
                PrimaryKeyModel pkModel = tableModel.getPrimaryKey();

                // then
                assertEquals("cool_pk_name", pkModel.getName());
            }

            @Test
            @DisplayName("set to generated value when model property is null")
            void shouldSetToDefaultValue() {
                // given
                setPkName(null);

                // when
                PrimaryKeyModel pkModel = tableModel.getPrimaryKey();

                // then
                assertEquals("SampleTable_PK", pkModel.getName());
            }
        }

        @Nested
        @DisplayName("with columns")
        class Columns {

            @Test
            @DisplayName("list containing all column names defined in xml model")
            void emptyColumnsList() {
                // given
                addPkColumn("pkColumn1");
                addPkColumn("pkColumn2");

                // when
                PrimaryKeyModel pkModel = tableModel.getPrimaryKey();

                // then
                assertIterableEquals(asList("pkColumn1", "pkColumn2"), pkModel.getColumnNames());
            }
        }
    }

    @Nested
    @DisplayName("return alternate keys data")
    class AlternateKeyTest {

        @BeforeEach
        void setup() {
            table.withAlternateKeys(new AlternateKeys());
        }

        void addAlternateKey(String name, List<String> columns) {
            List<JAXBElement<Object>> akColumns = columns.stream()
                    .map(colName -> new Column().withName(colName))
                    .map(VerJoTestUtil::buildJAXBWithColumn)
                    .collect(Collectors.toList());

            AlternateKey ak = new AlternateKey().withName(name)
                    .withColumns(new AlternateKeyColumns().withColumn(akColumns));

            table.getAlternateKeys().withAlternateKey(ak);
        }

        void addAlternateKey(String name) {
            addAlternateKey(name, emptyList());
        }

        @Test
        void shouldReturnEmptyListWhenNoAlternateKeys() {
            // when
            List<AlternateKeyModel> alternateKeys = tableModel.getAlternateKeys();

            // then
            assertEquals(0, alternateKeys.size());
        }

        @Nested
        @DisplayName("with name")
        class Name {

            @BeforeEach
            void setup() {
                table.withName("fancy_table");
            }

            @Test
            @DisplayName("set to value defined in xml model")
            void setToValueDefinedInModel() {
                // given
                addAlternateKey("ak1");

                // when
                List<AlternateKeyModel> alternateKeys = tableModel.getAlternateKeys();

                // then
                assertEquals(1, alternateKeys.size());
                assertEquals("ak1", alternateKeys.get(0).getName());
            }
        }

        @Nested
        @DisplayName("with columns")
        class Columns {

            @Test
            @DisplayName("containing all column names defined in xml")
            void shouldReturnAllAlternateKeyColumns() {
                // given
                addAlternateKey("ak1", singletonList("ak1c1"));
                addAlternateKey("ak2");

                // when
                List<AlternateKeyModel> alternateKeys = tableModel.getAlternateKeys();

                // then
                assertEquals(2, alternateKeys.size());
                AlternateKeyModel ak1 = alternateKeys.get(0);
                assertEquals("ak1", ak1.getName());
                assertIterableEquals(singletonList("ak1c1"), ak1.getColumnNames());

                AlternateKeyModel ak2 = alternateKeys.get(1);
                assertEquals("ak2", ak2.getName());
                assertIterableEquals(emptyList(), ak2.getColumnNames());
            }
        }
    }
}