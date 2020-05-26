package jays.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import jays.utils.SceneRouter;

import java.net.URL;
import java.util.ResourceBundle;

public class JMain implements Initializable {

  @FXML
  private StackPane rootPane;

  @FXML
  private JFXButton navTransaction;

  @FXML
  private JFXButton navServices;

  @FXML
  private JFXButton navCustomer;

  @FXML
  private JFXButton navRecord;

  @FXML
  private JFXButton navSettings;

  @FXML
  private Label lblUser;

  @FXML
  private ImageView icAvatar;

  @FXML
  private ImageView ivDropDown;

  @FXML
  private AnchorPane subSceneContainer;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    SceneRouter.attachNode(subSceneContainer, "JServices");
  }

}
