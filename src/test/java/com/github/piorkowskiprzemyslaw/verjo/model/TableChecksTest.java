package com.github.piorkowskiprzemyslaw.verjo.model;

import com.github.piorkowskiprzemyslaw.verjo.model.table.ColumnCheckModel;
import com.github.piorkowskiprzemyslaw.verjo.model.table.TableCheckModel;
import com.github.piorkowskiprzemyslaw.verjo.xsd.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Table model should")
class TableChecksTest {

    private TableModel tableModel;
    private Table table;

    @BeforeEach
    void setup() {
        table = new Table()
                .withProperties(new Properties())
                .withTableChecks(new TableChecks());
        tableModel = TableModel.of(table);
    }

    @Nested
    @DisplayName("return table checks")
    class TableCheckTest {

        private void addTableCheck(String checkName, String checkExpr) {
            TableCheck tc = new TableCheck()
                    .withName(checkName)
                    .withCheckExpression(checkExpr);
            table.getTableChecks().withTableCheck(tc);
        }

        @Test
        @DisplayName("with not null check expression")
        void shouldReturnChecksWithNotNullExpression() {
            // given
            addTableCheck("ch1", null);

            // when
            List<TableCheckModel> tableChecks = tableModel.getTableChecks();

            // then
            assertTrue(tableChecks.isEmpty());
        }

        @Test
        @DisplayName("for all table checks definition")
        void shouldReturnAllChecks() {
            // given
            addTableCheck("ch1", "chExpr1");
            addTableCheck("ch2", "");

            // when
            List<TableCheckModel> tableChecks = tableModel.getTableChecks();

            // then
            assertEquals(2, tableChecks.size());

            TableCheckModel firstTC = tableChecks.get(0);
            assertEquals("ch1", firstTC.getName());
            assertEquals("chExpr1", firstTC.getCheckExpression());

            TableCheckModel secondTC = tableChecks.get(1);
            assertEquals("ch2", secondTC.getName());
            assertEquals("", secondTC.getCheckExpression());
        }
    }

    @Nested
    @DisplayName("return column checks")
    class ColumnCheckTest {

        @BeforeEach
        void setup() {
            table.withName("TableName").withColumns(new Columns());
        }

        void addColumnWithCheck(String columnName, String checkExpr) {
            Column c = new Column().withName(columnName).withCheckExpression(checkExpr);
            table.getColumns().withColumn(c);
        }

        @Test
        @DisplayName("for all column checks definitions")
        void shouldReturnAllColumnChecks() {
            // given
            addColumnWithCheck("col1", "colCheck1");
            addColumnWithCheck("col2", "");

            // when
            List<ColumnCheckModel> columnChecks = tableModel.getColumnChecks();

            // then
            assertEquals(2, columnChecks.size());

            ColumnCheckModel firstCC = columnChecks.get(0);
            assertEquals("TableName_col1_check", firstCC.getName());
            assertEquals("colCheck1", firstCC.getCheckExpression());

            ColumnCheckModel secondCC = columnChecks.get(1);
            assertEquals("TableName_col2_check", secondCC.getName());
            assertEquals("", secondCC.getCheckExpression());
        }

        @Test
        @DisplayName("with not null check expression")
        void shouldNotReturnColumnChecksWithNullCheckExpression() {
            // given
            addColumnWithCheck("col3", null);

            // when
            List<ColumnCheckModel> columnChecks = tableModel.getColumnChecks();

            // then
            assertTrue(columnChecks.isEmpty());
        }
    }
}
