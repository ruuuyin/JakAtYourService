package jays.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import jays.App;
import jays.utils.SceneRouter;

import java.net.URL;
import java.util.ResourceBundle;

public class JHome implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private JFXButton btnTransaction;
    @FXML private JFXButton btnService;
    @FXML private JFXButton btnCustomer;
    @FXML private JFXButton btnRecord;
    @FXML private Label lblVersion;

    @FXML void btnOnAction(ActionEvent event) {
        JFXButton button = (JFXButton) event.getSource();
        if (button.equals(btnTransaction)) SceneRouter.attachNode((AnchorPane) rootPane.getParent(),"JTransaction");
        else if(button.equals(btnService)) SceneRouter.attachNode((AnchorPane) rootPane.getParent(),"JServices");
        else if(button.equals(btnCustomer)) SceneRouter.attachNode((AnchorPane) rootPane.getParent(),"JCustomer");
        else if(button.equals(btnRecord)) SceneRouter.attachNode((AnchorPane) rootPane.getParent(),"JRecord");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        lblVersion.setText("Version "+ App.systemInfo().getSystemVersion());
    }
}
