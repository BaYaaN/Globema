package com.globema.interview;

import org.junit.Test;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class JourneyServiceTest {

    private final FileReaderService fileReaderService = mock(FileReaderService.class);
    private final PrintStream printStream = mock(PrintStream.class);
    private final JourneyService journeyService = new JourneyService(fileReaderService, printStream);

    @Test
    public void shouldThrowNpeWhenPathIsNull() {
        //when
        assertThatThrownBy(() -> journeyService.printJourney(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    public void shouldInvokePrintExactlyOneTimes() {
        String testPath = "TestPath";
        Map<String, List<String>> cities = Map.of("Szczecin", List.of("Poznań"),
                "Poznań", List.of("Szczecin", "Wrocław"),
                "Wrocław", List.of("Poznań", "Legnica"),
                "Legnica", List.of("Opole", "Wrocław"),
                "Opole", List.of("Legnica", "Kluczbork"),
                "Kluczbork", List.of("Opole"));
        given(fileReaderService.findCitiesWithPreviousAndNextStop(testPath)).willReturn(cities);

        //when
        journeyService.printJourney(testPath);

        //then
        verify(printStream, times(1)).print(anyString());
    }
}
