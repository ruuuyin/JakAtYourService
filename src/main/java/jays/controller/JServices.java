package jays.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import jays.App;
import jays.controller.component.JChip;
import jays.data.DatabaseHandler;
import jays.utils.InputHandler;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

public class JServices implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private AnchorPane tableContainer;
    @FXML private TableColumn<?, ?> colServiceName;
    @FXML private TableColumn<?, ?> colCategory;
    @FXML private TableColumn<?, ?> colPrice;
    @FXML private TableColumn<?, ?> colProfit;
    @FXML private TableColumn<?, ?> colAction;
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

    @FXML void btnSearchOnAction(ActionEvent event) {

    }

    @FXML void cbCategoriesOnAction(ActionEvent event) {

    }

    @FXML void mngServiceCancelOnAction(ActionEvent event) {
        if(mngServicesSaveBtn.getText().equals("Add")){
            setManageCategoryFocus(false);
            new InputHandler(igServiceName).getInputField().setText("");
            new InputHandler(igServiceProfit).getInputField().setText("");
            new InputHandler(igServicePrice).getInputField().setText("");
            cbCategorySelector.getSelectionModel().select(-1);
            mngServicesSaveBtn.setText("Save");
        }
    }

    @FXML void mngServicesSaveBtnOnAction(ActionEvent event) {

    }

    @FXML void btnAddServiceOnAction(ActionEvent event) {
        setManageCategoryFocus(true);
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
            chipHashMap.forEach((k,v)->{
                if (v.getLabel().toLowerCase().equals(input.getText().toLowerCase())){
                    errorDisplayer.setText("The category is already exist");
                    errorDisplayer.setVisible(true);
                }
            });
            //TODO Create add action here
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
                chipHashMap.forEach((k,v)->{
                    if (v.getLabel().toLowerCase().equals(input.getText().toLowerCase())){
                        errorDisplayer.setText("The category is already exist");
                        errorDisplayer.setVisible(true);
                    }
                });
                //TODO Create edit action here
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

    private void setManageCategoryFocus(boolean isFocused){
        mngCategories.setDisable(isFocused);
        tableContainer.setDisable(isFocused);
        mngServicesSaveBtn.setText("Add");
        btnSearch.setDisable(isFocused);
        tfSearch.setDisable(isFocused);
        cbCategories.setDisable(isFocused);
        setFieldsEditable(isFocused);
    }
}
