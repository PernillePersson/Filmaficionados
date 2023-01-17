package com.example.filmficionados.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Favorite {
    private SimpleIntegerProperty fav;

    public Favorite(int fav1) {
        fav = new SimpleIntegerProperty(fav1);
    }

    public int getFav() {
        return fav.get();
    }

    @Override
    public String toString() {
        return String.valueOf(getFav());
    }
}
