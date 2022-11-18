package com.libra.ptcgt.ptcgtool;

import com.libra.ptcgt.ptcgtool.api.PTCGAPI;
import com.libra.ptcgt.ptcgtool.objects.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class Controller {

    private final Map<String, List<Card>> searchCache = new HashMap<>();
    private final Map<Card, Image> imageCache = new HashMap<>();

    @FXML
    private ListView<Card> lView;

    @FXML
    private TextField searchField;

    @FXML
    private ImageView cardImage;

    @FXML
    private Button addButton;

    @FXML
    private Button removeButton;

    @FXML
    public void initialize() {
        System.out.println("Initializing...");
        clearCache();
        bindListView();

        //DEBUG
        searchField.textProperty().setValue("Lugia");
        search();
    }

    @FXML
    protected void search(){
        lView.getItems().clear();
        String s = searchField.textProperty().get();
        searchField.textProperty().setValue("");
        List<Card> cards = fetchCardsList(s);
        lView.getItems().addAll(cards);
        Thread t = new Thread(() -> cards.forEach(this::fetchImage));
        t.setPriority(Thread.MIN_PRIORITY);
    }

    /**
     * Updates the Image from the ListView selected content
     */
    @FXML
    protected void bindListView() {
        lView.getSelectionModel().selectedItemProperty().addListener((observableValue, c0, c1) -> {
            Card c = lView.getSelectionModel().getSelectedItem();
            if (c != null) {
                showButtons();
                cardImage.setImage(fetchImage(c));
            } else
                hideButtons();
        });
    }

    /**
     * Hides the Add and Remove Buttons
     */
    private void hideButtons() {
        addButton.setVisible(false);
        removeButton.setVisible(false);
    }

    /**
     * Shows the Add and Remove Buttons
     */
    private void showButtons() {
        addButton.setVisible(true);
        removeButton.setVisible(true);
    }

    /**
     * Fetches the Image representing a Card
     *
     * @param c the given Card
     * @return Image related to Card
     */
    private Image fetchImage(Card c) {
        if(imageCache.containsKey(c))
            return imageCache.get(c);
        AtomicReference<Image> img = new AtomicReference<>();
        Thread t = new Thread(() -> img.set(c.getImg()));
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Image res = img.get();
        imageCache.put(c, res);
        return res;
    }

    /**
     * Fetches the list of cards related to the searched Pokémon
     *
     * @param searchQuery the Pokémon we are looking for
     * @return a List of Cards that represent the cards featuring that Pokémon
     */
    private List<Card> fetchCardsList(String searchQuery) {
        if(searchCache.containsKey(searchQuery))
            return searchCache.get(searchQuery);

        List<Card> cardsList = new ArrayList<>();
        Thread t = new Thread(() -> PTCGAPI.searchCardData(searchQuery).forEach(v -> cardsList.add(new Card(v))));
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        searchCache.put(searchQuery, cardsList);
        return cardsList;
    }

    private void clearCache() {
        searchCache.clear();
        imageCache.clear();
    }
}