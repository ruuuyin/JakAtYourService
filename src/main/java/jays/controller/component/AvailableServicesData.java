package jays.controller.component;

import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import jays.controller.JTransaction;
import jays.data.entity.Service;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AvailableServicesData extends Service {
    private HBox move,delete;

    public AvailableServicesData(int service_id, String service_name, boolean service_status, float service_price, float service_profit, String service_category) {
        super(service_id, service_name, service_status, service_price, service_profit, service_category);
        this.move = new HBox(2);
        this.move.setAlignment(Pos.CENTER);
        this.move.setPadding(new Insets(15,0,15,0));
        this.move.getChildren().addAll(generateMove());
        this.delete = new HBox(2);
        this.delete.setAlignment(Pos.CENTER);
        this.delete.setPadding(new Insets(15,0,15,0));
        this.delete.getChildren().addAll(generateDelete());

    }

    AvailableServicesData selected ;
    private JFXButton generateMove() {
        JFXButton btn = new JFXButton(">>");
        btn.getStyleClass().addAll("buttonTextLast","table-action","buttonImportantOutlined");
        btn.setOnAction(event -> {
            selected = new AvailableServicesData(getService_id(),
                    getService_name(),isService_status(),getService_price(),getService_profit(),getService_category());
            JTransaction.tblSelected.add(selected);
            computeAll();
            //TODO Moving Available table to selected table
        });

        return btn;
    }

    private JFXButton generateDelete() {
        JFXButton delete = new JFXButton("x");
        delete.getStyleClass().addAll("buttonTextLast","buttonDangerOutlined","table-action");
        delete.setOnAction(event -> {
            JTransaction.tblSelected.remove(this);
            computeAll();
            //TODO removing data from selected table
        });
        return delete;
    }

    public HBox getMove() {
        return move;
    }

    public HBox getDelete() {
        return delete;
    }

    private void computeAll(){
        if (JTransaction.tblSelected.getList().size()>0){
            AtomicInteger services= new AtomicInteger();
            AtomicReference<Float> overallprice= new AtomicReference<>(0f);
            AtomicReference<Float> profit= new AtomicReference<>(0f);
            JTransaction.tblSelected.getList().forEach(data->{
                services.getAndIncrement();
                overallprice.set(overallprice.get() + data.getService_price());
                profit.set(profit.get() + data.getService_price());
            });
            JTransaction.lServices.setText(services.get()+"");
            JTransaction.lAmount.setText(overallprice.get()+"");
            JTransaction.lProfit.setText(profit.get()+"");

        }else{
            JTransaction.lServices.setText("0");
            JTransaction.lAmount.setText("0.0");
            JTransaction.lProfit.setText("0.0");
        }
    }
}
