package com.libra.ptcgt.ptcgtool.objects;

import org.json.simple.JSONObject;

public final class Card {
    private String name;
    private String setName;
    private String setID;

    public Card(JSONObject data) {
        name = data.get("name").toString();
        JSONObject setData = (JSONObject) data.get("set");
        setName = setData.get("ptcgoCode") == null ? "NONE" : setData.get("ptcgoCode").toString();
        setID = setData.get("id").toString();
    }

    @Override
    public String toString() {
        return  name + " " + setName;
    }
}
