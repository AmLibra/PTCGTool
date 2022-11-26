package com.libra.ptcgt.ptcgtool.controllers;

import com.libra.ptcgt.ptcgtool.api.InputOutputUtils;
import com.libra.ptcgt.ptcgtool.objects.CardType;
import com.libra.ptcgt.ptcgtool.objects.Deck;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.util.List;

public class DeckBuilderTabController {
    private CardSearchTabController cardSearchTabController;
    @FXML
    private ChoiceBox<String> existingDecks;
    @FXML
    private Label currentDeckLabel;
    private final SimpleStringProperty currentDeckName = new SimpleStringProperty();
    protected Deck currentDeck;



    @FXML
    public void initialize() {
        System.out.print("Deck builder initializing...");
        existingDecks.getItems().addAll(InputOutputUtils.readDeckFolder());
        currentDeckLabel.textProperty().bind(currentDeckName);
        InputOutputUtils.writeNewDeck("New Deck", new Deck(List.of()));
        System.out.println("Done !");
    }

    @FXML
    protected void loadDeck() {
        String selectedDeck = existingDecks.getValue();
        if (selectedDeck != null)
            currentDeck = InputOutputUtils.readDeckFromDisk(selectedDeck);
    }

    @FXML
    protected void saveDeck() {
        if (currentDeck != null)
            InputOutputUtils.writeNewDeck("DefaultName", currentDeck);
    }

    public void linkToCardSearch(CardSearchTabController cardSearchTabController) {
        this.cardSearchTabController = cardSearchTabController;
    }

    @FXML
    protected void addToDeck() {
        System.out.println("adding Card");
        currentDeck = currentDeck.withCard(cardSearchTabController.selectedCard);
    }

    @FXML
    protected void removeFromDeck() {
        System.out.println("removing Card");
        currentDeck = currentDeck.withoutCard(cardSearchTabController.selectedCard);
    }
}
