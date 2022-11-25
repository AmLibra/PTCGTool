package com.libra.ptcgt.ptcgtool.controllers;

import com.libra.ptcgt.ptcgtool.api.InputOutputUtils;
import com.libra.ptcgt.ptcgtool.api.PTCGAPI;
import com.libra.ptcgt.ptcgtool.objects.Card;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CardSearchTabController {
    private final static boolean BG_CACHING_ENABLED = true; // Enables background caching of all the searched results
    // in local directory
    @FXML
    private ListView<Card> cardListView; // where the user sees and selects the found results to the query
    @FXML
    private TextField searchField; // user input field
    @FXML
    private CheckBox toggleStandard; // filters standard only
    @FXML
    private ImageView cardImage; // the selected card from the found results
    @FXML
    private Button addButton; // used to add the selected card from current Deck
    @FXML
    private Button removeButton; // used to remove the selected card from current Deck
    @FXML
    private Label statusDisplayLabel;

    @FXML
    private Label cacheSizeLabel;

    private final SimpleStringProperty cacheSize = new SimpleStringProperty();
    private final SimpleStringProperty status = new SimpleStringProperty();

    @FXML
    public void initialize() {
        if (BG_CACHING_ENABLED) System.out.println("Parallel Caching enabled.");
        System.out.print("Initializing Card Search Tab...");
        cardListView.getSelectionModel().
                selectedItemProperty().addListener((o, old, newSelected) -> toggleSelectedCardView(newSelected));
        statusDisplayLabel.textProperty().bind(status);
        cacheSizeLabel.textProperty().bind(cacheSize);
        status.set("Welcome ! Try to look something up !");
        cacheSize.set(cacheSizeToString());
        System.out.println("Done!");
    }

    /**
     * Called when user presses the Search button or presses enter
     */
    @FXML
    public void search() {
        cardListView.getItems().clear();
        List<Card> cards = fetchCardsList(getSearchFieldValue(), toggleStandard.isSelected());
        cardListView.getItems().addAll(cards);
        status.set("Found " + cards.size() + " cards matching your request.");
        runCachingProcess(cards);
        cacheSize.set(cacheSizeToString());
    }

    private void runCachingProcess(List<Card> cards) {
        if (BG_CACHING_ENABLED)
            cards.forEach(c -> new Thread(c::getImg).start());
    }

    private String getSearchFieldValue() {
        String s = searchField.textProperty().get();
        searchField.textProperty().setValue("");
        return s;
    }

    /**
     * Toggles the view of the selected card on and on when needed
     *
     * @param card the current selected card, may be null
     */
    private void toggleSelectedCardView(Card card) {
        boolean cardIsSelected = card != null;
        addButton.setVisible(cardIsSelected);
        removeButton.setVisible(cardIsSelected);
        cardImage.setVisible(cardIsSelected);
        statusDisplayLabel.setVisible(!cardIsSelected);
        if (cardIsSelected)
            cardImage.setImage(card.getImg());
        cacheSize.set(cacheSizeToString());
    }

    /**
     * Fetches the list of cards related to the searched Pokémon
     *
     * @param searchQuery the Pokémon we are looking for
     * @return a List of Cards that represent the cards featuring that Pokémon
     */
    private List<Card> fetchCardsList(String searchQuery, boolean isStandardLegal) {
        List<Card> cardsList = new ArrayList<>();
        Objects.requireNonNull(PTCGAPI.searchCardData(searchQuery, isStandardLegal))
                .forEach(v -> cardsList.add(new Card(v)));
        return cardsList;
    }

    @FXML
    protected void clearCache() {
        System.out.print("Clearing all images from cache...");
        InputOutputUtils.deleteFile(InputOutputUtils.CACHED_FILES_LOCATION);
        cacheSize.set(cacheSizeToString());
        System.out.println("Done!");
    }
    private String cacheSizeToString() {
        return "Cache size is " + InputOutputUtils.directorySize(InputOutputUtils.CACHED_FILES_LOCATION) / 1000000 + " Mb";
    }
}
