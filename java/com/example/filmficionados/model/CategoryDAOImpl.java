package com.example.filmficionados.model;

import com.example.filmficionados.dao.CategoryDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAOImpl implements CategoryDAO {
    private Connection con; // forbindelsen til databasen

    public CategoryDAOImpl(){
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
    public void tilføjKategori(String kat) {
        try
        {
            PreparedStatement ps = con.prepareStatement("INSERT INTO Category VALUES(?);");

            ps.setString(1, kat);

            ps.executeUpdate();
        }
        catch (SQLException e)
        {
            System.err.println("Kan ikke oprette kategori " + e.getMessage());
        }
    }

    @Override
    public void sletKategori(int ka) {
        try
        {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Category WHERE categoryID = ?");
            ps.setInt(1,ka);
            ps.executeUpdate();
        }

        catch (SQLException e)
        {
            System.err.println("Kunne ikke slette denne kategori" + e.getMessage());
        }
    }

    @Override
    public List<Category> getAllKategori() {
        List<Category> fåAlleKategorier = new ArrayList();
        try
        {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Category");
            ResultSet rs = ps.executeQuery();

            Category kategori;
            while(rs.next())
            {
                String id = rs.getString(1);
                String title = rs.getString(2);

                kategori = new Category(id,title);
                fåAlleKategorier.add(kategori);
            }
        }
        catch (SQLException e)
        {
            System.err.println("can not access records" + e.getMessage());
        }
        return fåAlleKategorier;
    }
}
