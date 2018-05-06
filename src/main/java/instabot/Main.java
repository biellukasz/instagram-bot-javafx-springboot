package instabot;

import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import instabot.views.MainView;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main extends AbstractJavaFxApplicationSupport{
    public static void main(String[] args) {
        launchApp(Main.class, MainView.class, args);
    }
}
