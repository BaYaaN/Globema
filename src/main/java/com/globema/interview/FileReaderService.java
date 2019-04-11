package com.globema.interview;

import lombok.NonNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.nio.file.Files.lines;
import static java.nio.file.Paths.get;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;

public class FileReaderService {

    private static final String DASH = "-";
    private static final Logger LOGGER = Logger.getLogger(FileReaderService.class.getName());

    public Map<String, List<String>> findCitiesWithPreviousAndNextStop(@NonNull final String filePath) {
        LOGGER.log(FINE, "Start processing with path {}", filePath);

        Map<String, List<String>> citiesWithPossibleRoutes = newHashMap();

        try (Stream<String> stream = lines(get(filePath))) {
            stream
                    .skip(1)
                    .forEach(line -> {
                        String[] cities = line.split(DASH);
                        citiesWithPossibleRoutes.computeIfAbsent(cities[0], empList -> newArrayList()).add(cities[1]);
                        citiesWithPossibleRoutes.computeIfAbsent(cities[1], empList -> newArrayList()).add(cities[0]);
                    });
        } catch (IOException e) {
            LOGGER.log(SEVERE, "Can not read from file with path {0}", filePath);
            throw new RuntimeException("Something went wrong during file processing", e);
        }

        LOGGER.log(FINE, "Successful read file from path {}", filePath);

        return citiesWithPossibleRoutes;
    }
}
