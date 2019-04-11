package com.globema.interview;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.nio.file.Files.lines;
import static java.nio.file.Paths.get;

public class FileReaderService {

    //TODO: add logging
    private static final String DASH = "-";

    public Map<String, List<String>> findCitiesWithPossibleRoutes(final String filePath) {
        final Map<String, List<String>> citiesWithPossibleRoutes = new HashMap<>();

        try {
            lines(get(filePath))
                    .skip(1)
                    .forEach(line -> {
                        final String[] split = line.split(DASH);
                        citiesWithPossibleRoutes.computeIfAbsent(split[0], empList -> new ArrayList<>()).add(split[1]);
                        citiesWithPossibleRoutes.computeIfAbsent(split[1], empList -> new ArrayList<>()).add(split[0]);
                    });
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong during file proccessing", e);
        }

        return citiesWithPossibleRoutes;
    }
}
