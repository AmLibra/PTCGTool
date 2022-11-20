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

/**
 * This Class implements all the API calls used for getting the data needed for the functionalities of this app
 * A very big thank you to the developers of this API:
 * <a href="https://dev.pokemontcg.io/">PokemonTCG API Link</a>
 */
public class PTCGAPI {
    private static final String API_BASE = "https://api.pokemontcg.io/v2/cards"; // API call link
    private static final String API_URL = API_BASE + "/"; // add the id from the Card class to search for that Card
    private static final String API_URL_SEARCH = API_BASE + "?q=name:"; // suffix for searching with name
    private static final String CONNECTION_SUCCESS = "Response received: 200 OK"; // Successful API response
    private static final String CONNECTION_FAIL = "Refused API call! Response Code: "; // Successful API response

    /**
     * Fetches the Json Object used to instantiate a single Card using its id
     *
     * @param id unique ID of the card to fetch
     * @return Json Object representing the Card
     */
    public static JSONObject getCardData(String id) {
        System.out.println("Fetching card with id: " + id);
        try {
            URL url = new URL(API_URL + id);
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
     * Fetches the List of Json Objects representing a Card each
     *
     * @param cardName the name of the card to look for
     * @return all the cards whose names contain the name we looked for
     */
    public static List<JSONObject> searchCardData(String cardName) {
        if (cardName.isEmpty()) return List.of();
        System.out.println("Fetching all cards with name: " + cardName);
        try {
            URL url = new URL(API_URL_SEARCH + cardName);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200)
                return toList(parseResponse(connection));
            else
                System.out.println(CONNECTION_FAIL + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetches all the cards currently available, useful for a full caching //TODO: Read Multiples Pages
     *
     * @return the Json Object containing all cards
     */
    public static List<JSONObject> getAllCards() {
        System.out.println("Fetching all cards !");
        try {
            URL url = new URL(API_BASE);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200)
                return toList(parseResponse(connection));
            else
                System.out.println(responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
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
        System.out.print("Converting to list...");
        List<JSONObject> list = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) json.get("data");
        for (Object o : jsonArray) list.add((JSONObject) o);
        System.out.println(" Done! Found " + list.size() + " cards");
        return list;
    }


    /**
     * <a href="https://stackoverflow.com/questions/10292792/getting-image-from-url-java">Stack Overflow Solution</a>
     * This function fetches an image from a URL and downloads it to the desired destination file
     *
     * @param imageUrl        the URL we want to download the image from
     * @param destinationFile the absolute Path on the disk, we can get the Project's path using System.getProperty("user.dir")
     */
    public static void saveImage(String imageUrl, String destinationFile) {
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[(int) Math.pow(2, 9)];
            int length;
            while ((length = is.read(b)) != -1)
                os.write(b, 0, length);

            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
