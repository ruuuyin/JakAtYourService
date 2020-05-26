package jays.controller.component;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import jays.utils.Directory;

public class JDialogPopup {
    private static BoxBlur blur = new BoxBlur(4,4,4);
    private  static JFXDialogLayout dialogLayout ;
    private static JFXDialog dialog;

    public static final JFXDialogLayout createStandard(StackPane root, Node toBeBlur, String message,boolean overlayClose){
        dialogLayout= new JFXDialogLayout();
        dialog = new JFXDialog(root,dialogLayout,JFXDialog.DialogTransition.TOP);
        dialog.setOverlayClose(overlayClose);

        Label msg = new Label(message);
        msg.getStyleClass().add("inputLabel");

        dialogLayout.setBody(msg);
        dialog.setOnDialogClosed(event -> toBeBlur.setEffect(null));
         dialog.setOnDialogOpened(event -> toBeBlur.setEffect(blur));

        return dialogLayout;
    }

    public static final JFXDialogLayout createStandard(StackPane root, Node toBeBlur, String title,String message,boolean overlayClose){
        dialogLayout= new JFXDialogLayout();
        dialog = new JFXDialog(root,dialogLayout,JFXDialog.DialogTransition.TOP);
        dialog.setOverlayClose(overlayClose);

        Label msg = new Label(message);
        msg.getStyleClass().add("inputLabel");

        Label ttl = new Label(title);
        msg.getStyleClass().add("headerLabel");

        dialogLayout.setHeading(ttl);
        dialogLayout.setBody(msg);
        dialog.setOnDialogClosed(event -> toBeBlur.setEffect(null));
         dialog.setOnDialogOpened(event -> toBeBlur.setEffect(blur));

        return dialogLayout;
    }

    public static final JFXDialogLayout createStandard(StackPane root, Node toBeBlur, String message){
        dialogLayout= new JFXDialogLayout();
        dialog = new JFXDialog(root,dialogLayout,JFXDialog.DialogTransition.TOP);
        dialog.setOverlayClose(true);

        Label msg = new Label(message);
        msg.getStyleClass().add("inputLabel");

        dialogLayout.setBody(msg);
        dialog.setOnDialogClosed(event -> toBeBlur.setEffect(null));
         dialog.setOnDialogOpened(event -> toBeBlur.setEffect(blur));

        return dialogLayout;
    }

    public static final JFXDialogLayout createStandard(StackPane root, Node toBeBlur, String title,String message){
        dialogLayout= new JFXDialogLayout();
        dialog = new JFXDialog(root,dialogLayout,JFXDialog.DialogTransition.TOP);
        dialog.setOverlayClose(true);

        Label msg = new Label(message);
        msg.getStyleClass().add("inputLabel");

        Label ttl = new Label(title);
        msg.getStyleClass().add("headerLabel");

        dialogLayout.setHeading(ttl);
        dialogLayout.setBody(msg);
        dialog.setOnDialogClosed(event -> toBeBlur.setEffect(null));
        dialog.setOnDialogOpened(event -> toBeBlur.setEffect(blur));

        return dialogLayout;
    }


    public static final JFXDialogLayout createByType(StackPane root, Node toBeBlur, String title,String message,DialogType dialogType){
        dialogLayout= new JFXDialogLayout();
        dialog = new JFXDialog(root,dialogLayout,JFXDialog.DialogTransition.TOP);
        dialog.setOverlayClose(true);

        Label msg = new Label(message);
        msg.getStyleClass().add("inputLabel");

        Label ttl = new Label(title);
        ttl.getStyleClass().add("headerLabel");

        ImageView iv = new ImageView(getImageByType(dialogType)==null?null:new Image(getImageByType(dialogType)));
        iv.setFitHeight(30);
        iv.setFitWidth(30);
        HBox.setMargin(iv,new Insets(0,5,0,0));
        HBox headerContainer = new HBox(iv,ttl);
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        dialogLayout.setHeading(headerContainer);
        dialogLayout.setBody(msg);
        dialog.setOnDialogClosed(event -> toBeBlur.setEffect(null));

        
        dialog.setOnDialogOpened(event -> toBeBlur.setEffect(blur));
     
        return dialogLayout;
    }

    public static final JFXDialogLayout createByType(StackPane root, Node toBeBlur, String title,String message,DialogType dialogType,boolean overlayClose){
        dialogLayout= new JFXDialogLayout();
        dialog = new JFXDialog(root,dialogLayout,JFXDialog.DialogTransition.TOP);
        dialog.setOverlayClose(overlayClose);

        Label msg = new Label(message);
        msg.getStyleClass().add("inputLabel");

        Label ttl = new Label(title);
        ttl.getStyleClass().add("headerLabel");

        ImageView iv = new ImageView(new Image(getImageByType(dialogType)));
        iv.setFitHeight(30);
        iv.setFitWidth(30);
        HBox.setMargin(iv,new Insets(0,5,0,0));
        HBox headerContainer = new HBox(iv,ttl);
        headerContainer.setAlignment(Pos.CENTER_LEFT);
        dialogLayout.setHeading(headerContainer);
        dialogLayout.setBody(msg);
        dialog.setOnDialogClosed(event -> toBeBlur.setEffect(null));


        dialog.setOnDialogOpened(event -> toBeBlur.setEffect(blur));

        return dialogLayout;
    }

    private static String getImageByType(DialogType type){
        switch (type){
            case ERROR:
                return JDialogPopup.class.getResource(Directory.ICONS+"error.png").toString();
            case INFORM:
                return JDialogPopup.class.getResource(Directory.ICONS+"inform.png").toString();
            case CONFIRM:
                return JDialogPopup.class.getResource(Directory.ICONS+"confirm.png").toString();
            case SUCCESS:
                return JDialogPopup.class.getResource(Directory.ICONS+"success.png").toString();
            default: return null;
        }
    }

    public static void showDialog(JFXDialogLayout dialogLayout){
        JFXDialog dialog = (JFXDialog) dialogLayout.getParent().getParent();
        dialog.show();
    }

    public static void closeDialog(JFXDialogLayout dialogLayout){
        JFXDialog dialog = (JFXDialog) dialogLayout.getParent().getParent();
        dialog.close();
    }
}
