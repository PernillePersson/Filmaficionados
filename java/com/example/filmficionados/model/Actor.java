package com.example.filmficionados.model;

import javafx.beans.property.SimpleStringProperty;

public class Actor {

    private SimpleStringProperty id;
    private SimpleStringProperty name;

    public Actor(String id1, String name1){
        id = new SimpleStringProperty(id1);
        name = new SimpleStringProperty(name1);
    }

    public String toString(){
        return getName();
    }

    public String getId() {
        return id.get();
    }

    public String getName() {
        return name.get();
    }

}
