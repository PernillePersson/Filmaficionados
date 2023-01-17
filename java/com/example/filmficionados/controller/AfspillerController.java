package com.example.filmficionados.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AfspillerController{

    @FXML
    private ImageView lydBillede, playPauseBillede;

    @FXML
    private Image mute, audio, play, pause;

    @FXML
    private Slider volumeSlider;

    @FXML
    private MediaView filmView;

    private Duration length; //Viser hvor lang en trailer varer

    private File fil;
    private Media film;
    private static MediaPlayer filmAfspiller;
    private String mp4String;

    public void initialize(){
        playPauseBillede.setImage(play);
        lydBillede.setImage(audio);
        ændreLyd();
    }

    @FXML
    void lydKnapKlik(ActionEvent event) {
        if (lydBillede.getImage() == audio){
            volumeSlider.adjustValue(0.0);
            lydBillede.setImage(mute);
        }else if (lydBillede.getImage() == mute){
            volumeSlider.adjustValue(30.0);
            lydBillede.setImage(audio);
        }
    }

    public void ændreLyd(){
        //Updater volume med volumeslider
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1)
            {
                try
                {
                    filmAfspiller.setVolume(volumeSlider.getValue()/100);
                }
                //Laver en catch til hvis lydstyrken ikke kunne ændres
                catch (Exception e){
                    System.err.println("Der opstod et problem " + e.getMessage());
                }
                if (volumeSlider.getValue() == 0.0)
                    lydBillede.setImage(mute);
                else
                    lydBillede.setImage(audio);
            }
        });
    }

    @FXML
    void playPauseMedia(ActionEvent event) {
        if (playPauseBillede.getImage() == play) {
            filmAfspiller.seek(length);
            filmAfspiller.play();
            playPauseBillede.setImage(pause);
        }
        // Pause valgte sang tilstand
        else if (playPauseBillede.getImage() == pause) {
            length = filmAfspiller.getCurrentTime();
            filmAfspiller.pause();
            playPauseBillede.setImage(play);
        }
    }

    public void opsætMedie(){
        fil = new File("/Users/pernillepersson/IdeaProjects/Filmficionados/src/main/resources/com/example/filmficionados/mp4/" + mp4String);
        film = new Media(fil.toURI().toString());
        filmAfspiller = new MediaPlayer(film);
        filmView.setMediaPlayer(filmAfspiller);
    }

    public void mp4(String m){
        mp4String = m;
        opsætMedie();
    }

    public void stopMedie(){
        filmAfspiller.dispose();
    }
}
