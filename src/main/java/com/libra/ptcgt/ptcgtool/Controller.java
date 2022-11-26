package com.libra.ptcgt.ptcgtool;

import com.libra.ptcgt.ptcgtool.controllers.CardSearchTabController;
import com.libra.ptcgt.ptcgtool.controllers.DeckBuilderTabController;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab cardSearchTab;
    @FXML
    private CardSearchTabController _cardSearchTabController;
    @FXML
    private Tab deckBuilderTab;
    @FXML
    private DeckBuilderTabController _deckBuilderTabController;

    @FXML
    protected void handleKeyPressed(KeyEvent key) {
        if (key.getCode() == KeyCode.ENTER && cardSearchTab.isSelected()) _cardSearchTabController.search();
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing scene...");
        _deckBuilderTabController.linkToCardSearch(_cardSearchTabController);
        _cardSearchTabController.linkToDeckBuilder(_deckBuilderTabController);
    }
}