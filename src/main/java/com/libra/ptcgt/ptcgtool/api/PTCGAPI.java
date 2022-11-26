package com.libra.ptcgt.ptcgtool.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * This Class implements all the API calls used for getting the data needed for the functionalities of this app
 * A very big thank you to the developers of this API:
 * <a href="https://dev.pokemontcg.io/">PokemonTCG API Link</a>
 */
public class PTCGAPI {
    private static final String API_BASE = "https://api.pokemontcg.io/v2/cards"; // API call link
    private static final String API_URL = API_BASE + "/"; // add the id from the Card class to search for that Card
    private static final String API_URL_SEARCH = API_BASE + "?q=name:"; // suffix for searching with name
    private static final String STANDARD_LEGAL_FILTER = "legalities.standard:legal";
    private static final String ORDER_BY_DATE_FILTER = "orderBy=-set.releaseDate";
    private static final String FILTERS_DELIMITER = "&";
    private static final String NAME_FILTER_SEPARATOR = " ";
    private static final String CONNECTION_SUCCESS = "Response received: 200 OK"; // Successful API response
    private static final String CONNECTION_FAIL = "Refused API call! Response Code: "; // Successful API response


    /**
     * Fetches the Json Object used to instantiate a single Card using its id
     *
     * @param request the URL to address the request to
     * @return Json Object representing the Card
     */
    private static JSONObject getJsonObject(String request) {
        try {
            URL url = new URL(request);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200)
                return parseResponse(connection);
            else
                System.out.println(CONNECTION_FAIL + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetches the Json Object used to instantiate a single Card using its id
     *
     * @param id unique ID of the card to fetch
     * @return Json Object representing the Card
     */
    public static JSONObject getCardData(String id) {
        System.out.println("Fetching card with id: " + id);
        return getJsonObject(API_URL + id);
    }

    /**
     * Fetches the List of Json Objects representing a Card each
     *
     * @param cardName the name of the card to look for
     * @return all the cards whose names contain the name we looked for
     */
    public static List<JSONObject> searchCardData(String cardName, boolean standardLegal) {
        System.out.println("Fetching all cards containing: " + cardName);
        String standardFilter = (standardLegal ? NAME_FILTER_SEPARATOR + STANDARD_LEGAL_FILTER : "");
        return toList(getJsonObject(API_URL_SEARCH + "*" + cardName + "*" + standardFilter +
                FILTERS_DELIMITER + ORDER_BY_DATE_FILTER));
    }
public static List<JSONObject> searchCardsOfSet(String setName) {
    System.out.println("Fetching all cards of set: " + setName);
    System.out.println("NOT IMPLEMENTED YET");
    return  null;
}

    /**
     * Fetches all the cards currently available, useful for a full caching
     *
     * @return the Json Object containing all cards
     */
    public static List<JSONObject> getAllCards() { // TODO: TEST THIS WORKS
        System.out.println("Fetching all cards !");
        List<JSONObject> accumulatorList = new ArrayList<>();
        JSONObject allCardsData = Objects.requireNonNull(getJsonObject(API_BASE));
        long totalCount = (long) allCardsData.get("totalCount");
        int numPages = (int) Math.ceilDiv(totalCount, (long) allCardsData.get("pageSize")) + 1;
        IntStream.range(1, numPages).parallel().forEach( i -> accumulatorList.addAll(toList(getJsonObject(API_URL_SEARCH_PAGE(i)))));
        return accumulatorList;
    }

    /**
     * Used to receive and parse a Json response using an HTTPS connection
     *
     * @param connection the connection that was established with the server
     * @return a parsed Json Object that contains either a Json Array or a Json Object wrapped in a "data" header
     * @throws IOException    Thrown by the InputStreamReader
     * @throws ParseException Thrown by the JSONParser
     */
    private static JSONObject parseResponse(HttpsURLConnection connection) throws IOException, ParseException {
        System.out.println(CONNECTION_SUCCESS);
        System.out.print("Parsing...");
        JSONParser parser = new JSONParser();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String output = in.readLine();
        in.close();
        System.out.println(" Done!");
        return (JSONObject) parser.parse(output);
    }

    /**
     * Used to convert a JsonObject that contains a Json Array into a List of Json Objects representing a Card each
     *
     * @param json the Json Object containing a Json Array to convert
     * @return a List of Json Objects representing a Card each
     */
    private static List<JSONObject> toList(JSONObject json) {
        if (json == null) return List.of();
        System.out.print("Converting to list...");
        List<JSONObject> list = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) json.get("data");
        for (Object o : jsonArray) list.add((JSONObject) o);
        System.out.println(" Done! Found " + list.size() + " cards");
        return list;
    }


    private static String API_URL_SEARCH_PAGE(int i) {
        return API_BASE + "?page=" + i + "&pageSize=250";
    }
}
