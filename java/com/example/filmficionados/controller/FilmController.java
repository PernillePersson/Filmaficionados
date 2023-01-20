package com.example.filmficionados.controller;

import com.example.filmficionados.FilmApplication;
import com.example.filmficionados.dao.ActMovDAO;
import com.example.filmficionados.dao.CatMovDAO;
import com.example.filmficionados.dao.CategoryDAO;
import com.example.filmficionados.dao.MovieDAO;
import com.example.filmficionados.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class FilmController {

    @FXML
    private Label catLabel, dateLabel, directorLabel, titleLabel; //information om film

    @FXML
    private ImageView mPoster; //Viser filmplakat

    @FXML
    private TextArea resumeText; //filmbeskrivelse

    @FXML
    private TextField søgefelt;

    @FXML
    private ListView movieList = new ListView(), categoryList = new ListView(), actorList = new ListView(), oldMovList = new ListView<>();

    private final ObservableList<Movie> moviesInCategoryObsList = FXCollections.observableArrayList(); //Kan interegere med observable - så det skal bruges til tableview

    @FXML private TableView<Movie> catMovTable;
    @FXML private TableColumn<Movie, String> titleColumn, imdbColumn, myRateColumn, favColumn;

    private ImageView favView;
    private final Image emptyFav = new Image(String.valueOf(FilmApplication.class.getResource("Img/emptyStar.png")));
    private final Image filledFav = new Image(String.valueOf(FilmApplication.class.getResource("Img/filledStar.png")));

    private String gemtId, gemtMp4;

    public void initialize(){
        visFilm();
        visKategori();
        visOldFilm();
        categoryList.getSelectionModel().select(0); //vælger den første kategori
        visKatFilm();
        catMovTable.getSelectionModel().selectFirst(); //Vælger den første film i kategorien
        setLabels();
    }

    public void visFilm(){
        //Viser alle film på hele biblioteket
        movieList.getItems().clear();
        List<Movie> film = mdi.getAlleFilm(); //Laver en liste af alle film i databasen
        for (Movie fi : film)
        {
            movieList.getItems().add(fi); //tilføjer alle film til listview
        }
    } //Viser alle film

    public void visKategori(){
        //Viser alle film i den første "mappe" af kategorier
        categoryList.getItems().clear();
        List<Category> kategorier = cdi.getAllKategori(); //Laver liste af alle kategorier i databasen
        for (Category ka : kategorier)
        {
            categoryList.getItems().add(ka); //Tilføjer kategorierne til listview
        }
    } //Viser alle kategorier

    public void visKatFilm(){
        //Viser alle film i valgt kategori
        //Finder først det valgte item i kategori listen, og opsætter tableview,
        //med alle sange i den bestemte kategori.
        søgefelt.clear();
        ObservableList valgtKategori = categoryList.getSelectionModel().getSelectedIndices();
        for (Object indeks : valgtKategori){
            catMovTable.getItems().clear();
            titleColumn.setCellValueFactory(new PropertyValueFactory<>("Title"));
            imdbColumn.setCellValueFactory(new PropertyValueFactory<>("IMDbRating"));
            myRateColumn.setCellValueFactory(new PropertyValueFactory<>("MyRating"));
            favColumn.setCellValueFactory(new PropertyValueFactory<>("Yndling"));
            Category kat = (Category) categoryList.getItems().get((int) indeks);
            moviesInCategoryObsList.addAll(cmdi.getKatFilm(kat)); //Liste med alle film i bestemt kategori
            catMovTable.setItems(moviesInCategoryObsList); //Sætter film op i tableviewet
            catMovTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            catLabel.setText(kat.getTitle()); //Fortæller hvilken kategori der bliver vist
        }

    } //Viser film i den valgte katagori

    public void setLabels(){
        //Sætter til at passe med den valgte sang
        ObservableList valgtFilm = catMovTable.getSelectionModel().getSelectedIndices();
        for (Object indeks : valgtFilm){
            Movie mov = (Movie) catMovTable.getItems().get((int) indeks);
            dateLabel.setText(mov.getLastSeen()); ;
            directorLabel.setText(mov.getDirector());
            titleLabel.setText(mov.getTitle());
            resumeText.setText(mov.getResume());
            mPoster.setImage(new Image(String.valueOf(FilmApplication.class.getResource("Img/" + mov.getImg()))));

            actorList.getItems().clear();
            List<Actor> skuespiller = amdi.getActFilm(mov); //Laver liste af alle skuespillere for film
            for (Actor act : skuespiller)
            {
                actorList.getItems().add(act);
            }
            gemtId = mov.getId(); //Sætter String til if for film, til brug i andre metoder
            gemtMp4 = mov.getFileLink(); //Sætter string til mp4, til brug i andre metoder
        }
    } //Opdaterer information om den valgte film

    public void visOldFilm(){
        List<Movie> film = mdi.getOldFilm(); //Laver en liste af film der ikke er set i 2 år
        for (Movie fi : film)
        {
            oldMovList.getItems().add(fi);
        }
    } //Viser film der ikke er blevet set i mere end 2 år
    @FXML
    void getInfo(ActionEvent event) {
        //Viser information om en film fra listen med film der ikke er set længe
        ObservableList valgtFilm = oldMovList.getSelectionModel().getSelectedIndices();
        for (Object indeks : valgtFilm){
            Movie mov = (Movie) oldMovList.getItems().get((int) indeks);
            //sætter labels på samme måde som setLabels() - men fra listen med film der ikke er set i mere end 2 år
            dateLabel.setText(mov.getLastSeen()); ;
            directorLabel.setText(mov.getDirector());
            titleLabel.setText(mov.getTitle());
            resumeText.setText(mov.getResume());
            mPoster.setImage(new Image(String.valueOf(FilmApplication.class.getResource("Img/" + mov.getImg()))));

            actorList.getItems().clear();
            List<Actor> skuespiller = amdi.getActFilm(mov);
            for (Actor act : skuespiller)
            {
                actorList.getItems().add(act);
            }
            gemtId = mov.getId(); //Sætter String til if for film, til brug i andre metoder
            gemtMp4 = mov.getFileLink(); //Sætter string til mp4, til brug i andre metoder
        }
    } //Viser info om film valgt som ikke er set mere end 2 år
    @FXML
    void sletOldFilm(ActionEvent event){
        //Sletter valgt film i listen fra gamle sange fra hele biblioteket
        ObservableList valgtFilm = oldMovList.getSelectionModel().getSelectedIndices();
        if (valgtFilm.size() == 0)
            System.out.println("Vælg en film");
        else
            for (Object indeks : valgtFilm)
            {
                Movie f  = (Movie) oldMovList.getItems().get((int) indeks);
                mdi.sletFilm(Integer.parseInt(f.getId())); //Sletter film helt fra databasen
                //Clearer og opdaterer listviews, så den slettede film ikke længere vises
                movieList.getItems().clear();
                oldMovList.getItems().clear();
                visFilm();
                visOldFilm();
            }
    } //Sletter en film man ikke har set længe

    @FXML
    void tilføjFilm(ActionEvent event) throws IOException {
        //Tilføjer film
        //Åbner dialog med anden FXML-fil.

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(FilmApplication.class.getResource("fxml/addMovie.fxml"));
        DialogPane moviePane = fxmlLoader.load();

        MovController movController = fxmlLoader.getController(); //Tilføjer passende controller

        Dialog<ButtonType> mDialog = new Dialog<>();
        mDialog.setDialogPane(moviePane);
        mDialog.setTitle("Add new movie");
        mDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> knap = mDialog.showAndWait();
        if (knap.get() == ButtonType.OK)
            try {
                //Tilføjer nyt objekt af film ved brug af textfield i movControlleren
                mdi.tilføjFilm(movController.movTitle(),movController.director(),movController.resume(),
                        movController.IMDb(),movController.rating(),movController.link(),movController.img());
                visFilm();

            } catch (Exception e)
            {
                System.err.println("Fejl: " + e.getMessage());
                alert(e.getMessage());
            }

    } //Åbner dialog hvor man kan udfylde info om ny film

    @FXML
    void sletFilm(ActionEvent event) {
        //Sletter film
        ObservableList valgtFilm = movieList.getSelectionModel().getSelectedIndices();
        if (valgtFilm.size() == 0)
            System.out.println("Vælg en film");
        else
            for (Object indeks : valgtFilm)
            {
                Movie f  = (Movie) movieList.getItems().get((int) indeks); //Finder valgt objekt i listen for at kunne hente id
                mdi.sletFilm(Integer.parseInt(f.getId())); //Sletter den valgte film fra databasen
                movieList.getItems().clear();
                visFilm();
            }
    } //Sletter film

    @FXML
    void catClick(MouseEvent event) {
        visKatFilm();
    } //Viser film i den valgte kategori
    @FXML
    void tilføjKategori(ActionEvent event) throws IOException {
        //Åbner dialog med anden FXML-fil.

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(FilmApplication.class.getResource("fxml/addCategory.fxml"));
        DialogPane categoryPane = fxmlLoader.load();

        CatController catController = fxmlLoader.getController();

        Dialog<ButtonType> cDialog = new Dialog<>();
        cDialog.setDialogPane(categoryPane);
        cDialog.setTitle("Add new category");
        cDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> knap = cDialog.showAndWait();
        if (knap.get() == ButtonType.OK)
            try {
                cdi.tilføjKategori(catController.nyKategori()); //Tilføjer objekt af kategori ud fra textfield i catController
                visKategori();

            } catch (Exception e)
            {
                System.err.println("Fejl: " + e.getMessage());
                alert(e.getMessage());
            }
    } //Dialog med fxml tilføjer ny kategori

    @FXML
    void sletKategori(ActionEvent event) {
        //Sletter kategori
        ObservableList valgtKategori = categoryList.getSelectionModel().getSelectedIndices();
        if (valgtKategori.size() == 0)
            System.out.println("Vælg en kategori");
        else
            for (Object indeks : valgtKategori)
            {
                Category ka = (Category) categoryList.getItems().get((int) indeks);
                cmdi.sletAlleFilmFraKat(Integer.parseInt(ka.getId())); //Sletter først film fra kategori, så de ikke er i databasen
                cdi.sletKategori(Integer.parseInt(ka.getId())); //sletter kategori fra databasen
                categoryList.getItems().clear();
                visKategori();
            }
    } //Sletter valgt kategori

    @FXML
    void currentMovClick(MouseEvent event) {
        //klikkes i tableviewet med film
        setLabels();
    } //Ændre alle labels når der klikkes i tableview, for at få info om film

    @FXML
    public void sætYndling(ActionEvent event){
        Category c = (Category) categoryList.getSelectionModel().getSelectedItem(); //Laver object for at få id fra kategori
        cmdi.fjernY(c.getId()); //Updaterer databasen - sætter alle film i den kategori til at være 0
        cmdi.sætY(gemtId, c.getId()); //Sætter den valgte film i kategorien til at være 1
        visKatFilm(); //Genopsætter filmene i tableviewet
    } //Opdaterer hvilken film der er favorit i den bestemte kategori

    @FXML
    public void ændreRating(ActionEvent event) {
        Dialog<ButtonType> ratingDialog = new Dialog();
        // Her sættes et nyt vindue op
        ratingDialog.setTitle("Change your rating for this movie");
        ratingDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField rating = new TextField(); //Til ny rating
        rating.setPromptText("Rate movie ex. 4.7...");
        VBox opsæt = new VBox(rating);
        ratingDialog.getDialogPane().setContent(opsæt);
        Optional<ButtonType> knap = ratingDialog.showAndWait();
        // Derefter kan vi hente felternes indhold ud og gøre hvad der skal gøres...
        if (knap.get() == ButtonType.OK)
            try {
                ObservableList valgteIndeks = catMovTable.getSelectionModel().getSelectedIndices();
                if (valgteIndeks.size() == 0)
                    System.out.println("Vælg en film");
                else
                    for (Object indeks : valgteIndeks) {
                        Movie mr = (Movie) catMovTable.getItems().get((int) indeks); //Henter valgt objekt for at få id
                        mdi.updateRating(rating.getText(), Integer.parseInt(mr.getId())); //Updaterer databasen - ændrer rating fra textfield
                        catMovTable.getItems().clear();
                        visKatFilm();
                    }

                //Laver en catch til hvis rating ikke bliver ændret
            } catch (Exception e) {
                System.err.println("Noget gik galt");
                System.err.println("Tjek om du har skrevet rating rigtigt");
                System.err.println("Fejl: " + e.getMessage());
                alert("Maybe you need a dot - " + e.getMessage());
            }
    } //Dialogopsæt uden fxml ændrer egen rating for film

    @FXML
    void sletFilmFraKat(ActionEvent event){
        ObservableList valgteIndeks = catMovTable.getSelectionModel().getSelectedIndices();
        if (valgteIndeks.size() == 0)
            System.out.println("Vælg en film");
        else
            for (Object indeks : valgteIndeks) {
                Movie fi = (Movie) catMovTable.getItems().get((int) indeks); //Henter objekt for at få id
                Category ka = (Category) categoryList.getSelectionModel().getSelectedItem(); //Henter objekt for at få id
                cmdi.sletFilmFraKat(fi.getId(),ka.getId()); //Fjerne film fra kategori i databasen
            }
        visKatFilm();
    } //Fjerner en film fra bestemt kategori

    @FXML
    void føjTilKat(ActionEvent event){
        ObservableList valgteIndeks = movieList.getSelectionModel().getSelectedIndices();
        if (valgteIndeks.size() == 0)
            System.out.println("Vælg en film");
        else
            for (Object indeks : valgteIndeks)
            {
                Movie fi  = (Movie) movieList.getItems().get((int) indeks);
                Category ka = (Category) categoryList.getSelectionModel().getSelectedItem();
                cmdi.føjFilmTilKat(fi.getId(),ka.getId()); //opdaterer database - tilføjer valgt film till åben kategori

                visKatFilm(); //Sætter film op i tableview på ny
            }
    } //Tilføjer valgt film til valgt kategori

    @FXML
    void søgKnap(ActionEvent event) {
        //Viser alle film i databasen der matcher det der er blevet skrevet i søgefeltet
        List<Movie> søgtFilm = mdi.getSøgFilm(søgefelt.getText()); //Laver en liste af match mellem søgefelt og objekter i databsen
        catLabel.setText(søgefelt.getText());
        catMovTable.getItems().clear();
        for (Movie mov : søgtFilm)
        {
            catMovTable.getItems().add(mov); //Sætter alle film der er blevet søgt på, op i tableviewet
        }
    } //Sætter film op i tableview som passer på det man søger på

    @FXML
    void seTrailer(ActionEvent event) throws IOException {
        //Åbner dialog med mediaafspilning
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(FilmApplication.class.getResource("fxml/filmAfspiller.fxml"));
        DialogPane categoryPane = fxmlLoader.load();

        AfspillerController afspillerController = fxmlLoader.getController();
        afspillerController.mp4(gemtMp4); //Sætter String i afspiller controlleren til at være mp4fil for valgt film

        Dialog<ButtonType> movieDialog = new Dialog<>();
        movieDialog.setDialogPane(categoryPane);
        movieDialog.setTitle("Watching trailer");
        movieDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ((Button) movieDialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Close and update seen date");
        ((Button) movieDialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Close without updating");

        Optional<ButtonType> knap = movieDialog.showAndWait();
        if (knap.get() == ButtonType.OK)
            try {
                afspillerController.stopMedie(); //Stopper med at afspille medie når vinduet lukkes
                mdi.updateSeenDate(Integer.parseInt(gemtId)); //Opdaterer seenDate til dags dato
                setLabels(); //Opdaterer labels inlk ny seenDate

            } catch (Exception e)
            {
                System.err.println("Fejl: " + e.getMessage());
                alert(e.getMessage());
            }
        if (knap.get() == ButtonType.CANCEL)
            try {
                afspillerController.stopMedie(); //Stopper medie og opdaterer ikke seenDate

            } catch (Exception e)
            {
                System.err.println("Fejl: " + e.getMessage());
                alert(e.getMessage());
            }
    } //Åbner dialog med mediaview

    @FXML
    void addActors(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(FilmApplication.class.getResource("fxml/actor.fxml"));
        DialogPane actorPane = fxmlLoader.load();

        ActorController actorController = fxmlLoader.getController();
        actorController.filmId(gemtId); //Sætter stirng i actorController til at være id af valgt film

        Dialog<ButtonType> actorDialog = new Dialog<>();
        actorDialog.setDialogPane(actorPane);
        actorDialog.setTitle("Watching trailer");
        actorDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> knap = actorDialog.showAndWait();
        if (knap.get() == ButtonType.OK)
            try {
                setLabels();
            } catch (Exception e)
            {
                System.err.println("Fejl: " + e.getMessage());
                alert(e.getMessage());
            }
    } //Åbner dialog med alle actors og tilføjfuntion

    @FXML
    void fjernActFraMov(ActionEvent event){
        ObservableList valgteIndeks = actorList.getSelectionModel().getSelectedIndices();
        if (valgteIndeks.size() == 0)
            System.out.println("Vælg en skuespiller");
        else
            for (Object indeks : valgteIndeks) {
                Actor ac = (Actor) actorList.getItems().get((int) indeks); //Henter objekt til brug af id
                amdi.sletActFraFilm(gemtId, ac.getId()); //Fjerner skuespiller fra vist film
            }
        setLabels();
    } //Fjerner en skuespiller fra filmen

    public void alert(String a){
        Dialog<ButtonType> alertDialog = new Dialog();
        // Her sættes et nyt vindue op
        alertDialog.setTitle("Alert!");
        alertDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        Label besked = new Label();
        besked.setTextFill(Color.RED);
        besked.setText(a); //Label text er den string der bliver givet når metoden bliver brugt
        alertDialog.getDialogPane().setContent(besked);
        Optional<ButtonType> knap = alertDialog.showAndWait();
    } //Dialog med alert besked til exception handling


    MovieDAO mdi = new MovieDAOImpl();
    CategoryDAO cdi = new CategoryDAOImpl();
    CatMovDAO cmdi = new CatMovDAOImpl();
    ActMovDAO amdi = new ActMovDAOImpl();


}