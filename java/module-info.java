module com.example.filmficionados {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.media;


    opens com.example.filmficionados to javafx.fxml;
    exports com.example.filmficionados;
    exports com.example.filmficionados.model;
    opens com.example.filmficionados.model to javafx.fxml;
    exports com.example.filmficionados.controller;
    opens com.example.filmficionados.controller to javafx.fxml;
}