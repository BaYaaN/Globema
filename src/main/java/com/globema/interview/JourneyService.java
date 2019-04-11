package com.globema.interview;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;

@AllArgsConstructor
public class JourneyService {

    private static final String DASH = "-";
    private static final Logger LOGGER = Logger.getLogger(JourneyService.class.getName());

    private final FileReaderService fileReaderService;
    private final PrintStream out;

    public void printJourney(@NonNull final String filePath) {
        LOGGER.log(FINE, "Start printing stops from path {}", filePath);

        Map<String, List<String>> citiesWithPreviousAndNextStops = fileReaderService.findCitiesWithPreviousAndNextStop(filePath);

        String[] edgeCities = findEdgeCities(citiesWithPreviousAndNextStops);
        String firstCity = edgeCities[0];
        String lastCity = edgeCities[1];

        String[] previousAndCurrentCity = new String[2];
        previousAndCurrentCity[1] = firstCity;

        StringBuilder stringBuilder = new StringBuilder(firstCity);

        do {
            String previousCity = previousAndCurrentCity[0];
            String currentCity = previousAndCurrentCity[1];
            List<String> possibleCities = citiesWithPreviousAndNextStops.get(currentCity);
            String nextCity = findNextCity(possibleCities, previousCity);

            previousAndCurrentCity[0] = currentCity;
            previousAndCurrentCity[1] = nextCity;

            stringBuilder.append(DASH).append(nextCity);
        } while (!previousAndCurrentCity[1].equals(lastCity));

        out.print(stringBuilder.toString());

        LOGGER.log(FINE, "Finnish printing");
    }

    private String[] findEdgeCities(final Map<String, List<String>> citiesWithPossibleRoutes) {
        return citiesWithPossibleRoutes
                .entrySet()
                .stream()
                .filter(x -> x.getValue().size() == 1)
                .map(x -> x.getKey())
                .toArray(size -> new String[size]);
    }

    private String findNextCity(final List<String> previousAndCurrentCity, final String previousCity) {
        if (previousAndCurrentCity.size() == 1) {
            return previousAndCurrentCity.get(0);
        }

        String firstCity = previousAndCurrentCity.get(0);
        String secondCity = previousAndCurrentCity.get(1);

        return firstCity.equals(previousCity) ? secondCity : firstCity;
    }
}
