package com.example.filmficionados.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MovieDAOImpl implements MovieDAO{

    private Connection con; // forbindelsen til databasen
    public MovieDAOImpl(){
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
    public void tilføjFilm(String s0, String s1, String s2, String s3, String s4, String s5, String s6) {
        try{
            PreparedStatement ps = con.prepareStatement("INSERT INTO Movie VALUES(?,?,?,?,?,?,getDate(),?);");
            ps.setString(1, s0);
            ps.setString(2, s1);
            ps.setString(3, s2);
            ps.setDouble(4, Double.parseDouble(s3));
            ps.setDouble(5, Double.parseDouble(s4));
            ps.setString(6, s5);
            ps.setString(7, s6);
            ps.executeUpdate();
        } catch (SQLException e)
        {
            System.err.println("Kunne ikke tilføje film" + e.getMessage());
        }
    }

    @Override
    public void updateRating(String film, int id) {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE Movie SET ratingOwn = ? WHERE movieID = ?;");

            ps.setString(1, film);
            ps.setInt(2, id);


            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.err.println("Kan ikke ændre din rating af denne film " + e.getMessage());
        }
    }

    @Override
    public void updateSeenDate(int id) {
        try {
            PreparedStatement ps = con.prepareStatement("UPDATE Movie SET lastSeen = getDate() WHERE movieID = ?;");

            ps.setInt(1, id);


            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.err.println("Kan ikke ændre sidst set dato på denne film " + e.getMessage());
        }
    }

    @Override
    public void sletFilm(int id) {
        try
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Movie WHERE movieID = ?");
            ps.setInt(1,id);
            ps.executeUpdate();
        }

        catch (SQLException e)
        {
            System.err.println("Kunne ikke slette film" + e.getMessage());
        }
    }

    @Override
    public List<Movie> getAlleFilm() {
        List<Movie> fåAlleFilm = new ArrayList<>();
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Movie");
            ResultSet rs = ps.executeQuery();

            Movie film;
            while(rs.next())
            {
                String id = rs.getString(1);
                String title = rs.getString(2);
                String director = rs.getString(3);
                String resume = rs.getString(4);
                String imdb = rs.getString(5);
                String myRate = rs.getString(6);
                String fileLink = rs.getString(7);
                String lastSeen = rs.getString(8);
                String img = rs.getString(9);


                film = new Movie(id,title,director,resume,imdb,myRate,fileLink,lastSeen,img);
                fåAlleFilm.add(film);
            }
        }
        catch (SQLException e)
        {
            System.err.println("can not access records" + e.getMessage());
        }
        return fåAlleFilm;
    }

    @Override
    public List<Movie> getSøgFilm(String query) {
        List<Movie> søgteFilm= new LinkedList<>();
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Movie WHERE title LIKE ? OR director LIKE ? OR ratingIMBd LIKE ?;");
            ps.setString(1, query);
            ps.setString(2, query);
            ps.setString(3, query);
            ResultSet rs = ps.executeQuery();

            Movie film;
            while(rs.next())
            {
                String id = rs.getString(1);
                String title = rs.getString(2);
                String director = rs.getString(3);
                String resume = rs.getString(4);
                String imdb = rs.getString(5);
                String myRate = rs.getString(6);
                String fileLink = rs.getString(7);
                String lastSeen = rs.getString(8);
                String img = rs.getString(9);


                film = new Movie(id,title,director,resume,imdb,myRate,fileLink,lastSeen,img);
                søgteFilm.add(film);
            }
        }
        catch (SQLException e)
        {
            System.err.println("Kunne ikke finde filmen du søgte, " + e.getMessage());
        }
        return søgteFilm;
    }

    @Override
    public List<Movie> getOldFilm() {
        List<Movie> fåAlleGamleFilm = new ArrayList<>();
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Movie WHERE lastSeen < DATEADD(year, -2, GETDATE());");
            ResultSet rs = ps.executeQuery();

            Movie film;
            while(rs.next())
            {
                String id = rs.getString(1);
                String title = rs.getString(2);
                String director = rs.getString(3);
                String resume = rs.getString(4);
                String imdb = rs.getString(5);
                String myRate = rs.getString(6);
                String fileLink = rs.getString(7);
                String lastSeen = rs.getString(8);
                String img = rs.getString(9);


                film = new Movie(id,title,director,resume,imdb,myRate,fileLink,lastSeen,img);
                fåAlleGamleFilm.add(film);
            }
        }
        catch (SQLException e)
        {
            System.err.println("can not access records" + e.getMessage());
        }
        return fåAlleGamleFilm;
    }

}
