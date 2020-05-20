package jays;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/fxml/JMain.fxml"));
        stage.setScene(new Scene(parent));
        stage.getScene().getStylesheets().add(String.valueOf(getClass().getResource("/style/DarkMode.css")));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}