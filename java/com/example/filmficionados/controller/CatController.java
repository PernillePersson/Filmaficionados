package com.example.filmficionados.controller;

import javafx.fxml.FXML;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class CatController {

    @FXML
    public BorderPane catPane;
    @FXML
    private TextField catTitleText;

    public String nyKategori(){

        return catTitleText.getText();
    }
}
