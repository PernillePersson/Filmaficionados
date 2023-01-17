package com.example.filmficionados.model;

import java.util.List;

public interface ActorDAO {

    public List<Actor> getAllActor();

    public List<Actor> getSøgActor(String query);

    public void tilføjActor(String act);

    public void sletActor(String ac);
}
