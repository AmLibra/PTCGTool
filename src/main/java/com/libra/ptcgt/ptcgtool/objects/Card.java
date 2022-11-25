package com.libra.ptcgt.ptcgtool.objects;

import com.libra.ptcgt.ptcgtool.api.InputOutputUtils;
import com.libra.ptcgt.ptcgtool.api.PTCGAPI;
import javafx.scene.image.Image;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Card {
    private final JSONObject data; // Json Object used to build a Card Object for this App
    private final String name; // The name of the Pokémon
    private final String setName; // The name of the set this card comes from
    private final String id; // The unique id of a card, useful to find images and search a specific card's Json
    private final static String CACHED_FILES_LOCATION = "\\src\\main\\resources\\cache\\images\\";
    private final static String IMAGE_FILE_EXTENSION = ".png";
    private final String imgDir;

    /**
     * @param data Json containing all the data for a card, which we store in order to fetch data lazily as to not
     *             make the process of fetching and creating a lot of cards too long
     */
    public Card(JSONObject data) {
        this.data = data;
        name = data.get("name").toString();
        id = data.get("id").toString();
        JSONObject setData = (JSONObject) data.get("set");
        setName = setData.get("ptcgoCode") == null ? "" : setData.get("ptcgoCode").toString();
        imgDir = System.getProperty("user.dir") + CACHED_FILES_LOCATION + id + IMAGE_FILE_EXTENSION;
    }

    /**
     * Fetches lazily the image if it already is stored in the App cache, stores image on disk
     *
     * @return the Image Object used to display on the JavaFx Scene
     */
    public Image getImg() {
        File imgFile = new File(imgDir);
        if (!imgFile.isFile()) { // checks if the file exists and is not corrupted
            String imgURL = ((JSONObject) data.get("images")).get("large").toString(); //gets the higher resolution image url
            System.out.println("Image for " + this + " not found in cache. Downloading...");
            InputOutputUtils.saveImage(imgURL,System.getProperty("user.dir") + CACHED_FILES_LOCATION,id + IMAGE_FILE_EXTENSION);
            System.out.println(" Done fetching: " + this + "!");
        }
        return new Image(imgDir);
    }

    /**
     * Used to display Card as text
     * i.e. Lugia V SIT
     *
     * @return String representing the Card
     */
    @Override
    public String toString() {
        return name + " " + setName;
    }
}
