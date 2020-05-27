package jays;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jays.data.JsonWorker;
import jays.data.entity.SystemInfo;
import jays.utils.Directory;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource(Directory.FXML +"JMain.fxml"));
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

    public static Image getImage(String imageName,boolean colorIsHint){
        if (systemInfo().getTheme().equals("DarkMode") && colorIsHint)
            return new Image(App.class.getResource(Directory.DARK_HINT+imageName+".png").toString());
        else if (systemInfo().getTheme().equals("DarkMode") && !colorIsHint)
            return new Image(App.class.getResource(Directory.DARK_CONTAINER+imageName+".png").toString());
        else if (systemInfo().getTheme().equals("LightMode") && colorIsHint)
            return new Image(App.class.getResource(Directory.LIGHT_HINT+imageName+".png").toString());
        else if (systemInfo().getTheme().equals("LightMode") && !colorIsHint)
            return new Image(App.class.getResource(Directory.LIGHT_CONTAINER+imageName+".png").toString());
        else return null;
    }
}