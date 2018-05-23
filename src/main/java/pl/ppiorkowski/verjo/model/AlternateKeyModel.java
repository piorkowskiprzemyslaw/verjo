package pl.ppiorkowski.verjo.model;

import java.util.List;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Value
@Builder
public class AlternateKeyModel {
    private String name;
    @Singular
    private List<String> columns;
}
