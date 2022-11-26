package com.libra.ptcgt.ptcgtool.controllers;

import com.libra.ptcgt.ptcgtool.api.InputOutputUtils;
import com.libra.ptcgt.ptcgtool.objects.Card;
import com.libra.ptcgt.ptcgtool.objects.Deck;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

public class DeckBuilderTabController {
    private CardSearchTabController cardSearchTabController;
    @FXML
    private ChoiceBox<String> existingDecks;
    @FXML
    private TextField deckSavingNameField;
    @FXML
    private Label currentDeckLabel;
    @FXML
    private ListView<Card> pokemon;
    @FXML
    private ListView<Card> trainer;
    @FXML
    private ListView<Card> energy;

    @FXML
    private ImageView cardImage; // the selected card from the found results
    @FXML
    private Button addButton; // used to add the selected card from current Deck
    @FXML
    private Button removeButton; // used to remove the selected card from current Deck

    private Card selectedCard;
    private final SimpleStringProperty currentDeckName = new SimpleStringProperty();
    protected Deck currentDeck;

    @FXML
    public void initialize() {
        System.out.print("Deck builder initializing...");
        existingDecks.getItems().addAll(InputOutputUtils.readDeckFolder());
        currentDeckLabel.textProperty().bind(currentDeckName);
        pokemon.getSelectionModel().
                selectedItemProperty().addListener((o, old, newSelected) -> toggleSelectedCardView(newSelected));
        trainer.getSelectionModel().
                selectedItemProperty().addListener((o, old, newSelected) -> toggleSelectedCardView(newSelected));
        energy.getSelectionModel().
                selectedItemProperty().addListener((o, old, newSelected) -> toggleSelectedCardView(newSelected));
        updateCardsView();
        updateDecksListView();
        System.out.println("Done !");
    }

    @FXML
    protected void loadDeck() {
        String selectedDeck = existingDecks.getValue();
        if (selectedDeck != null) {
            currentDeckName.set(selectedDeck);
            currentDeck = InputOutputUtils.readDeckFromDisk(selectedDeck);
            updateCardsView();
        }
    }

    @FXML
    protected void saveDeck() {
        if (currentDeck != null)
            InputOutputUtils.writeNewDeck(getSavingDeckName(), currentDeck);
        updateDecksListView();
    }

    private String getSavingDeckName() {
        String s = deckSavingNameField.textProperty().get().strip();
        deckSavingNameField.textProperty().setValue("");
        if (s == null || s.equals("")) s = "DefaultName";
        return s;
    }

    public void linkToCardSearch(CardSearchTabController cardSearchTabController) {
        this.cardSearchTabController = cardSearchTabController;
    }

    protected void updateCardsView() {
        pokemon.getItems().clear();
        trainer.getItems().clear();
        energy.getItems().clear();
        if (currentDeck != null) {
            pokemon.getItems().addAll(currentDeck.getPokemonCards());
            trainer.getItems().addAll(currentDeck.getTrainerCards());
            energy.getItems().addAll(currentDeck.getEnergyCards());
        }

    }

    /**
     * Toggles the view of the selected card on and on when needed
     *
     * @param card the current selected card, may be null
     */
    private void toggleSelectedCardView(Card card) {
        boolean cardIsSelected = card != null;
        selectedCard = card;
        addButton.setVisible(cardIsSelected);
        removeButton.setVisible(cardIsSelected);
        cardImage.setVisible(cardIsSelected);
        if (cardIsSelected)
            cardImage.setImage(card.getImg());
    }

    private void updateDecksListView() {
        existingDecks.getItems().clear();
        existingDecks.getItems().addAll(InputOutputUtils.readDeckFolder());
    }

    @FXML
    protected void addToDeck() {
        if (currentDeck != null)
            currentDeck = currentDeck.withCard(selectedCard);
        updateCardsView();
    }

    @FXML
    protected void removeFromDeck() {
        if (currentDeck != null)
            currentDeck = currentDeck.withoutCard(selectedCard);
        updateCardsView();
    }
}
