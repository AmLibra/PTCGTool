package com.libra.ptcgt.ptcgtool;

import com.libra.ptcgt.ptcgtool.api.PTCGAPI;
import com.libra.ptcgt.ptcgtool.objects.Card;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Controller {

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
    private TabPane tabPane;

    //private Scene scene = tabPane.getScene();

    @FXML
    protected void handleKeyPressed(KeyEvent key){
        KeyCode c = key.getCode();
        System.out.println("Key Pressed: " + c);
        if(c == KeyCode.ENTER)
            search();
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing scene...");
        bindListView();

        //DEBUG
        searchField.textProperty().setValue("Lugia");
        search();
    }

    /**
     * Called when user presses the Search button or presses enter
     */
    @FXML
    protected void search() {
        cardListView.getItems().clear();
        List<Card> cards = fetchCardsList(getSearchFieldValue(), toggleStandard.isSelected());
        cardListView.getItems().addAll(cards);
        runCachingProcess(cards);
    }

    private void runCachingProcess(List<Card> cards) {
        if (!BG_CACHING_ENABLED) return;
        System.out.println("Parallel Caching enabled.");
        cards.forEach(c -> new Thread(c::getImg).start());
    }

    private String getSearchFieldValue() {
        String s = searchField.textProperty().get();
        searchField.textProperty().setValue("");
        return s;
    }

    /**
     * Updates the current displayed Image to reflect the selected card in the list
     */
    @FXML
    protected void bindListView() {
        cardListView.getSelectionModel().selectedItemProperty().addListener((observableValue, c0, c1) -> {
            Card card = cardListView.getSelectionModel().getSelectedItem();
            toggleSelectedCardView(card);
        });
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
        if (cardIsSelected)
            cardImage.setImage(card.getImg());
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
}