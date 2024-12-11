package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LetterDataProcessor {
    private static final Logger logger = LoggerFactory.getLogger(LetterDataProcessor.class);
    private final HashMap<String, ArrayList<ArrayList<ArrayList<Double>>>> alphabetMap;

    public LetterDataProcessor() {
        this("hershey-occidental.json", 0);
    }

    /**
     *
     * @param datafile The path to the json data to read
     * @param startIndex Where to start retrieving letters from the dataset. 0 is default for capital letters.
     */
    public LetterDataProcessor(String datafile, int startIndex) {
        this.alphabetMap = new HashMap<>();

        //JSON parser object to parse read file
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(datafile))
        {
            //Read JSON file
            JSONArray datalist = (JSONArray) jsonParser.parse(reader);


            // Iterate through the first 26 letters in the dataset
            char currentLetter = 'A';
            for (int i = 0; i < 26; i++) {
                Object letterVectors = datalist.get(i);
                var lines = new ArrayList<ArrayList<ArrayList<Double>>>();

                for (Object line: (JSONArray) ((JSONObject) letterVectors).get("lines")) {
                    var points = new ArrayList<ArrayList<Double>>();

                    for (Object point: (JSONArray) line) {
                        var pointArray = (JSONArray) point;
                        double x = ((Number) pointArray.get(0)).doubleValue();
                        double y = -1.0 * ((Number) pointArray.get(1)).doubleValue(); // vertical values start reversed
                        points.add(new ArrayList<>(Arrays.asList(x, y)));
                    }
                    lines.add(points);
                }

                // Map the processed lines to the letter
                alphabetMap.put(Character.toString(currentLetter), lines);
                currentLetter++;
            }

        } catch (IOException | ParseException e) {
            // Throw runtime exception for unrecoverable errors
            throw new RuntimeException("Error reading or parsing the data file", e);
        }
    }

    public ArrayList<ArrayList<ArrayList<Double>>> getLetterLines(String letter) {
        return alphabetMap.get(letter);
    }
}