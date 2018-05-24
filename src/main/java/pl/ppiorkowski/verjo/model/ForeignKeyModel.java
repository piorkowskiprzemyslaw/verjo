package pl.ppiorkowski.verjo.model;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Value;
import pl.ppiorkowski.verjo.xsd.Column;

@Value
@Builder
public class ForeignKeyModel {
    private String uniqueKeySchemaName;
    private String uniqueKeyName;
    private TableModel fkTable;
    private List<Column> fkTableReferenceColumns;
    private String foreignKeyName;

    public String getFkTableName() {
        return getFkTable().getName();
    }

    public List<String> getFkTableReferenceColumnNames() {
        return fkTableReferenceColumns.stream()
                .map(Column::getName)
                .collect(Collectors.toList());
    }
}
