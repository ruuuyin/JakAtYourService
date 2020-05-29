package jays.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import jays.App;
import jays.controller.component.DialogType;
import jays.controller.component.JDialogPopup;
import jays.data.AES;
import jays.data.DatabaseHandler;
import jays.data.JsonWorker;
import jays.data.entity.SystemInfo;
import jays.data.entity.User;
import jays.utils.Directory;
import jays.utils.SceneRouter;

import java.net.URL;
import java.sql.ResultSet;
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
  private JFXDialogLayout dialogLayout;
  private JDialogPopup dialogPopup;
  private JFXDialog dialog;
  private User user;
  private HashMap<JFXButton,Boolean> openedSceneMap;
  private static BoxBlur blur = new BoxBlur(4,4,4);

  @Override public void initialize(URL location, ResourceBundle resources) {
    user  = (User) JsonWorker.fromJson("UserInfo",User.class);
    icAvatar.setImage(new Image(getClass().getResource(Directory.AVATARS+user.getAvatar()+".png").toString()));
    lblUser.setText("@"+user.getUser());
    openedSceneMap = new HashMap<>();
    openedSceneMap.put(navTransaction,false);
    openedSceneMap.put(navServices,false);
    openedSceneMap.put(navCustomer,false);
    openedSceneMap.put(navRecord,false);
    ivDropDown.setImage(App.getImage("j-menu",true));
    SceneRouter.attachNode(subSceneContainer,"JHome");
  }

  @FXML void navAction(ActionEvent event) {
    JFXButton btn = (JFXButton) event.getSource();
    openScene(btn);
  }

  @FXML void logoOnClick(MouseEvent event) {
      SceneRouter.attachNode(subSceneContainer,"JHome");
  }

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
      JDialogPopup.closeDialog(dialogLayout);
      showChangeUsername();
    });
    return btn;
  }

  private JFXButton generateChangePassword(){
    JFXButton btn = new JFXButton("Change Password");
    btn.setMaxWidth(Double.MAX_VALUE);
    VBox.setMargin(btn,new Insets(5.0));
    btn.getStyleClass().add("buttonNormal");
    btn.setOnAction(event -> {
      JDialogPopup.closeDialog(dialogLayout);
      showChangePassword();
    });
    return btn;
  }

  private JFXButton generateChangeAvatar(){
    JFXButton btn = new JFXButton("Change Avatar");
    btn.setMaxWidth(Double.MAX_VALUE);
    VBox.setMargin(btn,new Insets(5.0));
    btn.getStyleClass().add("buttonNormal");
    btn.setOnAction(event -> {
      JDialogPopup.closeDialog(dialogLayout);
      showAvatarSelector();
    });
    return btn;
  }

  private JFXButton generateThemSwitcher(){
    JFXButton btn = new JFXButton(App.systemInfo().getTheme().equals("DarkMode")?"Switch to Light Mode":"Switch to Dark Mode");
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
      ivDropDown.setImage(App.getImage("j-menu",true));
    });
    return btn;
  }

  private JFXButton generateSignOut(){
    JFXButton btn = new JFXButton("Sign Out");
    btn.setMaxWidth(Double.MAX_VALUE);
    VBox.setMargin(btn,new Insets(5.0));
    btn.getStyleClass().add("buttonDangerOutlined");
    btn.setOnAction(event -> {
      App.clearData("UserInfo");
      SceneRouter.changeStage(rootPane,"JLogin","Jak at your Service");
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

  private void showChangeUsername(){
    dialogLayout= new JFXDialogLayout();
    dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.TOP);
    dialog.setOverlayClose(false);
    Label header = new Label("Change Username");
    header.getStyleClass().add("headerLabel");

    TextField input = new TextField(user.getUser());
    input.setPromptText("Enter your new username here");
    input.setPrefSize(441.0,34.0);
    input.setFont(new Font("Segoe UI",16));


    Label errorDisplayer = new Label();
    errorDisplayer.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
    errorDisplayer.setPrefSize(Region.USE_COMPUTED_SIZE,16.0);
    errorDisplayer.getStyleClass().add("errorLabel");
    errorDisplayer.setVisible(false);
    VBox.setVgrow(errorDisplayer, Priority.ALWAYS);

    VBox content = new VBox();
    content.setPrefHeight(Region.USE_COMPUTED_SIZE);
    content.setPrefWidth(Region.USE_COMPUTED_SIZE);
    content.getChildren().addAll(errorDisplayer,input);

    JFXButton cancel = new JFXButton("Cancel");
    cancel.setPrefWidth(120);
    cancel.getStyleClass().addAll("buttonNormal","buttonTextSecondary");
    cancel.setOnAction(event1 -> {
      dialog.close();
    });

    JFXButton btnAdd = new JFXButton("Save");
    btnAdd.setPrefWidth(120);
    btnAdd.getStyleClass().addAll("buttonImportantOutlined","buttonTextSecondary");
    btnAdd.setOnAction(event1 -> {

        if (input.getText().equals("")){
          errorDisplayer.setText("Please fill the input field");
          errorDisplayer.setVisible(true);
        }else if(user.getUser().equals(input.getText())){
          dialog.close();
        }else{
          try {
            DatabaseHandler handler = new DatabaseHandler();
            handler.startConnection();
            handler.execUpdate(String.format("update jays_user set user_name = '%s' where user_name = '%s'",
                    input.getText(),user.getUser()));
            lblUser.setText("@"+input.getText());
            handler.closeConnection();
          }catch (Exception e){
            System.out.println(e.getMessage());
            //TODO add action here when the user enters existing username
          }
          dialog.close();
        }

      //TODO Add update here
    });

    dialog.setOnDialogOpened(event -> rootPane.getChildren().get(0).setEffect(blur));
    dialog.setOnDialogClosed(event -> rootPane.getChildren().get(0).setEffect(null));
    dialogLayout.setHeading(header);
    dialogLayout.setBody(content);
    dialogLayout.setActions(cancel,btnAdd);

    dialog.show();
  }

  private void showChangePassword(){
    dialogLayout= new JFXDialogLayout();
    dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.TOP);
    dialog.setOverlayClose(false);
    Label header = new Label("Change Password");
    header.getStyleClass().add("headerLabel");

    PasswordField oldInput = new PasswordField();
    oldInput.setPromptText("Old Password");
    oldInput.setPrefSize(441.0,34.0);
    oldInput.setFont(new Font("Segoe UI",16));

    PasswordField newInput = new PasswordField();
    newInput.setPromptText("New Password");
    newInput.setPrefSize(441.0,34.0);
    newInput.setFont(new Font("Segoe UI",16));


    Label errorDisplayer = new Label();
    errorDisplayer.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
    errorDisplayer.setPrefSize(Region.USE_COMPUTED_SIZE,16.0);
    errorDisplayer.getStyleClass().add("errorLabel");
    errorDisplayer.setVisible(false);
    VBox.setVgrow(errorDisplayer, Priority.ALWAYS);

    VBox content = new VBox(2);
    content.setPrefHeight(Region.USE_COMPUTED_SIZE);
    content.setPrefWidth(Region.USE_COMPUTED_SIZE);
    content.getChildren().addAll(errorDisplayer,oldInput,newInput);

    JFXButton cancel = new JFXButton("Cancel");
    cancel.setPrefWidth(120);
    cancel.getStyleClass().addAll("buttonNormal","buttonTextSecondary");
    cancel.setOnAction(event1 -> {
      dialog.close();
    });

    JFXButton btnAdd = new JFXButton("Save");
    btnAdd.setPrefWidth(120);
    btnAdd.getStyleClass().addAll("buttonImportantOutlined","buttonTextSecondary");
    btnAdd.setOnAction(event1 -> {

      if (oldInput.getText().equals("") || newInput.getText().equals("")){
        errorDisplayer.setText("Please fill the input field");
        errorDisplayer.setVisible(true);
      }else{
        try {
          DatabaseHandler handler = new DatabaseHandler();
          handler.startConnection();
          String sql = String.format("Select * from jays_user where user_name = '%s' and user_password = '%s'",
                  lblUser.getText().substring(1),
                  AES.encrypt(oldInput.getText(),"USER"));
          ResultSet resultSet = handler.execQuery(sql);
          if (resultSet.next()){
            handler.closeConnection();
            handler.startConnection();
            handler.execUpdate(String.format("update jays_user set user_password = '%s' where user_name = '%s'",
                    AES.encrypt(newInput.getText(),"USER"),user.getUser()));
            JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,rootPane.getChildren().get(0),"Update Succes","Password has been updated",DialogType.SUCCESS));
            handler.closeConnection();
            dialog.close();
          }else{
            errorDisplayer.setText("You've entered wrong password");
            errorDisplayer.setVisible(true);
            handler.closeConnection();
          }
        }catch (Exception e){
          System.out.println(e.getMessage());
        }
      }
    });

    dialog.setOnDialogOpened(event -> rootPane.getChildren().get(0).setEffect(blur));
    dialog.setOnDialogClosed(event -> rootPane.getChildren().get(0).setEffect(null));
    dialogLayout.setHeading(header);
    dialogLayout.setBody(content);
    dialogLayout.setActions(cancel,btnAdd);

    dialog.show();
  }

  private FlowPane avatarsContainer;

  private HBox imageContainer(int imageNum){
    HBox hBox = new HBox();
    hBox.setMaxSize(85,85);
    hBox.setPrefSize(85,85);
    hBox.setMinSize(85,85);
    hBox.setAlignment(Pos.CENTER);
    hBox.setId("avatarID-"+imageNum);
    ImageView imageView = new ImageView(new Image(getClass().getResource(Directory.AVATARS+imageNum+".png").toString(),80,80,true,true));
    hBox.setStyle(user.getAvatar()!=imageNum
            ?
            "-fx-border-radius:50px;" +
            "    -fx-background-radius:50px;" +
            "    -fx-border-width:2px;"
            :
            "-fx-border-radius:50px;" +
            "    -fx-background-radius:50px;" +
            "    -fx-border-width:2px;" +
            "    -fx-border-color:#38b2ac;"
            );
    imageView.setFitWidth(80);
    imageView.setFitHeight(80);
    imageView.setOnMouseClicked(e->{
      for (Node child : avatarsContainer.getChildren()) {
        child.setStyle("-fx-border-radius:50px;" +
                "    -fx-background-radius:50px;" +
                "    -fx-border-width:2px;");
      }
      hBox.setStyle("-fx-border-radius:50px;" +
              "    -fx-background-radius:50px;" +
              "    -fx-border-width:2px;" +
              "    -fx-border-color:#38b2ac;");
      selectedAvatar = imageNum;
    });
    hBox.getChildren().add(imageView);
    return hBox;
  }

  private FlowPane generateFlowPane(){
    avatarsContainer = new FlowPane(10,10);
    avatarsContainer.setMaxSize(393,366);
    avatarsContainer.setPrefSize(393,366);
    avatarsContainer.setMinSize(393,366);
    avatarsContainer.setAlignment(Pos.CENTER);
    for (int i = 1; i <=16 ; i++) {
      avatarsContainer.getChildren().add(imageContainer(i));
    }
    return avatarsContainer;
  }

  private int selectedAvatar=0;
  private void showAvatarSelector(){
    dialogLayout= new JFXDialogLayout();
    dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.TOP);
    dialog.setOverlayClose(false);
    Label header = new Label("Select Avatar");
    header.getStyleClass().add("headerLabel");



    JFXButton cancel = new JFXButton("Cancel");
    cancel.setPrefWidth(120);
    cancel.getStyleClass().addAll("buttonNormal","buttonTextSecondary");
    cancel.setOnAction(event1 -> {
      dialog.close();
    });

    JFXButton btnAdd = new JFXButton("Save");
    btnAdd.setPrefWidth(120);
    btnAdd.getStyleClass().addAll("buttonImportantOutlined","buttonTextSecondary");
    btnAdd.setOnAction(event1 -> {
      DatabaseHandler handler = new DatabaseHandler();
      handler.startConnection();
      handler.execUpdate("Update jays_user set user_avatar = "+selectedAvatar +" where user_name = '"+lblUser.getText().split("@")[0]+"'");
      handler.closeConnection();
      icAvatar.setImage(new Image(getClass().getResource(Directory.AVATARS+selectedAvatar+".png").toString()));
      dialog.close();
    });

    dialog.setOnDialogOpened(event -> rootPane.getChildren().get(0).setEffect(blur));
    dialog.setOnDialogClosed(event -> rootPane.getChildren().get(0).setEffect(null));
    dialogLayout.setHeading(header);
    dialogLayout.setBody(generateFlowPane());
    dialogLayout.setActions(cancel,btnAdd);

    dialog.show();
  }
}
