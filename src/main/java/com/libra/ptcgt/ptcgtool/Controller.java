package com.libra.ptcgt.ptcgtool;

import com.libra.ptcgt.ptcgtool.api.InputOutputUtils;
import com.libra.ptcgt.ptcgtool.api.PTCGAPI;
import com.libra.ptcgt.ptcgtool.controllers.CardSearchTabController;
import com.libra.ptcgt.ptcgtool.controllers.DeckBuilderTabController;
import com.libra.ptcgt.ptcgtool.objects.Card;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Controller {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab cardSearchTab;
    @FXML
    private CardSearchTabController cardSearchTabController;
    @FXML
    private Tab deckBuilderTab;
    @FXML
    private DeckBuilderTabController deckBuilderTabController;

    public void init() {
        tabPane.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Tab> observable,
                                                                        Tab oldValue, Tab newValue) -> {
            if (newValue == cardSearchTab) {
                System.out.println("- card search -");
                System.out.println("xxx_card search_xxxController=" + cardSearchTabController); //if =null => inject problem
                cardSearchTabController.sayHi();
            } else if (newValue == deckBuilderTab) {
                System.out.println("- deck builder -");
                System.out.println("xxx_deck builder_xxxController=" + deckBuilderTabController); //if =null => inject problem
                deckBuilderTabController.sayHi();
            } else {
                System.out.println("- another Tab -");
            }
        });
    }


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

    private final SimpleStringProperty status = new SimpleStringProperty();

    //private Scene scene = tabPane.getScene();

    @FXML
    protected void handleKeyPressed(KeyEvent key) {
        if (key.getCode() == KeyCode.ENTER) search();
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing scene...");
        cardSearchTabController = new CardSearchTabController();
        deckBuilderTabController = new DeckBuilderTabController();
        init();
        if (BG_CACHING_ENABLED) System.out.println("Parallel Caching enabled.");
        System.out.println("Cache size is " + InputOutputUtils.directorySize(CACHED_FILES_LOCATION) / 1000000 + " Mb");

        cardListView.getSelectionModel().
                selectedItemProperty().addListener((o, old, newSelected) -> toggleSelectedCardView(newSelected));
        statusDisplayLabel.textProperty().bind(status);

        status.set("Welcome ! Try to look something up !");
    }

    /**
     * Called when user presses the Search button or presses enter
     */
    @FXML
    protected void search() {
        cardListView.getItems().clear();
        List<Card> cards = fetchCardsList(getSearchFieldValue(), toggleStandard.isSelected());
        cardListView.getItems().addAll(cards);
        status.set("Found " + cards.size() + " cards matching your request.");
        runCachingProcess(cards);
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

    private final static String CACHED_FILES_LOCATION = System.getProperty("user.dir") + "\\src\\main\\resources\\cache\\images";

    @FXML
    protected void clearCache() {
        System.out.print("Clearing all images from cache...");
        InputOutputUtils.deleteFile(CACHED_FILES_LOCATION);
        System.out.println("Done!");
    }


}