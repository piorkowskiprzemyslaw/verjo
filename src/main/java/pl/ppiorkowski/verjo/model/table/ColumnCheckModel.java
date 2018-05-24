package pl.ppiorkowski.verjo.model.table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import pl.ppiorkowski.verjo.xsd.Column;

@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ColumnCheckModel {
    private String name;
    private String checkExpression;

    public ColumnCheckModel(String tableName, Column column) {
        name = String.format("%s_%s_check", tableName, column.getName());
        checkExpression = column.getCheckExpression();
    }

    public boolean isMeaningful() {
        return checkExpression != null;
    }
}
