package com.globema.interview;

import org.junit.Test;

import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class FlowIntegrationTest {

    private final PrintStream printStream = mock(PrintStream.class);
    private final JourneyService journeyService = new JourneyService(new FileReaderService(), printStream);

    @Test
    public void shouldPrintProperStopsForJourney() throws URISyntaxException {
        //given
        final String path = Paths.get(getClass().getClassLoader().getResource("routes/route.txt").toURI()).toString();

        //when
        journeyService.printJourney(path);

        //then
        verify(printStream).print("Szczecin-Poznań-Wrocław-Legnica-Opole-Kluczbork");
    }


    @Test
    public void shouldPrintProperStopsForLongerJourney() throws URISyntaxException {
        //given
        final String path = Paths.get(getClass().getClassLoader().getResource("routes/longerRoute.txt").toURI()).toString();

        //when
        journeyService.printJourney(path);

        //then
        verify(printStream).print("Radom-Puławy-Sandomierz-Kielce-Tarnów-Rzeszów-Krosno-Przemyśl-Zamość-Chełm-Lublin");
    }
}