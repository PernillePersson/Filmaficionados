package com.example.filmficionados.controller;

import com.example.filmficionados.model.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class ActorController {

    @FXML
    private TextField actorText;

    @FXML
    private ListView allActorsList = new ListView(), addedActorsList = new ListView();

    private String id;

    public void initialize(){
        visActors();
        blankFelt();
    }
    @FXML
    void føjActorTilFilm(ActionEvent event) {
        ObservableList valgteIndeks = allActorsList.getSelectionModel().getSelectedIndices();
        if (valgteIndeks.size() == 0)
            System.out.println("Vælg en skuespiller");
        else
            for (Object indeks : valgteIndeks)
            {
                Actor ac = (Actor) allActorsList.getSelectionModel().getSelectedItem();
                addedActorsList.getItems().add(ac);
                amdi.føjActTilFilm(ac.getId(), id);
            }
    }

    @FXML
    void søgActor(ActionEvent event) {
        allActorsList.getItems().clear();
        List<Actor> søgtActor = adi.getSøgActor(actorText.getText());
        for (Actor a : søgtActor)
        {
            allActorsList.getItems().add(a);
        }
    }

    public void blankFelt(){
        actorText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (actorText.getText().isEmpty())
                visActors();
        });
    }

    @FXML
    void tilføjActor(ActionEvent event) {
        adi.tilføjActor(actorText.getText());
        visActors();
        allActorsList.scrollTo(allActorsList.getItems().size()-1);
    }

    @FXML
    void sletActor(ActionEvent event){
        ObservableList valgteIndeks = allActorsList.getSelectionModel().getSelectedIndices();
        if (valgteIndeks.size() == 0)
            System.out.println("Vælg en skuespiller");
        else
            for (Object indeks : valgteIndeks)
            {
                Actor ac = (Actor) allActorsList.getSelectionModel().getSelectedItem();
                adi.sletActor(ac.getId());
            }
        visActors();
    }

    public void visActors(){
        allActorsList.getItems().clear();
        List<Actor> alleActors = adi.getAllActor();
        for (Actor a : alleActors)
        {
            allActorsList.getItems().add(a);
        }
    }

    public void filmId(String s){
        id = s;
    }


    ActorDAO adi = new ActorDAOImpl();
    ActMovDAO amdi = new ActMovDAOImpl();
}
