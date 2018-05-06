package instabot.controllers;

import de.felixroske.jfxsupport.FXMLController;
import instabot.views.AddaccountView;
import instabot.views.FollowView;
import instabot.views.LikeView;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import org.springframework.beans.factory.annotation.Autowired;

@FXMLController
public class MainViewController {

    @FXML
    Pane myDynamicPane;
    
    @Autowired
    FollowView followView;
    
    @Autowired
    LikeView likeView;

    @Autowired
    AddaccountView addAccountView;

    public void showSomeButtonView(final Event e) {
        myDynamicPane.getChildren().clear();
        myDynamicPane.getChildren().add(followView.getView());
    }
    
    public void showSomeOtherView(final Event e) {
        myDynamicPane.getChildren().clear();
        myDynamicPane.getChildren().add(likeView.getView());
    }

    public void showAddAccountView(final Event e) {
        myDynamicPane.getChildren().clear();
        myDynamicPane.getChildren().add(addAccountView.getView());
    }

//    public void showAddaccountView(Event e) {
//        myDynamicPane.getChildren().clear();
//        myDynamicPane.getChildren().add(addaccountview.getView());
//    }
}
