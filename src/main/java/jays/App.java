package jays;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jays.data.JsonWorker;
import jays.data.entity.SystemInfo;
import jays.utils.Directory;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(Directory.FXML +"JLogin.fxml"));
        stage.setScene(new Scene(parent));
        stage.getScene().getStylesheets().add(styleSheet());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static SystemInfo systemInfo(){
        return (SystemInfo) JsonWorker.fromJson("SystemInfo",SystemInfo.class);
    }

    public static String styleSheet(){
        return systemInfo().getTheme().equals("DarkMode")
                ? App.class.getResource(Directory.STYLE+"DarkMode.css").toString()
                : App.class.getResource(Directory.STYLE+"LightMode.css").toString();
    }

}