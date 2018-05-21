package pl.ppiorkowski.verjo.model;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.ppiorkowski.verjo.xsd.View;

import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ViewModel extends ModelWithProperties {

    private static final String SCHEMA_PROPERTY_NAME = "schema";

    private final View view;

    public static ViewModel of(View view) {
        return new ViewModel(view);
    }

    public Optional<String> schema() {
        return getPropertyValue(SCHEMA_PROPERTY_NAME, view.getProperties());
    }

    public String name() {
        return view.getName();
    }
}
