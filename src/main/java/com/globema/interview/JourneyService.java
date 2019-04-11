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
    private static final int PREVIOUS_CITY_INDEX = 0;
    private static final int CURRENT_CITY_INDEX = 1;
    private static final int FIRST_CITY_INDEX = 0;
    private static final int LAST_CITY_INDEX = 1;

    private final FileReaderService fileReaderService;
    private final PrintStream out;

    public void printJourney(@NonNull final String filePath) {
        LOGGER.log(FINE, "Start printing stops from path {}", filePath);

        Map<String, List<String>> citiesWithPreviousAndNextStops = fileReaderService.findCitiesWithPreviousAndNextStop(filePath);

        String[] edgeCities = findEdgeCities(citiesWithPreviousAndNextStops);
        String firstCity = edgeCities[FIRST_CITY_INDEX];
        String lastCity = edgeCities[LAST_CITY_INDEX];

        String[] previousAndCurrentCity = new String[2];
        previousAndCurrentCity[1] = firstCity;

        StringBuilder sb = new StringBuilder(firstCity);

        do {
            String previousCity = previousAndCurrentCity[PREVIOUS_CITY_INDEX];
            String currentCity = previousAndCurrentCity[CURRENT_CITY_INDEX];
            List<String> possibleCities = citiesWithPreviousAndNextStops.get(currentCity);
            String nextCity = findNextCity(possibleCities, previousCity);

            previousAndCurrentCity[PREVIOUS_CITY_INDEX] = currentCity;
            previousAndCurrentCity[CURRENT_CITY_INDEX] = nextCity;

            sb.append(DASH).append(nextCity);
        } while (!previousAndCurrentCity[CURRENT_CITY_INDEX].equals(lastCity));

        out.print(sb.toString());

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
