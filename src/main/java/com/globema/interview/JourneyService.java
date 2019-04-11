package com.globema.interview;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Logger;

import static com.google.common.collect.Iterables.getLast;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.logging.Level.FINE;
import static java.util.stream.Collectors.joining;

@AllArgsConstructor
public class JourneyService {

    private static final String DASH = "-";
    private static final Logger LOGGER = Logger.getLogger(JourneyService.class.getName());

    private final Function<Collection<String>, Boolean> hasLessThen2Elements = list -> list == null || list.size() < 2;
    private final Function<Collection<String>, String> findLast = list -> getLast(list);

    private final FileReaderService fileReaderService;
    private final PrintStream out;

    public void printJourney(@NonNull final String filePath) {
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

        final List<String> cities = newArrayList(firstCity);

        do {
            final String cityBefore = findOneBeforeLast(cities);
            final String currentCity = findLast.apply(cities);
            final List<String> possibleRoutes = citiesWithPossibleRoutes.get(currentCity);
            final String nextCity = possibleRoutes.stream().filter(x -> !x.equals(cityBefore)).findFirst().get();
            cities.add(nextCity);
        } while (!findLast.apply(cities).equals(lastCity));

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

    private String findOneBeforeLast(final List<String> collection) {
        if (hasLessThen2Elements.apply(collection)) {
            return null;
        }

        return collection.get(collection.size() - 2);
    }
}
