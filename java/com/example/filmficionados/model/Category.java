package com.example.filmficionados.model;

import javafx.beans.property.SimpleStringProperty;

public class Category {

    private SimpleStringProperty id;
    private SimpleStringProperty title;

    public Category(String id1, String title1){
        id = new SimpleStringProperty(id1);
        title = new SimpleStringProperty(title1);
    }

    public String toString(){
        return getTitle();
    }

    public String getId() {
        return id.get();
    }


    public String getTitle() {
        return title.get();
    }

}
