package com.example.filmficionados.model;

import com.example.filmficionados.controller.FilmController;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CatMovDAOImpl implements CatMovDAO{
    private Connection con; // forbindelsen til databasen

    public CatMovDAOImpl(){
        try
        {
            // Opretter forbindelse til vores database
            con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;database=FilmDB;userName=sa;password=Bb12345678Zz;encrypt=true;trustServerCertificate=true");
            System.out.println("connected to the database... ");
        } catch (SQLException e){
            System.err.println("can not create connection " + e.getMessage());
        }
    }

    @Override
    public List<Movie> getKatFilm(Category kat) {
        List<Movie> moviesInCat = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement("select * from Movie, Category, CatMovie where Category.categoryID = CatMovie.categoryID AND CatMovie.movieID = Movie.movieID AND Category.categoryID = ?");
            ps.setInt(1, Integer.parseInt(kat.getId()));
            ResultSet rs = ps.executeQuery();
            Movie film;
            while (rs.next()) {
                String id = rs.getString(1);
                String title = rs.getString(2);
                String director = rs.getString(3);
                String resume = rs.getString(4);
                String imdb = rs.getString(5);
                String myRate = rs.getString(6);
                String fileLink = rs.getString(7);
                String lastSeen = rs.getString(8);
                String img = rs.getString(9);
                int fav = rs.getInt(15); //Favorite

                film = new Movie(id,title,director,resume,imdb,myRate,fileLink,lastSeen,img);
                film.makeFav(new Favorite(fav));
                moviesInCat.add(film);
            }
        } catch (SQLException t) {
            System.err.println("Kunne ikke få alle film i denne kategori" + t.getMessage());
        }

        return moviesInCat;
    }

    @Override
    public void føjFilmTilKat(String id1, String id2) {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO CatMovie VALUES (?,?,0);");
            ps.setInt(1, Integer.parseInt(id1));
            ps.setInt(2, Integer.parseInt(id2));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Kunne ikke tilføje film til kategori" + e.getMessage());
        }
    }

    @Override
    public void sletFilmFraKat(String id1, String id2) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM CatMovie WHERE movieID = ? AND categoryID = ?;");
            ps.setInt(1, Integer.parseInt(id1));
            ps.setInt(2, Integer.parseInt(id2));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Kunne ikke slette film fra kategori" + e.getMessage());
        }
    }

    @Override
    public void sletAlleFilmFraKat(int id) {
        try
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM CatMovie WHERE categoryID = ?");
            ps.setInt(1,id);
            ps.executeUpdate();
        }

        catch (SQLException e)
        {
            System.err.println("Kunne ikke slette denne kategori" + e.getMessage());
        }
    }

    @Override
    public void sætY(String id, String id2) {
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE CatMovie SET favorite = 1 WHERE MovieID = ? AND categoryID = ?;");
            ps.setInt(1, Integer.parseInt(id));
            ps.setInt(2, Integer.parseInt(id2));
            ps.executeUpdate();
        } catch (SQLException e)
        {
            System.err.println("Kunne ikke tilføje favorit" + e.getMessage());
        }
    }

    @Override
    public void fjernY(String id) {
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE CatMovie SET favorite = 0 WHERE categoryID = ?;");
            ps.setInt(1, Integer.parseInt(id));
            ps.executeUpdate();
        } catch (SQLException e)
        {
            System.err.println("Kunne ikke fjerne favorit" + e.getMessage());
        }
    }
}
