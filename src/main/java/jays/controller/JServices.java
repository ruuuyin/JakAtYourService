package jays.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jays.App;
import jays.controller.component.JChip;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class JServices implements Initializable {

    @FXML private StackPane rootPane;
    @FXML private TableColumn<?, ?> colServiceName;
    @FXML private TableColumn<?, ?> colCategory;
    @FXML private TableColumn<?, ?> colPrice;
    @FXML private TableColumn<?, ?> colProfit;
    @FXML private TableColumn<?, ?> colAction;
    @FXML private JFXButton btnAddService;
    @FXML private VBox igServiceName;
    @FXML private VBox igServiceCategory;
    @FXML private ComboBox<?> cbCategorySelector;
    @FXML private VBox igServicePrice;
    @FXML private VBox igServiceProfit;
    @FXML private JFXButton mngServiceCancel;
    @FXML private JFXButton mngServicesSaveBtn;
    @FXML private JFXButton btnCategoryAdd;
    @FXML private FlowPane fpCategoryContainer;
    @FXML private TextField tfSearch;
    @FXML private JFXButton btnSearch;
    @FXML private ImageView ivSearch;
    @FXML private ComboBox<?> cbCategories;

    private HashMap<String, JChip> chipHashMap = new HashMap<String, JChip>();

    @FXML void btnSearchOnAction(ActionEvent event) {

    }

    @FXML void cbCategoriesOnAction(ActionEvent event) {

    }

    @FXML void mngServiceCancelOnAction(ActionEvent event) {

    }

    @FXML void mngServicesSaveBtnOnAction(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ivSearch.setImage(App.getImage("j-search",false));
        addChip("Sample Chip");
        addChip("Sample Chip");
        addChip("Sample Chip");
        addChip("Sample Chip");
        addChip("Sample Chip");
        addChip("Sample Chip");
        addChip("Sample Chip");
        addChip("Sample Chip");
    }

    private void addChip(String label){
        String id = "chip-"+(chipHashMap.size()+1);
        JChip jChip = new JChip(label,true);
        jChip.setId(id);
        jChip.setCloeOnAction(e->{
            fpCategoryContainer.getChildren().remove(chipHashMap.get(id));
            chipHashMap.remove(id);
        });
        chipHashMap.put(id,jChip);
        fpCategoryContainer.getChildren().add(jChip);
    }
}
