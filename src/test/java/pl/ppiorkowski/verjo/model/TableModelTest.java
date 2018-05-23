package pl.ppiorkowski.verjo.model;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
    @DisplayName("return PK data")
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
            QName qName = new QName("nsURI", "localPart");
            table.getPrimaryKey().getColumns()
                    .withColumn(new JAXBElement<>(qName, Object.class, col));
        }

        @Nested
        @DisplayName("with name")
        class NameValue {

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
        class PkColumns {

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
}