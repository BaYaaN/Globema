package com.globema.interview;

import lombok.SneakyThrows;
import org.junit.Test;

import java.io.PrintStream;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FlowIntegrationTest {

    private final PrintStream printStream = mock(PrintStream.class);
    private final JourneyService journeyService = new JourneyService(new FileReaderService(), printStream);

    @Test
    @SneakyThrows
    public void shouldPrintProperStopsForJourney() {
        //given
        String path = Paths.get(getClass().getClassLoader().getResource("routes/route.txt").toURI()).toString();

        //when
        journeyService.printJourney(path);

        //then
        verify(printStream).print("Szczecin-Poznań-Wrocław-Legnica-Opole-Kluczbork");
    }


    @Test
    @SneakyThrows
    public void shouldPrintProperStopsForLongerJourney() {
        //given
        String path = Paths.get(getClass().getClassLoader().getResource("routes/longerRoute.txt").toURI()).toString();

        //when
        journeyService.printJourney(path);

        //then
        verify(printStream).print("Radom-Puławy-Sandomierz-Kielce-Tarnów-Rzeszów-Krosno-Przemyśl-Zamość-Chełm-Lublin");
    }
}