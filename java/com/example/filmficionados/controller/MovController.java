package com.example.filmficionados.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MovController {

    final FileChooser fileChooser = new FileChooser();
    private FileChooser.ExtensionFilter mp4Filter = new FileChooser.ExtensionFilter("mp4 files (*.mp4)", "*.mp4");
    private FileChooser.ExtensionFilter imgFilter = new FileChooser.ExtensionFilter("img files (*.jpg)", "*.png", "*.jpg", "*.jpeg");
    @FXML
    private BorderPane borderPane;

    @FXML
    private DialogPane moviePane;

    @FXML
    private TextField IMDbText, directorText, linkText, ratingText, titleText, imgText;

    @FXML
    private TextArea resumeText;


    public String movTitle(){
        return titleText.getText();
    }

    public String director(){
        return directorText.getText();
    }

    public String resume(){
        return resumeText.getText();
    }

    public String IMDb(){
        return IMDbText.getText();
    }

    public String rating(){
        return ratingText.getText();
    }

    public String link(){
        return linkText.getText();
    }

    public String img(){
        return imgText.getText();
    }

    @FXML
    void chooseFile(ActionEvent event) {
        try
        {
            fileChooser.getExtensionFilters().clear();
            fileChooser.getExtensionFilters().add(imgFilter);
            Stage stage = (Stage) borderPane.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            imgText.setText(file.getName());
            file.renameTo(new File("/Users/pernillepersson/IdeaProjects/Filmficionados/src/main/resources/com/example/filmficionados/Img/" + file.getName()));
        }

        //Laver en catch til hvis billedets fil ikke kunne findes
        catch (Exception ex) {
            System.err.println("Vælg fil " + ex.getMessage());
        }
    }

    @FXML
    void chooseMp4(ActionEvent event) {
        try
        {
            fileChooser.getExtensionFilters().clear();
            fileChooser.getExtensionFilters().add(mp4Filter);
            Stage stage = (Stage) borderPane.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            linkText.setText(file.getName());
            file.renameTo(new File("/Users/pernillepersson/IdeaProjects/Filmficionados/src/main/resources/com/example/filmficionados/mp4/" + file.getName()));
        }

        //Laver en catch til hvis billedets fil ikke kunne findes
        catch (Exception ex) {
            System.err.println("Vælg fil " + ex.getMessage());
        }
    }

}
