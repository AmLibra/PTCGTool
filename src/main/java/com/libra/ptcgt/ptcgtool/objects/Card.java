package com.libra.ptcgt.ptcgtool.objects;

import javafx.scene.image.Image;
import org.json.simple.JSONObject;

public final class Card {
    private final JSONObject data;
    private final String name;
    private final String setName;
    private final String setID;
    private final String imgURL;
    private Image img;

    public Card(JSONObject data) {
        this.data = data;
        name = data.get("name").toString();
        JSONObject setData = (JSONObject) data.get("set");
        setName = setData.get("ptcgoCode") == null ? "NONE" : setData.get("ptcgoCode").toString();
        setID = setData.get("id").toString();
        JSONObject imgData = (JSONObject) data.get("images");
        imgURL = imgData.get("large").toString();
    }

    public Image getImg() {
        return img == null ? img = new Image(imgURL) : img;
    }

    public String getSetID() {
        return setID;
    }

    @Override
    public String toString() {
        return name + " " + setName;
    }
}
