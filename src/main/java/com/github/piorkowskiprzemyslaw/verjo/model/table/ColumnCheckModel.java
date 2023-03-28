package com.github.piorkowskiprzemyslaw.verjo.model.table;

import com.github.piorkowskiprzemyslaw.verjo.xsd.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ColumnCheckModel {
    String name;
    String checkExpression;

    public ColumnCheckModel(String tableName, Column column) {
        name = String.format("%s_%s_check", tableName, column.getName());
        checkExpression = column.getCheckExpression();
    }

    public boolean isMeaningful() {
        return checkExpression != null;
    }
}
