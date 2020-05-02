package jays.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneRouter {
    private Stage stage;
    private Parent root;
    private Scene scene;


    private Parent getFxml(String fxmlName){
        try {
            return FXMLLoader.load(getClass().getResource("fxml/"+fxmlName+".fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Stage getStage(Pane rootPane){
        return (Stage) rootPane.getScene().getWindow();
    }

    public Stage getStage(){
        return stage;
    }

    public void clearChildNode(Pane parent){
        if (parent.getChildren().size() > 0)
            parent.getChildren().clear();
    }

    public void closeStage(Pane rootPane){
        getStage(rootPane).close();
    }

    public SceneRouter openStage(String fxmlName,String title){
        root = getFxml(fxmlName);
        scene = new Scene(root);

        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
        return this;
    }

    public SceneRouter changeStage(Pane rootPane, String fxmlName,String title){
        root = getFxml(fxmlName);
        scene = new Scene(root);

        closeStage(rootPane);

        stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(title);
        stage.show();
        return this;
    }

    public SceneRouter attachNode(AnchorPane parent,String fxmlName){
        root = getFxml(fxmlName);
        stage = getStage(parent);

        AnchorPane.setBottomAnchor(root,0.0);
        AnchorPane.setTopAnchor(root,0.0);
        AnchorPane.setRightAnchor(root,0.0);
        AnchorPane.setLeftAnchor(root,0.0);

        clearChildNode(parent);
        parent.getChildren().addAll(root);
        return this;
    }

    public void changeScene(Pane rootPane,String fxmlName,String title){
        root = getFxml(fxmlName);
        stage = getStage(rootPane);
        stage.setTitle(title);

        boolean fullScreenFlag = stage.isFullScreen(),
                maximizeFlag = stage.isMaximized();

        double ww = stage.getScene().getWidth(),
               hh = stage.getScene().getHeight();

        scene = new Scene(root,ww,hh);
        stage.setScene(scene);

        stage.setMaximized(maximizeFlag);
        stage.setFullScreen(fullScreenFlag);
        stage.setFullScreenExitHint("");
    }
}
