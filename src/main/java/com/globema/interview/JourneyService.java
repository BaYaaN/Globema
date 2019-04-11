package com.globema.interview;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class JourneyService {

    private static final String DASH = "-";

    private final FileReaderService fileReaderService;
    private final PrintStream out;

    public JourneyService(FileReaderService fileReaderService, PrintStream out) {
        this.fileReaderService = fileReaderService;
        this.out = out;
    }

    public void printJourney(final String filePath) {
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