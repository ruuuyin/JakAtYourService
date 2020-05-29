package jays.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
import javafx.util.Duration;
import jays.App;
import jays.controller.component.*;
import jays.data.DatabaseHandler;
import jays.data.entity.Customer;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class JTransaction implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private AnchorPane apTableContainer;
    @FXML private TableView<AvailableServicesData> tvAvailableServices;
    @FXML private TableColumn<AvailableServicesData, String> colAvailableServices;
    @FXML private TableColumn<AvailableServicesData, Float> colPriceValue;
    @FXML private TableColumn<AvailableServicesData, HBox> colAdd;
    @FXML private TextField tfSearch;
    @FXML private JFXButton btnSearch;
    @FXML private ImageView ivSearch;
    @FXML private VBox mngServices;
    @FXML private JFXButton btnClear;
    @FXML private VBox igServiceCategory;
    @FXML private ComboBox<String> cbCustomer;
    @FXML private TableView<AvailableServicesData> tvSelectedServices;
    @FXML private TableColumn<AvailableServicesData, String> colSelectedServices;
    @FXML private TableColumn<AvailableServicesData, HBox> colRemove;
    @FXML private Label lblServices;
    @FXML private Label lblAmount;
    @FXML private Label lblProfit;
    @FXML private JFXButton btnAdd;

    private JFXDialogLayout dialogLayout;
    private JFXDialog dialog;
    private DatabaseHandler dbHandler;
    public static TableLoader<AvailableServicesData> tblAvailable;
    public static TableLoader<AvailableServicesData> tblSelected;
    public static StackPane staticNode;
    public static Label lServices,lAmount,lProfit;
    private static ArrayList<AvailableServicesData> serviceList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ivSearch.setImage(App.getImage("j-search",false));
        dbHandler = new DatabaseHandler();
        staticNode = rootPane;

        tblAvailable = new TableLoader<AvailableServicesData>(tvAvailableServices) {
            @Override
            public void loadCell() {
                colAvailableServices.setCellValueFactory(new PropertyValueFactory<AvailableServicesData,String>("service_name"));
                colPriceValue.setCellValueFactory(new PropertyValueFactory<AvailableServicesData,Float>("service_price"));
                colAdd.setCellValueFactory(new PropertyValueFactory<AvailableServicesData,HBox>("move"));
            }
        };

        tblSelected = new TableLoader<AvailableServicesData>(tvSelectedServices) {
            @Override
            public void loadCell() {
                colSelectedServices.setCellValueFactory(new PropertyValueFactory<AvailableServicesData,String>("service_name"));
                colRemove.setCellValueFactory(new PropertyValueFactory<AvailableServicesData,HBox>("delete"));
            }
        };
        queryAvailableServices();
        loadComboBox();

        lServices = lblServices;
        lAmount = lblAmount;
        lProfit = lblProfit;
        cbCustomer.getSelectionModel().select(0);
    }

    @FXML void btnAddOnAction(ActionEvent event) {

        if (tblSelected.getList().size()>0){
            JFXDialogLayout dialogLayout= JDialogPopup.createByType(rootPane,
                    rootPane.getChildren().get(0),
                    "Processing. . .",
                    "Saving Record, please wait. . .", DialogType.INFORM,false);
            JDialogPopup.showDialog(dialogLayout);
            Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),event1 -> {
                Date dt = new Date(System.currentTimeMillis());
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                String customer = cbCustomer.getSelectionModel().getSelectedIndex()==0?"null":cbCustomer.getSelectionModel().getSelectedItem().split(" - ")[0];
                tblSelected.getList().forEach(data->{
                    dbHandler.startConnection();
                    String sql =String.format("insert into jays_transaction(customer, transaction_amount, transaction_income, transaction_date, transaction_service)" +
                            " VALUES (%s,%s,%s,'%s',%s);",customer,lblAmount.getText(),lblProfit.getText(), formatter.format(dt),data.getService_id());
                    dbHandler.execUpdate(sql);
                    dbHandler.closeConnection();
                });
            }),new KeyFrame(Duration.seconds(1)));

            timeline.setOnFinished(event1 -> {
                resetFields();
                JDialogPopup.closeDialog(dialogLayout);
                JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,
                        rootPane.getChildren().get(0),
                        "Success",
                        "New Transaction Added",DialogType.SUCCESS));
            });
            timeline.setCycleCount(1);
            timeline.play();

        }else{
            JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,
                    rootPane.getChildren().get(0),
                    "Invalid Process",
                    "Services cannot be empty", DialogType.ERROR,true));
        }
    }

    @FXML void btnClearOnAction(ActionEvent event) {
        tblSelected.getList().clear();
        lServices.setText("0");
        lAmount.setText("0.0");
        lProfit.setText("0.0");
    }

    @FXML void btnSearchOnAction(ActionEvent event) {
        if (tfSearch.getText().equals("")){
            queryAvailableServices();
        }else{
            tblAvailable.getList().clear();
            serviceList.forEach(e->{
                if (e.getService_name().toLowerCase().contains(tfSearch.getText().toLowerCase())){
                    tblAvailable.getList().add(e);
                }
            });
        }
    }

    @FXML void tfSearchOnKeyRelease(KeyEvent event) {

    }

    @FXML void tvCustomerOnMouseClicked(MouseEvent event) {

    }

    public static void queryAvailableServices(){
        DatabaseHandler dbHandler = new DatabaseHandler();
        dbHandler.startConnection();
        ResultSet resultSet = dbHandler.execQuery(
                "select s.service_id as id,\n" +
                        "       s.service_name as name,\n" +
                        "       s.service_status as status,\n" +
                        "       s.service_price as price,\n" +
                        "       s.service_profit as profit,\n" +
                        "       c.category_name as category\n" +
                        "from jays_service as s\n" +
                        "    join jays_category as c\n" +
                        "        on s.service_category = c.category_id\n " +
                        "where s.service_deleted = 0 and s.service_status = 1;");
        try {
            tblAvailable.clearList();
            while (resultSet.next()){
                tblAvailable.add(new AvailableServicesData(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getBoolean("status"),
                        resultSet.getFloat("price"),
                        resultSet.getFloat("profit"),
                        resultSet.getString("category")
                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        dbHandler.closeConnection();
        serviceList.clear();
        serviceList.addAll(tblAvailable.getList());
    }

    public static ArrayList<Customer> customerList = new ArrayList<>();
    public void loadComboBox(){
        String sql = "Select * from jays_customer where customer_deleted = 0";
        cbCustomer.getItems().clear();
        customerList.clear();
        cbCustomer.getItems().add("None");
        try {
            dbHandler.startConnection();
            ResultSet resultSet = dbHandler.execQuery(sql);
            while (resultSet.next()){
                Customer newCustomer =  new Customer(resultSet.getInt("customer_id"),
                        resultSet.getString("customer_first"),
                        resultSet.getString("customer_middle").equals("N/A")
                                ? ""
                                : resultSet.getString("customer_middle"),
                        resultSet.getString("customer_last"),
                        resultSet.getString("customer_phone")
                );
                customerList.add(newCustomer);
                cbCustomer.getItems().add(newCustomer.toString());
            }
            dbHandler.closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            dbHandler.closeConnection();
        }


    }

    private void resetFields(){
        cbCustomer.getSelectionModel().select(0);
        tblSelected.getList().clear();
    }
}
