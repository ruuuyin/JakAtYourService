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
import jays.controller.component.AvailableServicesData;
import jays.controller.component.TableLoader;
import jays.data.DatabaseHandler;
import jays.data.entity.Customer;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

        /* tl = new Timeline(new KeyFrame(Duration.ZERO,event -> {

            if (tblSelected.getList().size()>0){
                AtomicInteger services= new AtomicInteger();
                AtomicReference<Float> overallprice= new AtomicReference<>(0f);
                AtomicReference<Float> profit= new AtomicReference<>(0f);
                tblSelected.getList().forEach(data->{
                    services.getAndIncrement();
                    overallprice.set(overallprice.get() + data.getService_price());
                    profit.set(profit.get() + data.getService_price());
                });
                lblServices.setText(services.get()+"");
                lblAmount.setText(overallprice.get()+"");
                lblProfit.setText(profit.get()+"");

            }else{
                lblServices.setText("0");
                lblAmount.setText("0.0");
                lblProfit.setText("0.0");
            }
        }));*/

        lServices = lblServices;
        lAmount = lblAmount;
        lProfit = lblProfit;
    }

    @FXML void btnAddOnAction(ActionEvent event) {

    }

    @FXML void btnClearOnAction(ActionEvent event) {

    }

    @FXML void btnSearchOnAction(ActionEvent event) {

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
}
