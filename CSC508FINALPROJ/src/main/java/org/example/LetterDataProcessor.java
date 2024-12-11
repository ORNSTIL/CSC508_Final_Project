package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Parses and processes raw letter stroke data from a JSON file.
 *
 * @version 1.0
 * @author Luca Ornstil
 * @author Monish Suresh
 * @author Christine Widden
 */

public class LetterDataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(LetterDataProcessor.class);
    private final HashMap<String, ArrayList<ArrayList<ArrayList<Double>>>> alphabetMap;

    public LetterDataProcessor() {
        this("hershey-occidental.json", 0);
    }

    /**
     * @param datafile   The path to the JSON data to read (in the resources folder).
     * @param startIndex Where to start retrieving letters from the dataset. 0 is default for capital letters.
     */
    public LetterDataProcessor(String datafile, int startIndex) {
        this.alphabetMap = new HashMap<>();

        // JSON parser object to parse the read file
        JSONParser jsonParser = new JSONParser();

        // Load the JSON file from resources
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(datafile);
             InputStreamReader reader = new InputStreamReader(inputStream)) {

            if (inputStream == null) {
                throw new FileNotFoundException(datafile + " not found in resources.");
            }

            // Read and parse the JSON file
            JSONArray datalist = (JSONArray) jsonParser.parse(reader);

            // Iterate through the first 26 letters in the dataset
            char currentLetter = 'A';
            for (int i = 0; i < 26; i++) {
                Object letterVectors = datalist.get(i);
                var lines = new ArrayList<ArrayList<ArrayList<Double>>>();

                for (Object line : (JSONArray) ((JSONObject) letterVectors).get("lines")) {
                    var points = new ArrayList<ArrayList<Double>>();

                    for (Object point : (JSONArray) line) {
                        var pointArray = (JSONArray) point;
                        double x = ((Number) pointArray.get(0)).doubleValue();
                        double y = -1.0 * ((Number) pointArray.get(1)).doubleValue(); // Vertical values are inverted
                        points.add(new ArrayList<>(Arrays.asList(x, y)));
                    }
                    lines.add(points);
                }

                // Map the processed lines to the letter
                alphabetMap.put(Character.toString(currentLetter), lines);
                currentLetter++;
            }

            logger.info("Successfully loaded letter data from '{}'", datafile);

        } catch (IOException | ParseException e) {
            logger.error("Error reading or parsing the data file: {}", e.getMessage());
            throw new RuntimeException("Error reading or parsing the data file", e);
        }
    }

    /**
     * Retrieves the line data for a specific letter.
     *
     * @param letter The letter to retrieve line data for.
     * @return The list of lines representing the letter, or null if not found.
     */
    public ArrayList<ArrayList<ArrayList<Double>>> getLetterLines(String letter) {
        return alphabetMap.get(letter);
    }
}
