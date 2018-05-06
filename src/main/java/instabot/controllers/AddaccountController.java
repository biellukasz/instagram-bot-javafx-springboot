package instabot.controllers;

import de.felixroske.jfxsupport.FXMLController;
import instabot.instaservice.InstagramService;
import instabot.utils.Utils;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;

@FXMLController
public class AddaccountController {


    @Autowired
    InstagramService instagramService;

    @Autowired
    Utils utils;


    @FXML
    TextField logintextfield ,passwordfield;


    @FXML
    TextArea logbox;



    @FXML
    public void reactOnClick(final Event e) throws IOException {

        Task<Boolean> task = new Task<Boolean>() {
            @Override protected Boolean call() throws Exception {
                updateMessage("Checking account");
                return instagramService.checkAccountAndSavetoDB(logintextfield.getText(), passwordfield.getText(),logbox);
            }
        };
        new Thread(task).start();
    }
}