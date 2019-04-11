package com.globema.interview;

import lombok.SneakyThrows;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FileReaderServiceTest {

    private final FileReaderService fileReaderService = new FileReaderService();


    @Test
    public void shouldThrowNpeWhenPathIsNull() {
        //when
        assertThatThrownBy(() -> fileReaderService.findCitiesWithPreviousAndNextStop(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @SneakyThrows
    public void shouldPrintProperStopsForJourney() {
        //given
        String path = Paths.get(getClass().getClassLoader().getResource("routes/route.txt").toURI()).toString();

        //when
        Map<String, List<String>> result = fileReaderService.findCitiesWithPreviousAndNextStop(path);

        //then
        assertThat(result.get("Szczecin")).containsExactlyInAnyOrder("Poznań");
        assertThat(result.get("Poznań")).containsExactlyInAnyOrder("Wrocław", "Szczecin");
        assertThat(result.get("Wrocław")).containsExactlyInAnyOrder("Legnica", "Poznań");
        assertThat(result.get("Legnica")).containsExactlyInAnyOrder("Opole", "Wrocław");
        assertThat(result.get("Opole")).containsExactlyInAnyOrder("Kluczbork", "Legnica");
        assertThat(result.get("Kluczbork")).containsExactlyInAnyOrder("Opole");
    }
}