package jays.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jays.App;
import jays.controller.component.*;
import jays.data.DatabaseHandler;
import jays.utils.InputHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JCustomer implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private TableView<CustomerData> tvCustomer;
    @FXML private TableColumn<CustomerData, String> colCustomer;
    @FXML private TableColumn<CustomerData, String> colContact;
    @FXML private TableColumn<CustomerData, HBox> colAction;
    @FXML private JFXButton btnCustomerAdd;
    @FXML private VBox igFirstName;
    @FXML private VBox igMiddleInitial;
    @FXML private VBox igLastName;
    @FXML private VBox igContactNum;
    @FXML private HBox mngCustomerBtns;
    @FXML private JFXButton mngCustomerCancel;
    @FXML private JFXButton mngCustomerSave;
    @FXML private JFXButton btnTransactionHistory;
    @FXML private ListView<?> lvTransactionHistory;
    @FXML private TextField tfSearch;
    @FXML private JFXButton btnSearch;
    @FXML private ImageView ivSearch;
    @FXML private VBox vbCustomer;
    @FXML private AnchorPane apTableContainer;
    @FXML private VBox vbTransaction;

    private DatabaseHandler dbHandler;
    private static TableLoader<CustomerData> tableLoader;
    public static StackPane staticNode;
    private CustomerData selectedData = null;
    private JFXDialogLayout dialogLayout;
    private JFXDialog dialog;
    private InputHandler inputFirstName;
    private InputHandler inputMiddle;
    private InputHandler inputLast;
    private InputHandler inputContact;

    @FXML void btnCustomerAddOnAction(ActionEvent event) {
        resetFields();
        setFieldsDisable(true,false);
        focusManageCustomer(true);
    }

    @FXML void btnSearchOnAction(ActionEvent event) {

    }

    @FXML void btnTransactionHistoryOnAction(ActionEvent event) {

    }

    @FXML void mngCustomerCancelOnAction(ActionEvent event) {
        setFieldsDisable(true,true);
        resetFields();
    }

    @FXML void mngCustomerSaveOnAction(ActionEvent event) {
        String first = inputFirstName.getInputField().getText();
        String middle = inputMiddle.getInputField().getText();
        String last = inputLast.getInputField().getText();
        String contact = inputContact.getInputField().getText();

        if (first.equals("") || last.equals("") || contact.equals("")){
            JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,
                    rootPane.getChildren().get(0),
                    "Operation Failed",
                    "Please fill all the required fields",
                    DialogType.ERROR));
        }else{
            if (mngCustomerSave.getText().equals("Add")){
                dbHandler.startConnection();
                String sql = String.format("insert into jays_customer(customer_first,customer_middle,customer_last,customer_phone) " +
                                "values('%s','%s','%s','%s')",
                        first,(middle.equals("")?"N/A":middle),last,contact);
                dbHandler.execUpdate(sql);
                dbHandler.closeConnection();
                setFieldsDisable(false,true);
                focusManageCustomer(false);
                JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,
                        rootPane.getChildren().get(0),
                        "Operation Success",
                        "New service has been added",
                        DialogType.SUCCESS));
                this.queryAllServices();
            }else{
                dbHandler.startConnection();
                String sql = String.format("update jays_customer set customer_first='%s',customer_middle='%s',customer_last='%s',customer_phone='%s' where customer_id = %s",
                        first,(middle.equals("")?"N/A":middle),last,contact,selectedData.getCustomer_id());
                dbHandler.execUpdate(sql);
                dbHandler.closeConnection();
                setFieldsDisable(false,true);
                resetFields();
                JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,
                        rootPane.getChildren().get(0),
                        "Operation Success",
                        "Service has been updated",
                        DialogType.SUCCESS));
                queryAllServices();
                selectedData.setCustomer_phone(contact);
            }

        }

    }

    @FXML void tfSearchOnKeyRelease(KeyEvent event) {

    }

    @FXML void tvCustomerOnMouseClicked(MouseEvent event) {
        CustomerData customerData =  tvCustomer.getSelectionModel().getSelectedItem();
        if (customerData!=null){
            setFieldsDisable(false,false);
            selectedData = customerData;
            inputFirstName.getInputField().setText(customerData.getCustomer_first());
            inputMiddle.getInputField().setText(customerData.getCustomer_middle().equals("N/A")?"":customerData.getCustomer_middle());
            inputLast.getInputField().setText(customerData.getCustomer_last());
            inputContact.getInputField().setText(customerData.getCustomer_phone());
        }else{
            setFieldsDisable(false,true);
            resetFields();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbHandler = new DatabaseHandler();
        inputFirstName = new InputHandler(igFirstName);
        inputMiddle = new InputHandler(igMiddleInitial);
        inputLast = new InputHandler(igLastName);
        inputContact = new InputHandler(igContactNum);

        InputHandler.integerOnly(inputContact);
        InputHandler.limitInput(inputContact,11);

        ivSearch.setImage(App.getImage("j-search",false));
        staticNode = rootPane;
        tableLoader = new TableLoader<CustomerData>(this.tvCustomer) {
            @Override
            public void loadCell() {
                colCustomer.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("fullName"));
                colContact.setCellValueFactory(new PropertyValueFactory<CustomerData,String>("customer_phone"));
                colAction.setCellValueFactory(new PropertyValueFactory<CustomerData,HBox>("action"));
            }
        };
        queryAllServices();
        setFieldsDisable(true,true);
        inputFirstName.getInputSubIdentifier().setVisible(true);
        inputFirstName.getInputSubIdentifier().setText("Required");
        inputLast.getInputSubIdentifier().setVisible(true);
        inputLast.getInputSubIdentifier().setText("Required");
        inputContact.getInputSubIdentifier().setVisible(true);
        inputContact.getInputSubIdentifier().setText("Required");
    }

    public final static void queryAllServices(){
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.startConnection();
        ResultSet resultSet =  dbHandler.execQuery("Select * from jays_customer where customer_deleted = 0;");
        try {
            tableLoader.clearList();
            while (resultSet.next()){
                tableLoader.add(new CustomerData(
                        resultSet.getInt("customer_id"),
                        resultSet.getString("customer_first"),
                        resultSet.getString("customer_middle"),
                        resultSet.getString("customer_last"),
                        resultSet.getString("customer_phone")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        dbHandler.closeConnection();
    }

    public void resetFields(){
        inputFirstName.getInputField().setText("");
        inputMiddle.getInputField().setText("");
        inputLast.getInputField().setText("");
        inputContact.getInputField().setText("");
    }

    public void setFieldsDisable(boolean isAdd,boolean disable){
        mngCustomerSave.setText(isAdd?"Add":"Save");
        mngCustomerBtns.setDisable(disable);
       inputFirstName.getInputField().setDisable(disable);
       inputMiddle.getInputField().setDisable(disable);
       inputLast.getInputField().setDisable(disable);
       inputContact.getInputField().setDisable(disable);
    }

    public void focusManageCustomer(boolean focus){
        vbTransaction.setDisable(focus);
        apTableContainer.setDisable(focus);
        tfSearch.setDisable(focus);
        btnSearch.setDisable(focus);
    }

}


   
