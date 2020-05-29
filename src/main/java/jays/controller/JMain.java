package jays.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import jays.App;
import jays.data.JsonWorker;
import jays.data.entity.User;
import jays.utils.Directory;
import jays.utils.SceneRouter;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class JMain implements Initializable {

  @FXML private StackPane rootPane;
  @FXML private JFXButton navTransaction;
  @FXML private JFXButton navServices;
  @FXML private JFXButton navCustomer;
  @FXML private JFXButton navRecord;
  @FXML private JFXButton navSettings;
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
    openedSceneMap.put(navSettings,false);
    ivDropDown.setImage(App.getImage("j-menu",true));
  }


  @FXML void navAction(ActionEvent event) {
    JFXButton btn = (JFXButton) event.getSource();
    openScene(btn);

  }

  private void openScene(JFXButton button){
    if (button.equals(navServices)) scanMap(navServices,"JServices");
    else if (button.equals(navCustomer))scanMap(navCustomer,"JCustomer");
    else if (button.equals(navTransaction))scanMap(navTransaction,"JTransaction");
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

}
