package jays.controller.component;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import jays.controller.JServices;
import jays.data.DatabaseHandler;
import jays.data.entity.Service;


public class ServiceData extends Service {


    private HBox action;
    public ServiceData(int service_id, String service_name, boolean service_status, float service_price, float service_profit, String service_category) {
        super(service_id, service_name, service_status, service_price, service_profit, service_category);
        this.action = new HBox(2);
        this.action.setAlignment(Pos.CENTER);
        this.action.setPadding(new Insets(15,0,15,0));
        this.action.getChildren().addAll(generateActivateButton(),generateDeleteButton());
    }

    private JFXButton generateActivateButton(){
        JFXButton btn = new JFXButton(isService_status()?"Deactivate":"Activate");
        btn.getStyleClass().addAll("buttonTextLast","table-action",(isService_status()?"buttonNormal":"buttonImportantOutlined"));
        btn.setOnAction(event -> {
            Timeline queryThread = new Timeline(new KeyFrame(Duration.ZERO,e->{
                DatabaseHandler dbHandler = new DatabaseHandler();
                dbHandler.startConnection();
                if (isService_status())
                    dbHandler.execUpdate("Update jays_service set service_status = 0 where service_id = "+getService_id());
                else
                    dbHandler.execUpdate("Update jays_service set service_status = 1 where service_id = "+getService_id());
                dbHandler.closeConnection();
            }),new KeyFrame(Duration.seconds(1)));

            queryThread.setOnFinished(e->{
                JServices.queryAllServices();
            });
            queryThread.setCycleCount(1);
            queryThread.play();
        });

        return btn;
    }
    private JFXButton generateDeleteButton(){
        JFXButton btn = new JFXButton("Delete");
        btn.getStyleClass().addAll("buttonTextLast","buttonDangerOutlined","table-action");
        btn.setOnAction(event -> {
            JFXDialogLayout deleteMessage = JDialogPopup.createByType(JServices.staticNode,
                    JServices.staticNode.getChildren().get(0),
                    "Warning","Do your really want to delete "+getService_name()+"?",
                    DialogType.CONFIRM,false);

            Timeline queryThread = new Timeline(new KeyFrame(Duration.ZERO,e->{
                DatabaseHandler dbHandler = new DatabaseHandler();
                dbHandler.startConnection();
                dbHandler.execUpdate("Update jays_service set service_deleted = 1 where service_id = "+getService_id());
                dbHandler.closeConnection();
            }),new KeyFrame(Duration.seconds(1)));
            queryThread.setOnFinished(e->{
                JServices.queryAllServices();
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

    public HBox getAction() {
        return action;
    }
}
