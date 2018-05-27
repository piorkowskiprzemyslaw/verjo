package com.github.piorkowskiprzemyslaw.verjo.collection;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

class CollectionEqualityTest {

    private static Stream<Arguments> compareSource() {
        return Stream.of(
                Arguments.of(asList("a", "b"), asList("b", "a"), true),
                Arguments.of(asList("a", "b"), asList("a", "b"), true),
                Arguments.of(null, asList("a", "b"), false),
                Arguments.of(asList("a", "b"), null, false),
                Arguments.of(null, null, false),
                Arguments.of(asList("a", "b", "c"), asList("a" , "b"), false),
                Arguments.of(asList("a" , "b"), asList("a", "b", "c"), false),
                Arguments.of(asList("a" , "b"), asList("a", "c"), false)
        );
    }

    @ParameterizedTest
    @MethodSource("compareSource")
    void equalTest(Collection<String> c1, Collection<String> c2, boolean expected) {
        // when
        boolean equals = CollectionEquality.equals(c1, c2);

        // then
        assertThat(equals).isEqualTo(expected);
    }

}