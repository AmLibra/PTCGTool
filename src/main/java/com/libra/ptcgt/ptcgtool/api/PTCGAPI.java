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

    public static JSONObject getCardData(String id) {
        String https_url = "https://api.pokemontcg.io/v2/cards/" + id;
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
                System.out.println("Response received !");
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
        if (query.isEmpty()) return List.of();

        String https_url = "https://api.pokemontcg.io/v2/cards?q=name:" + query;
        URL url;
        String response = "";
        JSONObject json = new JSONObject();
        JSONParser parser = new JSONParser();
        try {
            url = new URL(https_url);
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            int responseCode = con.getResponseCode();
            System.out.println("Fetching card with name: " + query);
            if (responseCode == 200) {
                System.out.println("Response received !");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                response = in.readLine();
                json = (JSONObject) parser.parse(response);
                in.close();
            }
        } catch (Exception e)  {
            e.printStackTrace();
        }
        List<JSONObject> list = new ArrayList<>();
        JSONArray arr = (JSONArray) json.get("data");
        arr.forEach(v -> list.add((JSONObject) v));
        return list;
    }
}
