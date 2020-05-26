package jays.controller.component;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import jays.App;
import notification.Location;
import notification.NotificationFactory;
import notification.NotificationType;
import notification.NotificationWorker;

public class JNotification {
    private static NotificationWorker nw ;
    private static NotificationFactory nf;
    private static final double GAP = 10;

    public static final NotificationWorker createBottomLeft(AnchorPane rootAnchor){
        nw = new NotificationWorker(Location.BOTTOM_LEFT,rootAnchor,GAP);
        return nw;
    }

    public static final NotificationWorker createBottomRight(AnchorPane rootAnchor){
        nw = new NotificationWorker(Location.BOTTOM_RIGHT,rootAnchor,GAP);
        return nw;
    }

    public static final NotificationWorker createTopLeft(AnchorPane rootAnchor){
        nw = new NotificationWorker(Location.TOP_LEFT,rootAnchor,GAP);
        return nw;
    }

    public static final NotificationWorker createTopRight(AnchorPane rootAnchor){
        nw = new NotificationWorker(Location.TOP_RIGHT,rootAnchor,GAP);
        return nw;
    }

    public static final NotificationFactory notif(String header,String content, NotificationType type){
        nf = new NotificationFactory(header,content,type);
        initializeStyle();
        nf.create();
        return nf;
    }


    private final static void initializeStyle(){
        nf.setEffect(new DropShadow(20,new Color(0f,0f,0f,1)));
        if (darkMode()){
            nf.setStyle("-fx-background-color:#343b4a;-fx-background-radius: 5px");
            nf.setHeaderStyle("-fx-text-fill:#ececec;-fx-font-weight:bold;");
            nf.setContentStyle("-fx-text-fill:#d5d5d5;");
        }else{
            nf.setStyle("-fx-background-color:#ffffff;-fx-background-radius: 5px");
            nf.setHeaderStyle("-fx-text-fill:#264d60;-fx-font-weight:bold;");
            nf.setContentStyle("-fx-text-fill:#79808e;");
        }

    }

    private final static boolean darkMode(){
        return App.systemInfo().getTheme().equals("DarkMode");
    }
}
