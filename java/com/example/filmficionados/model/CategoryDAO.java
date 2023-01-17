package com.example.filmficionados.model;

import java.util.List;

public interface CategoryDAO {
    public void tilføjKategori(String kat);

    public void sletKategori(int ka);

    public List<Category> getAllKategori();
}
