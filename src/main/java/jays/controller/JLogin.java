package jays.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import jays.App;
import jays.controller.component.DialogType;
import jays.controller.component.JDialogPopup;
import jays.data.AES;
import jays.data.DatabaseHandler;
import jays.data.JsonWorker;
import jays.data.entity.User;
import jays.utils.InputHandler;
import jays.utils.SceneRouter;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JLogin implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private VBox igUsername;
    @FXML private VBox igPassword;
    @FXML private JFXButton btnSignIn;

    private InputHandler usernameHandler;
    private InputHandler passwordHandler;

    private DatabaseHandler databaseHandler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameHandler = new InputHandler(igUsername);
        passwordHandler = new InputHandler(igPassword);
        databaseHandler = new DatabaseHandler();
    }


    @FXML void btnSignInOnAction(ActionEvent event) {
        rootPane.requestFocus();
        if (usernameHandler.getInputField().getText().equals("") || passwordHandler.getInputField().getText().equals("")){
            JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,
                    rootPane.getChildren().get(0),
                    "Access Denied",
                    "The username or password is cannot be empty",
                    DialogType.ERROR));
            if (usernameHandler.getInputField().getText().equals("")){
                usernameHandler.getInputSubIdentifier().setText("Username cannot be empty");
                usernameHandler.getInputSubIdentifier().setVisible(true);
            }
            if ( passwordHandler.getInputField().getText().equals("")){
                passwordHandler.getInputSubIdentifier().setText("Password cannot be empty");
                passwordHandler.getInputSubIdentifier().setVisible(true);
            }
        }else{
            JFXDialogLayout dialogLayout=JDialogPopup.createByType(rootPane,
                    rootPane.getChildren().get(0),
                    "Processing...",
                    "Retrieving data from server. Please wait...",DialogType.INFORM,false);
            JDialogPopup.showDialog(dialogLayout);

            Timeline fetchTimeline = new Timeline(new KeyFrame(Duration.ZERO,event1 -> {
                try {
                    databaseHandler.startConnection();
                    String sql = String.format("Select * from jays_user where user_name = '%s' and user_password = '%s'",
                            usernameHandler.getInputField().getText(),
                            AES.encrypt(passwordHandler.getInputField().getText(),"USER"));
                    ResultSet resultSet = databaseHandler.execQuery(sql);
                    if (resultSet.next()){
                        JsonWorker.toJson( new User(resultSet.getString("user_name"),resultSet.getInt("user_avatar")),"UserInfo");
                        databaseHandler.closeConnection();
                        SceneRouter.changeStage(rootPane,"JMain","Jak at your Service");
                    }else{
                        usernameHandler.getInputField().setText("");
                        passwordHandler.getInputField().setText("");
                        usernameHandler.getInputSubIdentifier().setText("");
                        passwordHandler.getInputSubIdentifier().setText("");
                        JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,
                                rootPane.getChildren().get(0),
                                "Access Denied",
                                "The username or password didn't match any account",DialogType.ERROR));
                        databaseHandler.closeConnection();
                    }

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    databaseHandler.closeConnection();
                }
            }),new KeyFrame(Duration.seconds(1)));

            fetchTimeline.setDelay(Duration.millis(1000));
            fetchTimeline.setCycleCount(1);
            fetchTimeline.setOnFinished(e->{
                JDialogPopup.closeDialog(dialogLayout);
                databaseHandler.closeConnection();
            });
            fetchTimeline.play();

        }


    }


}
