package jays.utils;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class InputHandler {
    private TextField inputField;
    private Label inputIdentifier;
    private Label inputSubIdentifier;

    public InputHandler(VBox inputGroup){
        inputGroup.getChildren().forEach(e->{
            if (e instanceof HBox)
                ((HBox) e).getChildren().forEach(lbl->{
                    lbl.getStyleClass().forEach(styleClass->{
                        if (styleClass.equals("inputLabel")) inputIdentifier = (Label) lbl;
                        else if (styleClass.equals("errorLabel")) inputSubIdentifier = (Label) lbl;
                    });
                });
            else if(e instanceof TextField)
                inputField = (TextField) e;
        });
    }

    public TextField getInputField() {
        return inputField;
    }

    public Label getInputIdentifier() {
        return inputIdentifier;
    }

    public Label getInputSubIdentifier() {
        return inputSubIdentifier;
    }

    public final static void phoneRestriction(InputHandler inputHandler){
        TextField tf = inputHandler.inputField;
        InputHandler.integerOnly(inputHandler);
        tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*\\.")) {
                if (tf.getText().charAt(0)=='0') InputHandler.limitInput(inputHandler,11);
                else if(tf.getText().charAt(0)=='9') InputHandler.limitInput(inputHandler,10);
                else  tf.setText(newValue.replaceAll("[^\\d\\.]", ""));
            }
            tf.setText(newValue.replaceAll("[^\\d\\.]", ""));
        });
    }

    public final static void integerOnly(InputHandler inputHandler){
        inputHandler.inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*\\.")) return;
            inputHandler.inputField.setText(newValue.replaceAll("[^\\d\\.]", ""));
        });
    }

    public final static void decimalOnly(InputHandler inputHandler){
        inputHandler.inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) return;
            inputHandler.inputField.setText(newValue.replaceAll("[^\\d]", ""));
        });
    }

    public final static void limitInput(InputHandler inputHandler,int max){
        inputHandler.inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length()>max)
                inputHandler.inputField.setText(newValue.substring(0,max));
            else
                return;
        });
    }

}
