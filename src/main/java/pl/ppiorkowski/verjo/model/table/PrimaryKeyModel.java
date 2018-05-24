package pl.ppiorkowski.verjo.model.table;

import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class PrimaryKeyModel {
    private String name;
    @Singular
    private List<String> columnNames;
}
