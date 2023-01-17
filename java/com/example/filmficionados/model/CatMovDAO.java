package com.example.filmficionados.model;

import java.util.List;

public interface CatMovDAO {

    public List<Movie> getKatFilm(Category kat); //Laver en liste af de film der er i en kategori

    public void føjFilmTilKat(String id1, String id2);

    public void sletFilmFraKat(String id1, String id2);

    public void sletAlleFilmFraKat(int id);

    public void sætY(String id, String id2);

    public void fjernY(String id);
}
