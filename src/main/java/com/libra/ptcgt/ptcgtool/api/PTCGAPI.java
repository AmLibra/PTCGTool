package com.libra.ptcgt.ptcgtool.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PTCGAPI {

    private static final String API_BASE = "https://api.pokemontcg.io/v2/cards";
    private static final String API_URL = API_BASE + "/";
    private static final String API_URL_SEARCH = API_BASE + "?q=name:";
    private static final String CONNECTION_SUCCESS = "Response received: 200 OK";

    public static JSONObject getCardData(String id) {
        String https_url = API_URL + id;
        URL url;
        String response = "";
        JSONObject json = new JSONObject();
        JSONParser parser = new JSONParser();

        try {
            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            int responseCode = con.getResponseCode();
            System.out.println("Fetching card with id: " + id);
            if (responseCode == 200) {
                System.out.println(CONNECTION_SUCCESS);
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                response = in.readLine();
                json = (JSONObject) parser.parse(response);
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (JSONObject) json.get("data");
    }

    public static List<JSONObject> searchCardData(String query) {
        if (query.isEmpty())
            return List.of();
        System.out.println("Fetching all cards with name: " + query);
        try {
            URL url = new URL(API_URL_SEARCH + query);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            int responseCode =connection.getResponseCode();
            if (responseCode == 200)
                return toList(parseResponse(connection));
            else
                System.out.println(responseCode);
        } catch (Exception e) { e.printStackTrace();}
        return List.of();
    }

    /**
     * @param connection
     * @return
     * @throws Exception
     */
    private static JSONObject parseResponse(HttpsURLConnection connection) throws Exception {
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
     *
     * @param json
     * @return
     */
    private static List<JSONObject> toList(JSONObject json) {
        System.out.print("Converting to list...");
        List<JSONObject> list = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) json.get("data");
        for (Object o : jsonArray) list.add((JSONObject) o);
        System.out.println(" Done!");
        return list;
    }
}
