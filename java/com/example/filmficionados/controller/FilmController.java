package com.example.filmficionados.controller;

import com.example.filmficionados.FilmApplication;
import com.example.filmficionados.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class FilmController {

    @FXML
    private Label catLabel, dateLabel, directorLabel, titleLabel;

    @FXML
    private ImageView mPoster;

    @FXML
    private TextArea resumeText;

    @FXML
    private TextField søgefelt;

    @FXML
    private ListView movieList = new ListView(), categoryList = new ListView(), actorList = new ListView(), oldMovList = new ListView<>();


    private final ObservableList<Movie> moviesInCategoryObsList = FXCollections.observableArrayList();

    @FXML private TableView<Movie> catMovTable;
    @FXML private TableColumn<Movie, String> titleColumn, imdbColumn, myRateColumn, favColumn;
    private ImageView favView;


    private final Image emptyFav = new Image(String.valueOf(FilmApplication.class.getResource("Img/emptyStar.png")));
    private final Image filledFav = new Image(String.valueOf(FilmApplication.class.getResource("Img/filledStar.png")));

    private String gemtId;
    private String gemtMp4;

    public void initialize(){
        visFilm();
        visKategori();
        visOldFilm();
        categoryList.getSelectionModel().select(0);
        visKatFilm();
        catMovTable.getSelectionModel().selectFirst();
        setLabels();
    }

    public void visFilm(){
        //Viser alle film på hele biblioteket
        movieList.getItems().clear();
        List<Movie> film = mdi.getAlleFilm();
        for (Movie fi : film)
        {
            movieList.getItems().add(fi);
        }
    }

    public void visKategori(){
        //Viser alle film i den første "mappe" af kategorier
        categoryList.getItems().clear();
        List<Category> kategorier = cdi.getAllKategori();
        for (Category ka : kategorier)
        {
            categoryList.getItems().add(ka);
        }
    }

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
            moviesInCategoryObsList.addAll(cmdi.getKatFilm(kat));
            catMovTable.setItems(moviesInCategoryObsList);
            catMovTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            catLabel.setText(kat.getTitle());
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
            List<Actor> skuespiller = amdi.getActFilm(mov);
            for (Actor act : skuespiller)
            {
                actorList.getItems().add(act);
            }
            gemtId = mov.getId();
            gemtMp4 = mov.getFileLink();
        }
    } //Opdaterer information om den valgte film

    public void visOldFilm(){
        List<Movie> film = mdi.getOldFilm();
        for (Movie fi : film)
        {
            oldMovList.getItems().add(fi);
        }
    } //Viser film der ikke er blevet set i mere end 2 år
    @FXML
    void oldMovClick(MouseEvent event) {
        //Klikkes i listen med film der ikke er set længe
    } //Skal nok ikke bruges
    @FXML
    void getInfo(ActionEvent event) {
        //Viser information om en film fra listen med film der ikke er set længe
        ObservableList valgtFilm = oldMovList.getSelectionModel().getSelectedIndices();
        for (Object indeks : valgtFilm){
            Movie mov = (Movie) oldMovList.getItems().get((int) indeks);
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
        }
    } //Viser info om film valgt som ikke er set mere end 2 år
    @FXML
    void sletOldFilm(ActionEvent event){
        //Sletter valgt sang i listen fra gamle sange fra hele biblioteket
        ObservableList valgtFilm = oldMovList.getSelectionModel().getSelectedIndices();
        if (valgtFilm.size() == 0)
            System.out.println("Vælg en film");
        else
            for (Object indeks : valgtFilm)
            {
                Movie f  = (Movie) oldMovList.getItems().get((int) indeks);
                mdi.sletFilm(Integer.parseInt(f.getId()));
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

        MovController movController = fxmlLoader.getController();

        Dialog<ButtonType> mDialog = new Dialog<>();
        mDialog.setDialogPane(moviePane);
        mDialog.setTitle("Add new movie");
        mDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Optional<ButtonType> knap = mDialog.showAndWait();
        if (knap.get() == ButtonType.OK)
            try {
                mdi.tilføjFilm(movController.movTitle(),movController.director(),movController.resume(),
                        movController.IMDb(),movController.rating(),movController.link(),movController.img());
                visFilm();

            } catch (Exception e)
            {
                System.err.println("Fejl: " + e.getMessage());
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
                Movie f  = (Movie) movieList.getItems().get((int) indeks);
                mdi.sletFilm(Integer.parseInt(f.getId()));
                movieList.getItems().clear();
                visFilm();
            }
    } //Sletter film

    @FXML
    void catClick(MouseEvent event) {
        //Klikkes på en kategori
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
                cdi.tilføjKategori(catController.nyKategori());
                visKategori();

            } catch (Exception e)
            {
                System.err.println("Fejl: " + e.getMessage());
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
                cmdi.sletAlleFilmFraKat(Integer.parseInt(ka.getId()));
                cdi.sletKategori(Integer.parseInt(ka.getId()));
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
        cmdi.fjernY(c.getId()); //Updaterer databasen

        ObservableList valgtFilm = catMovTable.getSelectionModel().getSelectedIndices(); //Gemmer selected indeks til senere brug
        for (Object indeks : valgtFilm){ //Laver en liste med den oprindelig valgte film
            Movie mov = (Movie) catMovTable.getItems().get((int) indeks); //Laver object af hver film fra orindelig selevted film
            cmdi.sætY(mov.getId(), c.getId());
        }
        visKatFilm();
    } //Opdaterer hvilken film der er favorit i den bestemte kategori

    @FXML
    public void ændreRating(ActionEvent event) {
        Dialog<ButtonType> ratingDialog = new Dialog();
        // Her sættes et nyt vindue op
        ratingDialog.setTitle("Change your rating for this movie");
        ratingDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        TextField rating = new TextField();
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
                        Movie mr = (Movie) catMovTable.getItems().get((int) indeks);
                        mdi.updateRating(rating.getText(), Integer.parseInt(mr.getId()));
                        catMovTable.getItems().clear();
                        visKatFilm();
                    }

                //Laver en catch til hvis rating ikke bliver ændret
            } catch (Exception e) {
                System.err.println("Noget gik galt");
                System.err.println("Tjek om du har skrevet rating rigtigt");
                System.err.println("Fejl: " + e.getMessage());
            }
    } //Dialogopsæt uden fxml ændrer egen rating for film

    @FXML
    void sletFilmFraKat(ActionEvent event){
        ObservableList valgteIndeks = catMovTable.getSelectionModel().getSelectedIndices();
        if (valgteIndeks.size() == 0)
            System.out.println("Vælg en film");
        else
            for (Object indeks : valgteIndeks) {
                Movie fi = (Movie) catMovTable.getItems().get((int) indeks);
                Category ka = (Category) categoryList.getSelectionModel().getSelectedItem();
                cmdi.sletFilmFraKat(fi.getId(),ka.getId());
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
                cmdi.føjFilmTilKat(fi.getId(),ka.getId());

                catMovTable.getItems().clear();
                List<Movie> film = cmdi.getKatFilm(ka);
                for (Movie mov : film)
                {
                    catMovTable.getItems().add(mov);
                }
            }
    } //Tilføjer valgt film til valgt kategori

    @FXML
    void søgKnap(ActionEvent event) {
        //Viser alle film i databasen der matcher det der er blevet skrevet i søgefeltet
        List<Movie> søgtFilm = mdi.getSøgFilm(søgefelt.getText());
        catLabel.setText(søgefelt.getText());
        catMovTable.getItems().clear();
        for (Movie mov : søgtFilm)
        {
            catMovTable.getItems().add(mov);
        }
    } //Sætter film op i tableview som passer på det man søger på

    @FXML
    void seTrailer(ActionEvent event) throws IOException {
        //Åbner dialog med mediaafspilning
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(FilmApplication.class.getResource("fxml/filmAfspiller.fxml"));
        DialogPane categoryPane = fxmlLoader.load();

        AfspillerController afspillerController = fxmlLoader.getController();
        afspillerController.mp4(gemtMp4);

        Dialog<ButtonType> movieDialog = new Dialog<>();
        movieDialog.setDialogPane(categoryPane);
        movieDialog.setTitle("Watching trailer");
        movieDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        ((Button) movieDialog.getDialogPane().lookupButton(ButtonType.OK)).setText("Close and update seen date");
        ((Button) movieDialog.getDialogPane().lookupButton(ButtonType.CANCEL)).setText("Close without updating");

        Optional<ButtonType> knap = movieDialog.showAndWait();
        if (knap.get() == ButtonType.OK)
            try {
                afspillerController.stopMedie();
                mdi.updateSeenDate(Integer.parseInt(gemtId));
                setLabels();

            } catch (Exception e)
            {
                System.err.println("Fejl: " + e.getMessage());
            }
        if (knap.get() == ButtonType.CANCEL)
            try {
                afspillerController.stopMedie();

            } catch (Exception e)
            {
                System.err.println("Fejl: " + e.getMessage());
            }
    } //Åbner dialog med mediaview

    @FXML
    void addActors(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(FilmApplication.class.getResource("fxml/actor.fxml"));
        DialogPane actorPane = fxmlLoader.load();

        ActorController actorController = fxmlLoader.getController();
        actorController.filmId(gemtId);

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
            }
    }

    @FXML
    void fjernActFraMov(ActionEvent event){
        ObservableList valgteIndeks = actorList.getSelectionModel().getSelectedIndices();
        if (valgteIndeks.size() == 0)
            System.out.println("Vælg en skuespiller");
        else
            for (Object indeks : valgteIndeks) {
                Actor ac = (Actor) actorList.getItems().get((int) indeks);
                amdi.sletActFraFilm(gemtId,ac.getId());
            }
        setLabels();
    } //Fjerner en skuespiller fra filmen



    MovieDAO mdi = new MovieDAOImpl();
    CategoryDAO cdi = new CategoryDAOImpl();
    CatMovDAO cmdi = new CatMovDAOImpl();
    ActorDAO adi = new ActorDAOImpl();
    ActMovDAO amdi = new ActMovDAOImpl();


}