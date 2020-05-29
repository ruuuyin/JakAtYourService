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

import java.io.File;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        File file = new File(JSON_DIR);
        if (!file.exists()){
            Parent parent = FXMLLoader.load(getClass().getResource(Directory.FXML +"JCreateAccount.fxml"));
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Setup");
            stage.centerOnScreen();
            stage.setMaxHeight(scene.getHeight());
            stage.setMaxWidth(scene.getWidth());
            stage.setMinWidth(scene.getWidth());
            stage.setMinHeight(scene.getHeight());
            stage.getScene().getStylesheets().add(App.class.getResource(Directory.STYLE+"LightMode.css").toString());
            stage.show();
        }else{
            Parent parent = FXMLLoader.load(getClass().getResource(Directory.FXML +"JLogin.fxml"));
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setMaxHeight(scene.getHeight());
            stage.setMaxWidth(scene.getWidth());
            stage.setMinWidth(scene.getWidth());
            stage.setMinHeight(scene.getHeight());
            stage.getScene().getStylesheets().add(styleSheet());
            stage.show();
        }

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
    private static final String JSON_DIR = System.getProperty("user.home")+"\\.jays_metadata\\";
    public static void clearData(String json){
        File file = new File(JSON_DIR+"\\"+json+".json");
        file.delete();
    }
}