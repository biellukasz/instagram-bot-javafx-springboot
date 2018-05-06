package instabot.seleniumconfig;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class SeleniumConf {
    WebDriver driver;

    public void setChromeDriver() throws IOException {
        String exePath = new ClassPathResource("chromedriver.exe").getFile().getAbsolutePath();
        System.setProperty("webdriver.chrome.driver", exePath);
        this.driver = new ChromeDriver();
        System.out.println("Chrome Driver loaded, using new profile.");
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void setChromeProfile() throws IOException {
        String exePath = new ClassPathResource("chromedriver.exe").getFile().getAbsolutePath();
        System.setProperty("webdriver.chrome.driver", exePath);
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("user-data-dir=C:\\Users\\Diego\\AppData\\Local\\Google\\Chrome\\User Data");
//        this.driver = new ChromeDriver(options);
//        System.out.println("Chrome Driver loaded, using local profile.");
    }
}
