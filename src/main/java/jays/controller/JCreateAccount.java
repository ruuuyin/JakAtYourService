package jays.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import jays.controller.component.DialogType;
import jays.controller.component.JDialogPopup;
import jays.data.AES;
import jays.data.DatabaseHandler;
import jays.data.JsonWorker;
import jays.data.entity.SystemInfo;
import jays.utils.SceneRouter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

public class JCreateAccount implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField tfUserName;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private FlowPane fpAvatarContainer;

    @FXML
    private ImageView icAvatar1;

    @FXML
    private ImageView icAvatar2;

    @FXML
    private ImageView icAvatar3;

    @FXML
    private ImageView icAvatar4;

    @FXML
    private ImageView icAvatar11;

    @FXML
    private ImageView icAvatar21;

    @FXML
    private ImageView icAvatar31;

    @FXML
    private ImageView icAvatar5;

    @FXML
    private ImageView icAvatar12;

    @FXML
    private ImageView icAvatar22;

    @FXML
    private ImageView icAvatar32;

    @FXML
    private ImageView icAvatar6;

    @FXML
    private ImageView icAvatar13;

    @FXML
    private ImageView icAvatar23;

    @FXML
    private ImageView icAvatar33;

    private int selectedAvatar=0;
    @FXML
    void avatarOnMouseClicked(MouseEvent event) {
        ImageView selectedImage = (ImageView) event.getSource();
        AtomicInteger img = new AtomicInteger();
        fpAvatarContainer.getChildren().forEach(hbox ->{
            img.getAndIncrement();
            HBox hb = (HBox) hbox;
            hb.setStyle("-fx-border-radius:50px;" +
                    "    -fx-background-radius:50px;" +
                    "    -fx-border-width:2px;");
            if (hb.getChildren().get(0).equals(selectedImage))selectedAvatar = img.get();
        });
        selectedImage.getParent().setStyle("-fx-border-radius:50px;" +
                "    -fx-background-radius:50px;" +
                "    -fx-border-width:2px;" +
                "    -fx-border-color:#38b2ac;");
    }

    @FXML
    void btnCancelOnAction(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void btnProceedOnAction(ActionEvent event) {
        if (tfPassword.getText().equals("") || tfUserName.getText().equals("")){
            JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,rootPane.getChildren().get(0),"Error","Invalid username or password", DialogType.ERROR,true));
        }else if(selectedAvatar==0){
            JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,rootPane.getChildren().get(0),"Error","Please select an avatar", DialogType.ERROR,true));
        } else{
            DatabaseHandler dbHandler = new DatabaseHandler();
            dbHandler.startConnection();
            dbHandler.execUpdate(String.format("Insert into jays_user(user_name,user_password,user_avatar) values('%s','%s',%s)",tfUserName.getText(), AES.encrypt(tfPassword.getText(),"USER"),selectedAvatar));
            dbHandler.closeConnection();
            JsonWorker.toJson(new SystemInfo("1.0.0","Jak At Your Service","LightMode"),"SystemInfo");
            SceneRouter.changeStage(rootPane,"JLogin","Login");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
