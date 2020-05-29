package jays.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import jays.controller.component.TableLoader;
import jays.data.DatabaseHandler;
import jays.data.entity.Transaction;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JRecord implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private AnchorPane apTableContainer;

    @FXML
    private TableView<Transaction> tvRecord;

    @FXML
    private TableColumn<Transaction, Integer> colId;

    @FXML
    private TableColumn<Transaction, String> colCustomer;

    @FXML
    private TableColumn<Transaction, Float> colAmount;

    @FXML
    private TableColumn<Transaction, Float> colProfit;

    @FXML
    private TableColumn<Transaction, String> colDate;

    @FXML
    private TableColumn<Transaction, String> colService;

    private TableLoader<Transaction> tableLoader;

    @FXML
    void tvCustomerOnMouseClicked(MouseEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableLoader = new TableLoader<Transaction>(tvRecord) {
            @Override
            public void loadCell() {
                colId.setCellValueFactory(new PropertyValueFactory<Transaction,Integer>("id"));
                colCustomer.setCellValueFactory(new PropertyValueFactory<Transaction,String>("customer"));
                colAmount.setCellValueFactory(new PropertyValueFactory<Transaction,Float>("amount"));
                colProfit.setCellValueFactory(new PropertyValueFactory<Transaction,Float>("income"));
                colDate.setCellValueFactory(new PropertyValueFactory<Transaction,String>("date"));
                colService.setCellValueFactory(new PropertyValueFactory<Transaction,String>("service"));
            }
        };
        DatabaseHandler dbHandler  = new DatabaseHandler();
        dbHandler.startConnection();
        String sql = "SELECT t.transaction_id as 'id',\n" +
                "       concat(c.customer_first,' ',\n" +
                "              c.customer_last) as customer,\n" +
                "       t.transaction_amount as amount,\n" +
                "       transaction_income as income,\n" +
                "       transaction_date as 'date',\n" +
                "       s.service_name as 'service'\n" +
                "from jays_transaction as t\n" +
                "left join jays_customer c on t.customer = c.customer_id\n" +
                "left join jays_service s on t.transaction_service = s.service_id";
        ResultSet resultSet = dbHandler.execQuery(sql);

        try {
            while (resultSet.next()){
                tableLoader.add(new Transaction(resultSet.getInt("id"),
                        resultSet.getString("customer").equals(" ")?"<Unknown>":resultSet.getString("customer"),
                        resultSet.getFloat("amount"),
                        resultSet.getFloat("income"),
                        resultSet.getString("date"),
                        resultSet.getString("service")
                ));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }


}
