package jays;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jays.utils.Directory;

import java.io.IOException;

public class App extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(Directory.FXML +"JLogin.fxml"));
        stage.setScene(new Scene(parent));
        stage.getScene().getStylesheets().add(String.valueOf(getClass().getResource(Directory.STYLE+"LightMode.css")));
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }

}