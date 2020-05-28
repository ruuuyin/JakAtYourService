package jays.controller.component;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import jays.data.entity.Service;

public class ServiceData extends Service {


    private HBox action;
    public ServiceData(int service_id, String service_name, boolean service_status, float service_price, float service_profit, String service_category) {
        super(service_id, service_name, service_status, service_price, service_profit, service_category);
        this.action = new HBox(2);
        this.action.setAlignment(Pos.CENTER);
        this.action.getChildren().addAll(generateActivateButton(),generateDeleteButton());
    }

    private JFXButton generateActivateButton(){
        JFXButton btn = new JFXButton(isService_status()?"Deactivate":"Activate");
        btn.getStyleClass().addAll("buttonTextLast",(isService_status()?"buttonNormal":"buttonImportantOutlined"),"table-action");
        btn.setOnAction(event -> {
            if (isService_status()){
                setService_status(false);
            }else{
                setService_status(true);
            }
            btn.getStyleClass().clear();
            btn.getStyleClass().addAll("buttonTextLast",(isService_status()?"buttonNormal":"buttonImportantOutlined"),"table-action");
            //TODO Activate action here
        });

        return btn;
    }
    private JFXButton generateDeleteButton(){
        JFXButton btn = new JFXButton("Delete");
        btn.getStyleClass().addAll("buttonTextLast","buttonDangerOutlined","table-action");
        btn.setOnAction(event -> {
            //TODO Delete  action here
        });
        return btn;
    }

    public HBox getAction() {
        return action;
    }
}
