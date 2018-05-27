package com.github.piorkowskiprzemyslaw.verjo.model;

import com.github.piorkowskiprzemyslaw.verjo.xsd.Column;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

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
