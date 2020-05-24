package jays.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import jays.utils.SceneRouter;

public class JMain implements Initializable {

  @FXML
  private StackPane rootPane;

  @FXML
  private AnchorPane subSceneContainer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    SceneRouter sceneRouter = new SceneRouter();
    sceneRouter.attachNode(subSceneContainer, "JServices");
  }

}
