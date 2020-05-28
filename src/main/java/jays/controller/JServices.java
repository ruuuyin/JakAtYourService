package jays.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import jays.App;
import jays.controller.component.*;
import jays.data.DatabaseHandler;
import jays.data.entity.Service;
import jays.utils.InputHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class JServices implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private AnchorPane tableContainer;
    @FXML private TableView<ServiceData> tvService;
    @FXML private TableColumn<ServiceData, String> colServiceName;
    @FXML private TableColumn<ServiceData, String> colCategory;
    @FXML private TableColumn<ServiceData, Float> colPrice;
    @FXML private TableColumn<ServiceData, Float> colProfit;
    @FXML private TableColumn<ServiceData, HBox> colAction;
    @FXML private VBox mngServices;
    @FXML private JFXButton btnAddService;
    @FXML private VBox igServiceName;
    @FXML private VBox igServiceCategory;
    @FXML private ComboBox<String> cbCategorySelector;
    @FXML private VBox igServicePrice;
    @FXML private VBox igServiceProfit;
    @FXML private JFXButton mngServiceCancel;
    @FXML private JFXButton mngServicesSaveBtn;
    @FXML private VBox mngCategories;
    @FXML private JFXButton btnCategoryAdd;
    @FXML private FlowPane fpCategoryContainer;
    @FXML private TextField tfSearch;
    @FXML private JFXButton btnSearch;
    @FXML private ImageView ivSearch;
    @FXML private ComboBox<String> cbCategories;

    private HashMap<String, JChip> chipHashMap = new HashMap<String, JChip>();
    private JFXDialogLayout dialogLayout;
    private JFXDialog dialog;
    private DatabaseHandler dbHandler;
    private TableLoader<ServiceData> tableLoader;

    @FXML void btnSearchOnAction(ActionEvent event) {

    }

    @FXML void cbCategoriesOnAction(ActionEvent event) {

    }

    @FXML void mngServiceCancelOnAction(ActionEvent event) {
        if(isAdd()){
            setManageServices(false);
            new InputHandler(igServiceName).getInputField().setText("");
            new InputHandler(igServiceProfit).getInputField().setText("");
            new InputHandler(igServicePrice).getInputField().setText("");
            cbCategorySelector.getSelectionModel().select(-1);
            mngServicesSaveBtn.setText("Save");
        }
    }

    @FXML void mngServicesSaveBtnOnAction(ActionEvent event) {
        if (isAdd()){
            InputHandler service_name = new InputHandler(igServiceName);
            InputHandler service_price = new InputHandler(igServicePrice);
            InputHandler service_profit = new InputHandler(igServiceProfit);
            if (service_name.getInputField().getText().equals("")||
                    service_price.getInputField().getText().equals("")||
                    service_profit.getInputField().getText().equals("")||
                    cbCategorySelector.getSelectionModel().getSelectedItem()==null){
                JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,
                        rootPane.getChildren().get(0),
                        "Operation Failed",
                        "Please fill all the required fields",
                        DialogType.ERROR));
            }else{
                AtomicReference<String> id = new AtomicReference<>();
                chipHashMap.forEach((k,v)->{
                    if (v.getLabel().toLowerCase().equals(cbCategorySelector.getSelectionModel().getSelectedItem().toLowerCase()))
                        id.set(k.split("-")[1]);
                });
                dbHandler.startConnection();
                String sql = String.format("insert into jays_service(service_name,service_price,service_profit,service_category) " +
                                "values('%s',%s,%s,%s)",
                        service_name.getInputField().getText(),
                        service_price.getInputField().getText(),
                        service_profit.getInputField().getText()
                        ,id.get());
                dbHandler.execUpdate(sql);
                dbHandler.closeConnection();
                setManageServices(false);
                new InputHandler(igServiceName).getInputField().setText("");
                new InputHandler(igServiceProfit).getInputField().setText("");
                new InputHandler(igServicePrice).getInputField().setText("");
                mngServicesSaveBtn.setText("Save");
                JDialogPopup.showDialog(JDialogPopup.createByType(rootPane,
                        rootPane.getChildren().get(0),
                        "Operation Success",
                        "New service has been added",
                        DialogType.SUCCESS));
                this.queryAllServices();
            }
        }
    }

    @FXML void btnAddServiceOnAction(ActionEvent event) {
        setManageServices(true);
        resetTextField();
    }

    @FXML void btnCategoryAddOnAction(ActionEvent event) {
        dialogLayout= new JFXDialogLayout();
        dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.TOP);
        dialog.setOverlayClose(false);
        Label header = new Label("Add Category");
        header.getStyleClass().add("headerLabel");

        TextField input = new TextField();
        input.setPromptText("Enter new Category");
        input.setPrefSize(441.0,34.0);
        input.setFont(new Font("Segoe UI",16));

        Label errorDisplayer = new Label();
        errorDisplayer.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        errorDisplayer.setPrefSize(Region.USE_COMPUTED_SIZE,16.0);
        errorDisplayer.getStyleClass().add("errorLabel");
        errorDisplayer.setVisible(false);
        VBox.setVgrow(errorDisplayer,Priority.ALWAYS);

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

        JFXButton btnAdd = new JFXButton("Add");
        btnAdd.setPrefWidth(120);
        btnAdd.getStyleClass().addAll("buttonImportantOutlined","buttonTextSecondary");
        btnAdd.setOnAction(event1 -> {
            AtomicBoolean exist = new AtomicBoolean(false);
            chipHashMap.forEach((k,v)->{
                if (v.getLabel().toLowerCase().equals(input.getText().toLowerCase())){
                    errorDisplayer.setText("The category is already exist");
                    errorDisplayer.setVisible(true);
                    exist.set(true);
                }
            });
            if (!exist.get()){
                dbHandler.startConnection();
                dbHandler.execUpdate(String.format("Insert into jays_category(category_name) values('%s');",input.getText()));
                dbHandler.closeConnection();
                loadServices();
                dialog.close();
                //TODO Add push Notification here if possible
            }
        });

        dialogLayout.setHeading(header);
        dialogLayout.setBody(content);
        dialogLayout.setActions(cancel,btnAdd);

        dialog.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ivSearch.setImage(App.getImage("j-search",false));
        dbHandler = new DatabaseHandler();
        loadServices();
        setFieldsEditable(false);
        InputHandler.decimalOnly(new InputHandler(igServicePrice));
        InputHandler.decimalOnly(new InputHandler(igServiceProfit));
        resetTextField();
        tableLoader = new TableLoader<ServiceData>(this.tvService) {
            @Override
            public void loadCell() {
                colServiceName.setCellValueFactory(new PropertyValueFactory<ServiceData,String>("service_name"));
                colCategory.setCellValueFactory(new PropertyValueFactory<ServiceData,String>("service_category"));
                colPrice.setCellValueFactory(new PropertyValueFactory<ServiceData,Float>("service_price"));
                colProfit.setCellValueFactory(new PropertyValueFactory<ServiceData,Float>("service_profit"));
                colAction.setCellValueFactory(new PropertyValueFactory<ServiceData,HBox>("action"));
            }
        };
        queryAllServices();
    }

    private void addChip(int chipId,String label){
        String id = "chip-"+(chipId);
        JChip jChip = new JChip(label,true);
        jChip.setId(id);
        jChip.setCloseOnAction(e->{
            fpCategoryContainer.getChildren().remove(chipHashMap.get(id));
            chipHashMap.remove(id);
        });
        jChip.setOnAction(e->{
            dialogLayout= new JFXDialogLayout();
            dialog = new JFXDialog(rootPane,dialogLayout,JFXDialog.DialogTransition.TOP);
            dialog.setOverlayClose(false);
            Label header = new Label("Edit Category");
            header.getStyleClass().add("headerLabel");

            TextField input = new TextField(jChip.getLabel());
            input.setPromptText("Enter Category");
            input.setPrefSize(441.0,34.0);
            input.setFont(new Font("Segoe UI",16));


            Label errorDisplayer = new Label();
            errorDisplayer.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
            errorDisplayer.setPrefSize(Region.USE_COMPUTED_SIZE,16.0);
            errorDisplayer.getStyleClass().add("errorLabel");
            errorDisplayer.setVisible(false);
            VBox.setVgrow(errorDisplayer,Priority.ALWAYS);

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
                if (jChip.getLabel().toLowerCase().equals(input.getText().toLowerCase())){
                    dialog.close();
                }else{
                    AtomicBoolean exist = new AtomicBoolean(false);
                    chipHashMap.forEach((k,v)->{
                        if (v.getLabel().toLowerCase().equals(input.getText().toLowerCase())){
                            errorDisplayer.setText("The category is already exist");
                            errorDisplayer.setVisible(true);
                            exist.set(true);
                        }
                    });
                    if (!exist.get()){
                        dbHandler.startConnection();
                        dbHandler.execUpdate(String.format("Update jays_category set category_name = '%s' where category_id = "+chipId+";",input.getText()));
                        dbHandler.closeConnection();
                        loadServices();
                        dialog.close();
                        //TODO Add push Notification here if possible
                    }
                }
            });

            dialogLayout.setHeading(header);
            dialogLayout.setBody(content);
            dialogLayout.setActions(cancel,btnAdd);

            dialog.show();
        });
        chipHashMap.put(id,jChip);
        fpCategoryContainer.getChildren().add(jChip);
    }

    private void loadServices(){
        String sql = "Select * from jays_category";
        cbCategorySelector.getItems().clear();
        cbCategories.getItems().clear();
        chipHashMap.clear();
        fpCategoryContainer.getChildren().clear();
        try {
            dbHandler.startConnection();
            ResultSet resultSet = dbHandler.execQuery(sql);
            while (resultSet.next()){
                int category_id = resultSet.getInt("category_id");
                String category_name = resultSet.getString("category_name");
                cbCategories.getItems().add(category_name);
                cbCategorySelector.getItems().add(category_name);
                addChip(category_id,category_name);
            }
            dbHandler.closeConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            dbHandler.closeConnection();
        }
    }

    private final void setFieldsEditable(boolean disabler){
        cbCategorySelector.setDisable(!disabler);
        new InputHandler(igServiceName).getInputField().setEditable(disabler);
        new InputHandler(igServicePrice).getInputField().setEditable(disabler);
        new InputHandler(igServiceProfit).getInputField().setEditable(disabler);
    }

    private void setManageServices(boolean isFocused){
        mngCategories.setDisable(isFocused);
        tableContainer.setDisable(isFocused);
        mngServicesSaveBtn.setText("Add");
        btnSearch.setDisable(isFocused);
        tfSearch.setDisable(isFocused);
        cbCategories.setDisable(isFocused);
        setFieldsEditable(isFocused);
    }

    private final boolean isAdd(){
        return mngServicesSaveBtn.getText().equals("Add");
    }

    private final void resetTextField(){
        InputHandler service_name = new InputHandler(igServiceName);
        InputHandler service_price = new InputHandler(igServiceProfit);
        InputHandler service_profit = new InputHandler(igServicePrice);
        InputHandler service_category = new InputHandler(igServiceCategory);
        cbCategorySelector.getSelectionModel().select(-1);
        service_name.getInputField().setText("");
        service_price.getInputField().setText("");
        service_profit.getInputField().setText("");
        service_name.getInputSubIdentifier().setVisible(true);
        service_name.getInputSubIdentifier().setText("Required");
        service_price.getInputSubIdentifier().setVisible(true);
        service_price.getInputSubIdentifier().setText("Required");
        service_profit.getInputSubIdentifier().setVisible(true);
        service_profit.getInputSubIdentifier().setText("Required");
        service_category.getInputSubIdentifier().setVisible(true);
        service_category.getInputSubIdentifier().setText("Required");
    }

    private final void queryAllServices(){
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
                "        on s.service_category = c.category_id\n");
        try {
            tableLoader.clearList();
            while (resultSet.next()){
                tableLoader.add(new ServiceData(
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
}
