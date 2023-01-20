package com.example.filmficionados.model;

import com.example.filmficionados.dao.ActorDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ActorDAOImpl implements ActorDAO {

    private Connection con; // forbindelsen til databasen
    public ActorDAOImpl(){
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
    public List<Actor> getAllActor() {
        List<Actor> fåAlleActor = new ArrayList<>();
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Actor");
            ResultSet rs = ps.executeQuery();

            Actor act;
            while(rs.next())
            {
                String id = rs.getString(1);
                String title = rs.getString(2);


                act = new Actor(id,title);
                fåAlleActor.add(act);
            }
        }
        catch (SQLException e)
        {
            System.err.println("can not access records" + e.getMessage());
        }
        return fåAlleActor;
    }

    @Override
    public List<Actor> getSøgActor(String query) {
        List<Actor> søgtActor= new LinkedList<>();
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Actor WHERE name LIKE ?;");
            ps.setString(1, query);
            ResultSet rs = ps.executeQuery();

            Actor act;
            while(rs.next())
            {
                String id = rs.getString(1);
                String name = rs.getString(2);

                act = new Actor(id,name);
                søgtActor.add(act);
            }
        }
        catch (SQLException e)
        {
            System.err.println("Kunne ikke finde skuespilleren du søgte på, " + e.getMessage());
        }
        return søgtActor;
    }

    @Override
    public void tilføjActor(String act) {
        try
        {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Actor VALUES(?);");

            ps.setString(1, act);

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.err.println("Kan ikke oprette ny actor " + e.getMessage());
        }
    }

    @Override
    public void sletActor(String ac) {
        try
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Actor WHERE actorID = ?");
            ps.setInt(1, Integer.parseInt(ac));
            ps.executeUpdate();
        }

        catch (SQLException e)
        {
            System.err.println("Kunne ikke slette denne actor" + e.getMessage());
        }
    }
}
