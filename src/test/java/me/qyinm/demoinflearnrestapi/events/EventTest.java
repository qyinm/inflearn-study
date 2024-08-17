package me.qyinm.demoinflearnrestapi.events;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class EventTest {
    @Test
    public void builder() {
        Event event = Event.builder()
                .name("Inflearn Spring REST API")
                .description("REST API development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean() {
        // Given
        String name = "Event";
        String description = "Spring";

        // When
        Event event = new Event();
        event.setName(name);
        event.setDescription("Spring");

        // Then
        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @ParameterizedTest
//    @CsvSource(value = {
//            "0, 0, true",
//            "0, 100, false",
//            "100, 0, false",
//    })
//    @MethodSource("paramsForTestFree")
    @MethodSource // if factory method name is the same as the MethodSource method name, it can be omitted
    public void testFree(int basePrice, int maxPrice, boolean isFree) {
        Event.EventBuilder builder = Event.builder();
        builder.basePrice(basePrice);
        builder.maxPrice(maxPrice);
        Event event = builder
                .build();

        event.update();

        assertThat(event.isFree()).isEqualTo(isFree);
    }

//    private static Stream<Object> paramsForTestFree() {
    private static Stream<Object[]> testFree() {
        return Stream.of(
                new Object[] {0, 0, true},
                new Object[] {100, 0, false},
                new Object[] {0, 100, false},
                new Object[] {100, 200, false}
                );
    }

    @ParameterizedTest
    @MethodSource
    public void testOffline(String location, boolean isOffline) {
        // Given
        Event event = Event.builder()
                .location(location)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private static Stream<Object[]> testOffline() {
        return Stream.of(
                new Object[]{"강남역 네이버 D2 스타텁 팩토리", true},
                new Object[]{null, false},
                new Object[]{"    ", false}
        );
    }
}