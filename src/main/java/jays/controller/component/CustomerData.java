package jays.controller.component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import jays.controller.JCustomer;
import jays.data.DatabaseHandler;
import jays.data.entity.Customer;

public class CustomerData extends Customer {
    private String fullName;
    private HBox action;
    public CustomerData(int customer_id, String customer_first, String customer_middle, String customer_last, String customer_phone) {
        super(customer_id, customer_first, customer_middle, customer_last, customer_phone);
        this.fullName = customer_first+" "+(customer_middle.equals("N/A")?" ":customer_middle)+" "+customer_last;
        this.action = new HBox(2);
        this.action.setAlignment(Pos.CENTER);
        this.action.setPadding(new Insets(15,0,15,0));
        this.action.getChildren().addAll(generateDeleteButton());
    }

    public String getFullName() {
        return fullName;
    }

    public HBox getAction() {
        return action;
    }

    private JFXButton generateDeleteButton(){
        JFXButton btn = new JFXButton("Delete");
        btn.getStyleClass().addAll("buttonTextLast","buttonDangerOutlined","table-action");
        btn.setOnAction(event -> {
            JFXDialogLayout deleteMessage = JDialogPopup.createByType(JCustomer.staticNode,
                    JCustomer.staticNode.getChildren().get(0),
                    "Warning","Do your really want to delete "+getFullName()+"?",
                    DialogType.CONFIRM,false);

            Timeline queryThread = new Timeline(new KeyFrame(Duration.ZERO, e->{
                DatabaseHandler dbHandler = new DatabaseHandler();
                dbHandler.startConnection();
                dbHandler.execUpdate("Update jays_customer set customer_deleted = 1 where customer_id = "+getCustomer_id());
                dbHandler.closeConnection();
            }),new KeyFrame(Duration.seconds(1)));
            queryThread.setOnFinished(e->{
                JCustomer.queryAllServices();
                JDialogPopup.closeDialog(deleteMessage);
                //TODO add Push notification here if possible
            });
            queryThread.setCycleCount(1);

            JFXButton cancel = new JFXButton("Cancel");
            cancel.setPrefWidth(120);
            cancel.getStyleClass().addAll("buttonNormal","buttonTextSecondary");
            cancel.setOnAction(event1 -> {
                JDialogPopup.closeDialog(deleteMessage);
            });


            JFXButton btnAdd = new JFXButton("Yes");
            btnAdd.setPrefWidth(120);
            btnAdd.getStyleClass().addAll("buttonImportantOutlined","buttonTextSecondary");
            btnAdd.setOnAction(e->{
                queryThread.play();
            });
            deleteMessage.setActions(cancel,btnAdd);
            JDialogPopup.showDialog(deleteMessage);
        });
        return btn;
    }

    public void resetFullName(){
        this.fullName = getCustomer_first()+" "+(getCustomer_middle().equals("N/A")?" ":getCustomer_middle())+" "+getCustomer_last();
    }

}
