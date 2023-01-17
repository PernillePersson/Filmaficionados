package com.example.filmficionados.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class Movie {


    private SimpleStringProperty id;
    private SimpleStringProperty title;
    private SimpleStringProperty director;
    private SimpleStringProperty resume;
    private SimpleStringProperty IMDbRating;
    private SimpleStringProperty myRating;
    private SimpleStringProperty fileLink;
    private SimpleStringProperty lastSeen;
    private SimpleStringProperty img;

    private List<Favorite> yndling = new ArrayList<Favorite>();
    private List<Actor> skuespiller = new ArrayList<Actor>();



    public Movie(String id1, String title1, String director1, String resume1, String imdbRate, String myRate, String fileLink1, String date, String img1) {
        id = new SimpleStringProperty(id1);
        title = new SimpleStringProperty(title1);
        director = new SimpleStringProperty(director1);
        resume = new SimpleStringProperty(resume1);
        IMDbRating = new SimpleStringProperty(imdbRate);
        myRating = new SimpleStringProperty(myRate);
        fileLink = new SimpleStringProperty(fileLink1);
        lastSeen = new SimpleStringProperty(date);
        img = new SimpleStringProperty(img1);
    }

    public String toString(){
        return getTitle();
    }
    // Derskal være get-metoder for at tableview kan hente data
    public String getId() {
        return id.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getDirector(){
        return director.get();
    }

    public String getResume(){
        return resume.get();
    }

    public String getIMDbRating() {
        return IMDbRating.get();
    }

    public String getMyRating() {
        return myRating.get();
    }

    public String getFileLink() {
        return fileLink.get();
    }

    public String getLastSeen() {
        return lastSeen.get();
    }

    public String getImg(){
        return img.get();
    }

    public void makeFav(Favorite f){
        yndling.add(f);
    }

    public void removeFav(){
        yndling.clear();
    }

    public List<Favorite> getYndling() {
        return yndling;
    }
}
