package jays.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jays.controller.component.DialogType;
import jays.controller.component.JDialogPopup;

import java.net.URL;
import java.util.ResourceBundle;

public class JLogin implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private VBox igUsername;

    @FXML
    private VBox igPassword;

    @FXML
    private JFXButton btnSignIn;

    @FXML
    void btnSignInOnAction(ActionEvent event) {
        JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,rootPane.getChildren().get(0),"Access Granted","Body", DialogType.SUCCESS));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
