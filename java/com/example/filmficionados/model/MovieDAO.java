package com.example.filmficionados.model;

import java.util.List;

public interface MovieDAO {

    public void tilføjFilm(String s0, String s1, String s2, String s3, String s4, String s5, String s6);

    public void updateRating(String film, int id);

    public void updateSeenDate(int id);

    public void sletFilm(int id);

    public List<Movie> getAlleFilm();

    public  List<Movie> getSøgFilm(String query);

    public List<Movie> getOldFilm();
}
