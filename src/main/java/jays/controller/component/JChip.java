package jays.controller.component;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class JChip extends HBox {
    private Label label;
    private Button button;
    private String name;
    private boolean closable;

    public JChip(String label,boolean closable){
        super();
        this.name = label;
        this.closable = closable;
        this.setAlignment(Pos.CENTER_LEFT);
        this.getStyleClass().add("chip");
        this.getChildren().add(createLabel());
        if (closable){
            this.getChildren().add(createCloseButton());
            HBox.setMargin(this.label,new Insets(2.0,2.0,2.0,5.0));
        }
    }

    private Label createLabel(){
        label = new Label();
        label.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        label.getStyleClass().add("chipLabel");
        label.setText(name);
        HBox.setHgrow(label, Priority.ALWAYS);
        HBox.setMargin(label,new Insets(2.0,5.0,2.0,5.0));
        return label;
    }

    private Button createCloseButton(){
        button = new Button("x");
        button.setMinSize(23,23);
        button.setPrefSize(23,23);
        button.getStyleClass().add("chipClose");
        HBox.setMargin(button,new Insets(2.0));
        return button;
    }

    public void setCloeOnAction(EventHandler<ActionEvent> event){
        button.setOnAction(event);
    }

    public void setOnAction(EventHandler<? super MouseEvent> event){
       label.setOnMouseClicked(event);
    }

    public void setLabel(String label){
        this.name = label;
        this.label.setText(label);
    }

    public String getLabel(){
        return this.name;
    }

    public boolean isClosable() {
        return closable;
    }

}
