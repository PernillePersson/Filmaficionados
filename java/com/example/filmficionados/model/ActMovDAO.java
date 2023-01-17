package com.example.filmficionados.model;

import java.util.List;

public interface ActMovDAO {

    public List<Actor> getActFilm(Movie f);

    public void føjActTilFilm(String id1, String id2);

    public void sletActFraFilm(String id1, String id2);
}
