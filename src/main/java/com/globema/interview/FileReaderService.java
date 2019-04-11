package com.globema.interview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static java.nio.file.Files.lines;
import static java.nio.file.Paths.get;
import static java.util.logging.Level.FINE;
import static java.util.logging.Level.SEVERE;

public class FileReaderService {

    private static final String DASH = "-";
    private static final Logger LOGGER = Logger.getLogger(FileReaderService.class.getName());

    public Map<String, List<String>> findCitiesWithPossibleRoutes(final String filePath) {
        LOGGER.log(FINE, "Start processing with path {}", filePath);

        final Map<String, List<String>> citiesWithPossibleRoutes = new HashMap<>();

        try (Stream<String> stream = lines(get(filePath))) {
            stream
                    .skip(1)
                    .forEach(line -> {
                        final String[] split = line.split(DASH);
                        citiesWithPossibleRoutes.computeIfAbsent(split[0], empList -> new ArrayList<>()).add(split[1]);
                        citiesWithPossibleRoutes.computeIfAbsent(split[1], empList -> new ArrayList<>()).add(split[0]);
                    });
        } catch (IOException e) {
            LOGGER.log(SEVERE, "Can not read from file with path {0}", filePath);
            throw new RuntimeException("Something went wrong during file processing", e);
        }

        LOGGER.log(FINE, "Successful read file from path {}. Amount of cities: {}", filePath);

        return citiesWithPossibleRoutes;
    }
}
