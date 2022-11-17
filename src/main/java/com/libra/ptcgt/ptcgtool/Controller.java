package com.libra.ptcgt.ptcgtool;

import com.libra.ptcgt.ptcgtool.api.PTCGAPI;
import com.libra.ptcgt.ptcgtool.objects.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {

    @FXML
    private Label text;
    @FXML
    private TextField searchField;

    @FXML
    protected void search() {
        String s = searchField.textProperty().get();
        StringBuilder acc = new StringBuilder();
        searchField.textProperty().setValue("");
        PTCGAPI.searchCardData(s).forEach(v -> acc.append(new Card(v)).append("\n"));
        text.setText(acc.toString());
    }
}