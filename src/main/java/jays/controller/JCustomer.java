package jays.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jays.App;

import java.net.URL;
import java.util.ResourceBundle;

public class JCustomer implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private TableView<?> tvCustomer;
    @FXML private TableColumn<?, ?> colCustomer;
    @FXML private TableColumn<?, ?> colContact;
    @FXML private TableColumn<?, ?> colTotalRecord;
    @FXML private TableColumn<?, ?> colAction;
    @FXML private JFXButton btnCustomerAdd;
    @FXML private VBox igFirstName;
    @FXML private VBox igMiddleInitial;
    @FXML private VBox igLastName;
    @FXML private VBox igContactNum;
    @FXML private JFXButton mngCustomerCancel;
    @FXML private JFXButton mngCustomerSave;
    @FXML private JFXButton btnTransactionHistory;
    @FXML private TextField tfSearch;
    @FXML private JFXButton btnSearch;
    @FXML private ImageView ivSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ivSearch.setImage(App.getImage("j-search",false));
    }

    @FXML void btnCustomerAddOnAction(ActionEvent event) {

    }

    @FXML void btnSearchOnAction(ActionEvent event) {

    }

    @FXML void btnTransactionHistoryOnAction(ActionEvent event) {

    }

    @FXML void mngCustomerCancelOnAction(ActionEvent event) {

    }

    @FXML void mngCustomerSaveOnAction(ActionEvent event) {

    }


}
