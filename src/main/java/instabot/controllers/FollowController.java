package instabot.controllers;

import de.felixroske.jfxsupport.FXMLController;
import instabot.entitys.InstagramAccount;
import instabot.instaservice.InstagramService;
import instabot.repository.InstagramAccountRepo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.List;

@FXMLController
public class FollowController {

    @Autowired
    InstagramService instagramService;

    @Autowired
    InstagramAccountRepo instagramAccountRepo;


    @FXML
    TextField username;

    @FXML
    TextField hashtag;


    @FXML
    Label label;

    @FXML
    TextArea logbox;

    @FXML
    ComboBox combobox;

    private ObservableList<String> usernameslist = FXCollections.observableArrayList();


    @FXML
    public void reactOnClick(final Event e) throws IOException {
//        int number = Integer.parseInt(follows.getText());
        Task<Boolean> task = new Task<Boolean>() {
            @Override
            protected Boolean call() throws Exception {
                instagramService.followFromGivenHashtag(hashtag.getText(),combobox.getValue().toString(),logbox,10);
                return null;
            }
        };
        new Thread(task).start();
    }

    @FXML
    public void initialize(){
        List<InstagramAccount> all = instagramAccountRepo.findAll();
        for(InstagramAccount account : all){
            usernameslist.add(account.getLogin());
        }
        combobox.setItems(usernameslist);
    }

}
