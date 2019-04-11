package com.globema.interview;

import lombok.AllArgsConstructor;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.logging.Level.FINE;
import static java.util.stream.Collectors.joining;

@AllArgsConstructor
public class JourneyService {

    private static final String DASH = "-";
    private static final Logger LOGGER = Logger.getLogger(JourneyService.class.getName());

    private final FileReaderService fileReaderService;
    private final PrintStream out;

    public void printJourney(final String filePath) {
        LOGGER.log(FINE, "Start printing stops from path {}", filePath);

        out.print(findStops(filePath)
                .stream()
                .collect(joining(DASH)));
    }

    private List<String> findStops(final String filePath) {
        final Map<String, List<String>> citiesWithPossibleRoutes = fileReaderService.findCitiesWithPossibleRoutes(filePath);

        final String[] edgeCities = findEdgeCities(citiesWithPossibleRoutes);
        final String firstCity = edgeCities[0];
        final String lastCity = edgeCities[1];

        final List<String> cities = new ArrayList<>();
        cities.add(firstCity);

        do {
            final String cityBefore = findOneBeforeLast(cities);
            final String currentCity = findLast(cities);
            final List<String> possibleRoutes = citiesWithPossibleRoutes.get(currentCity);
            final String nextCity = possibleRoutes.stream().filter(x -> !x.equals(cityBefore)).findFirst().get();
            cities.add(nextCity);
        } while (!findLast(cities).equals(lastCity));

        return cities;
    }

    private String[] findEdgeCities(final Map<String, List<String>> citiesWithPossibleRoutes) {
        return citiesWithPossibleRoutes
                .entrySet()
                .stream()
                .filter(x -> x.getValue().size() == 1)
                .map(x -> x.getKey())
                .toArray(size -> new String[size]);
    }

    private String findLast(final List<String> collection) {
        if (collection == null || collection.size() == 0) {
            return null;
        }

        return collection.get(collection.size() - 1);
    }

    private String findOneBeforeLast(final List<String> collection) {
        if (collection == null || collection.size() < 2) {
            return null;
        }

        return collection.get(collection.size() - 2);
    }
}
