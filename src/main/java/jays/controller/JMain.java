package jays.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jays.App;
import jays.controller.component.JDialogPopup;
import jays.data.JsonWorker;
import jays.data.entity.SystemInfo;
import jays.data.entity.User;
import jays.utils.Directory;
import jays.utils.SceneRouter;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class JMain implements Initializable {

  @FXML private StackPane rootPane;
  @FXML private JFXButton navTransaction;
  @FXML private JFXButton navServices;
  @FXML private JFXButton navCustomer;
  @FXML private JFXButton navRecord;
  @FXML private Label lblUser;
  @FXML private ImageView icAvatar;
  @FXML private ImageView ivDropDown;
  @FXML private AnchorPane subSceneContainer;

  private HashMap<JFXButton,Boolean> openedSceneMap;

  @Override public void initialize(URL location, ResourceBundle resources) {
    User user  = (User) JsonWorker.fromJson("UserInfo",User.class);
    icAvatar.setImage(new Image(getClass().getResource(Directory.AVATARS+user.getAvatar()+".png").toString()));
    lblUser.setText("@"+user.getUser());
    openedSceneMap = new HashMap<>();
    openedSceneMap.put(navTransaction,false);
    openedSceneMap.put(navServices,false);
    openedSceneMap.put(navCustomer,false);
    openedSceneMap.put(navRecord,false);
    ivDropDown.setImage(App.getImage("j-menu",true));
  }


  @FXML void navAction(ActionEvent event) {
    JFXButton btn = (JFXButton) event.getSource();
    openScene(btn);
  }


  @FXML void logoOnClick(MouseEvent event) {

  }
  private JFXDialogLayout dialogLayout;
  private JDialogPopup dialogPopup;
  private JFXDialog dialog;

  @FXML void menuOnClick(MouseEvent event) {
    dialogLayout = JDialogPopup.createContentOnly(rootPane,rootPane.getChildren().get(0),"Select an Action",true,generateMenuSelection());
    JDialogPopup.showDialog(dialogLayout);
  }

  @FXML void avatarOnClick(MouseEvent mouseEvent) {
  }

  @FXML void changeName(MouseEvent mouseEvent) {
  }


  private void openScene(JFXButton button){
    if (button.equals(navServices)) scanMap(navServices,"JServices");
    else if (button.equals(navCustomer))scanMap(navCustomer,"JCustomer");
    else if (button.equals(navTransaction))scanMap(navTransaction,"JTransaction");
    else if (button.equals(navRecord))scanMap(navRecord,"JRecord");
  }

  private void scanMap(JFXButton btn,String fxmlName){
    if (!openedSceneMap.get(btn)){
      openedSceneMap.forEach((k,v)->{
        openedSceneMap.replace(k,false);
      });
      SceneRouter.attachNode(subSceneContainer,fxmlName);
      openedSceneMap.put(btn,true);
    }
  }





  private JFXButton generateChangeUserName(){
    JFXButton btn = new JFXButton("Change Username");
    btn.setMaxWidth(Double.MAX_VALUE);
    VBox.setMargin(btn,new Insets(5.0));
    btn.getStyleClass().add("buttonNormal");
    btn.setOnAction(event -> {
      //TODO set Action here
    });
    return btn;
  }

  private JFXButton generateChangePassword(){
    JFXButton btn = new JFXButton("Change Password");
    btn.setMaxWidth(Double.MAX_VALUE);
    VBox.setMargin(btn,new Insets(5.0));
    btn.getStyleClass().add("buttonNormal");
    btn.setOnAction(event -> {
      //TODO set Action here
    });
    return btn;
  }

  private JFXButton generateChangeAvatar(){
    JFXButton btn = new JFXButton("Change Avatar");
    btn.setMaxWidth(Double.MAX_VALUE);
    VBox.setMargin(btn,new Insets(5.0));
    btn.getStyleClass().add("buttonNormal");
    btn.setOnAction(event -> {
      //TODO set Action here
    });
    return btn;
  }

  private JFXButton generateThemSwitcher(){
    JFXButton btn = new JFXButton(App.systemInfo().getTheme().toLowerCase().equals("DarkMode")?"Switch to Light Mode":"Switch to Dark Mode");
    btn.setMaxWidth(Double.MAX_VALUE);
    VBox.setMargin(btn,new Insets(5.0));
    btn.getStyleClass().add("buttonImportantOutlined");
    btn.setOnAction(event -> {
      if (App.systemInfo().getTheme().equals("DarkMode")){
        SystemInfo systemInfo = new SystemInfo(App.systemInfo().getSystemVersion(),App.systemInfo().getSystemName(),"LightMode");
        JsonWorker.toJson(systemInfo,"SystemInfo");
        rootPane.getScene().getStylesheets().setAll(App.styleSheet());
      }else{
        SystemInfo systemInfo = new SystemInfo(App.systemInfo().getSystemVersion(),App.systemInfo().getSystemName(),"DarkMode");
        JsonWorker.toJson(systemInfo,"SystemInfo");
        rootPane.getScene().getStylesheets().setAll(App.styleSheet());
      }
      JDialogPopup.closeDialog(dialogLayout);
    });
    return btn;
  }

  private JFXButton generateSignOut(){
    JFXButton btn = new JFXButton("Sign Out");
    btn.setMaxWidth(Double.MAX_VALUE);
    VBox.setMargin(btn,new Insets(5.0));
    btn.getStyleClass().add("buttonDangerOutlined");
    btn.setOnAction(event -> {
      //TODO set Action here
    });
    return btn;
  }

  private VBox generateMenuSelection(){
    VBox vBox = new VBox();
    vBox.setPrefSize(200, Region.USE_COMPUTED_SIZE);
    vBox.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
    vBox.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
    vBox.getChildren().setAll(generateChangeUserName(),generateChangePassword(),generateChangeAvatar(),generateThemSwitcher(),generateSignOut());
    return vBox;
  }

}
