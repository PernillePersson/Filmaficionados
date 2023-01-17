package com.example.filmficionados.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActMovDAOImpl implements ActMovDAO{

    private Connection con; // forbindelsen til databasen

    public ActMovDAOImpl(){
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
    public List<Actor> getActFilm(Movie f) {
        List<Actor> actorInMov = new ArrayList<>();

        try {
            PreparedStatement ps = con.prepareStatement("select * from Actor, Movie, ActMovie where Movie.movieID = ActMovie.movieID AND ActMovie.actorID = Actor.actorID AND Movie.movieID = ?");
            ps.setInt(1, Integer.parseInt(f.getId()));
            ResultSet rs = ps.executeQuery();
            Actor act;
            while (rs.next()) {
                String id = rs.getString(1);
                String name = rs.getString(2);

                act = new Actor(id,name);
                actorInMov.add(act);
            }
        } catch (SQLException t) {
            System.err.println("Kunne ikke få alle skuespillere i denne film" + t.getMessage());
        }

        return actorInMov;
    }

    @Override
    public void føjActTilFilm(String id1, String id2) {
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO ActMovie VALUES (?,?);");
            ps.setInt(1, Integer.parseInt(id1));
            ps.setInt(2, Integer.parseInt(id2));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Kunne ikke tilføje skuespiller til film" + e.getMessage());
        }
    }

    @Override
    public void sletActFraFilm(String id1, String id2) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM ActMovie WHERE movieID = ? AND actorID = ?;");
            ps.setInt(1, Integer.parseInt(id1));
            ps.setInt(2, Integer.parseInt(id2));
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Kunne ikke slette skuespiller fra film" + e.getMessage());
        }
    }

}
