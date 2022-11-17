package com.libra.ptcgt.ptcgtool;

import com.libra.ptcgt.ptcgtool.api.PTCGAPI;
import com.libra.ptcgt.ptcgtool.objects.Card;
import javafx.beans.Observable;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    @FXML
    private ListView<Card> lView;
    @FXML
    private TextField searchField;

    @FXML
    protected void search() {
        List<Card> cards = new ArrayList<>();
        String s = searchField.textProperty().get();
        searchField.textProperty().setValue("");
        PTCGAPI.searchCardData(s).forEach(v -> cards.add(new Card(v)));
        lView.getItems().addAll(cards);
    }
}