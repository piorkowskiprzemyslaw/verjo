package pl.ppiorkowski.verjo.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.ppiorkowski.verjo.xsd.Sequence;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class SequenceModel extends ModelWithProperties {

    private static final String SCHEMA_PROPERTY_NAME = "schema";

    private final Sequence sequence;

    public static SequenceModel of(Sequence sequence) {
        return new SequenceModel(sequence);
    }

    public String getSchema(String defaultSchema) {
        return getPropertyValue(SCHEMA_PROPERTY_NAME, sequence.getProperties())
                .orElse(defaultSchema);
    }

    public String getName() {
        return sequence.getName();
    }

}
