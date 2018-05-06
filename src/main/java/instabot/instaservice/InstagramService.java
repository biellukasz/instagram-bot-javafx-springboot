package instabot.instaservice;

import instabot.entitys.BotStatistics;
import instabot.entitys.InstagramAccount;
import instabot.repository.BotStatisticsRepo;
import instabot.repository.InstagramAccountRepo;
import instabot.utils.Utils;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class InstagramService {


    private Utils utils;

    private InstagramAccountRepo instagramAccountRepo;

    private BotStatisticsRepo botStatisticsRepo;

    private DemoAplicationService demoAplicationService;

    @Autowired
    public InstagramService(Utils utils, InstagramAccountRepo instagramAccountRepo, BotStatisticsRepo botStatisticsRepo, DemoAplicationService demoAplicationService) {
        this.utils = utils;
        this.instagramAccountRepo = instagramAccountRepo;
        this.botStatisticsRepo = botStatisticsRepo;
        this.demoAplicationService = demoAplicationService;
    }

    public boolean checkAccountAndSavetoDB(String login, String password, TextArea logbox)  {
        System.setProperty("webdriver.chrome.driver", "C:\\driver\\chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("headless");
//        options.addArguments("window-size=1200x600");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://www.instagram.com/accounts/login/");
        utils.fluentWait(By.xpath("//*[@name=\"username\"]"), webDriver).sendKeys(login);
        utils.fluentWait(By.xpath("//*[@name=\"password\"]"), webDriver).sendKeys(password);
        utils.addLogLine(logbox,"Checking account");
        WebElement webElement = webDriver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/article/div/div[1]/div/form/span/button"));
        webElement.click();
        if (/*utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[2]/input"), webDriver).isDisplayed() &&*/ isAccountGood(webDriver)) {
            utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/nav/div[2]/div/div/div[3]/div/div[3]/a"), webDriver).click();
            Long followerscount = Long.valueOf(utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/header/section/ul/li[2]/a/span"), webDriver).getAttribute("title"));
            instagramAccountRepo.save(new InstagramAccount(login, password, followerscount, LocalDateTime.now()));
        } else {
            webDriver.close();
            utils.addLogLine(logbox,"Incorrect login and password ");
            return false;
        }
        utils.addLogLine(logbox,"Account saved in Data Base");
        webDriver.close();
        return true;

    }

    public void followFromGivenHashtag(String hashtag, String username, TextArea logbox,int numberOfLikes)  {
//        if(demoAplicationService.checkNumbersOfFollowed()){
//            System.exit(0);
//            Platform.exit();
//        }
        Set<String> links = new HashSet<>();
        System.setProperty("webdriver.chrome.driver", "C:\\driver\\chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("headless");
//        options.addArguments("window-size=1200x600");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://www.instagram.com/accounts/login/");
        InstagramAccount account = instagramAccountRepo.findByLogin(username);
        utils.fluentWait(By.xpath("//*[@name=\"username\"]"), webDriver).sendKeys(account.getLogin());
        utils.fluentWait(By.xpath("//*[@name=\"password\"]"), webDriver).sendKeys(account.getPassword());
        utils.addLogLine(logbox, "Login using " + account.getLogin() + " " + account.getPassword());
        WebElement webElement = webDriver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/article/div/div[1]/div/form/span/button"));
        webElement.click();
        webDriver.get("https://www.instagram.com/explore/tags/" + hashtag + "/");
        WebElement pageBody = utils.fluentWait(By.tagName("body"), webDriver);
        while (links.size() < numberOfLikes) {
            pageBody.sendKeys(Keys.PAGE_DOWN);
            pageBody.sendKeys(Keys.PAGE_DOWN);
            utils.waitGivenSeconds(2);
            getPhotolinksFromRow(3, webDriver, links);
            getPhotolinksFromRow(4, webDriver, links);
            getPhotolinksFromRow(5, webDriver, links);
            getPhotolinksFromRow(6, webDriver, links);
            utils.addLogLine(logbox, "Number of scraped links to follow " + links.size());
        }
        utils.addLogLine(logbox, "Starting following!");

        for (String link : links) {
            webDriver.get(link);
            if (isAvailable(webDriver)) {
                WebElement fav = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/div/div/article/header/div[2]/div[1]/div[2]/span[2]/button"), webDriver);
                fav.click();
                utils.waitGivenSeconds(2);
                WebElement profile = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/div/div/article/header/div[2]/div[1]/div[1]/a"), webDriver);
                String title = profile.getAttribute("title");
                utils.addLogLine(logbox, "Followed  " + title);
                botStatisticsRepo.save(new BotStatistics(account.getUuid(), title, LocalDateTime.now()));
            }

        }
        utils.addLogLine(logbox, "Job finished");
        webDriver.quit();
        webDriver.close();
    }

    private void getPhotolinksFromRow(int row, WebDriver driver, Set<String> links) {
        WebElement firstPhoto = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[2]/div[1]/div[" + row + "]/div[1]/a"), driver);
        WebElement secondPhoto = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[2]/div[1]/div[" + row + "]/div[2]/a"), driver);
        WebElement thirdPhoto = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/article/div[2]/div[1]/div[" + row + "]/div[3]/a"), driver);

        String firstLink = firstPhoto.getAttribute("href");
        String secondLink = secondPhoto.getAttribute("href");
        String thirdLink = thirdPhoto.getAttribute("href");


        String[] firstLinkStripped = firstLink.split("\\?");
        String[] secondLinkStripped = secondLink.split("\\?");
        String[] thirdLinkStripped = thirdLink.split("\\?");

        List<String> tempLinks = Arrays.asList(firstLinkStripped[0], secondLinkStripped[0], thirdLinkStripped[0]);
        links.addAll(tempLinks);
    }

    private boolean isAvailable(WebDriver driver) {
        boolean isAvailable = true;
        WebElement pageBody = utils.fluentWait(By.tagName("body"), driver);
        String bodyClass = pageBody.getAttribute("class");
        if (bodyClass.equals(" p-error dialog-404")) {
            isAvailable = false;
        }
        return isAvailable;
    }
    private boolean isAccountGood(WebDriver driver){
        boolean isAccountGood = true;
        WebElement slfErrorAlert1 = utils.fluentWait(By.id("slfErrorAlert"), driver);
        if(slfErrorAlert1 != null){
            isAccountGood = false;
        }
        return isAccountGood;
    }
    public void likeFromGivenHashtag(String hashtag, String username, TextArea logbox,int numberOfLikes)  {
//        if(demoAplicationService.checkNumbersOfFollowed()){
//            System.exit(0);
//            Platform.exit();
//        }
        Set<String> links = new HashSet<>();
        System.setProperty("webdriver.chrome.driver", "C:\\driver\\chromedriver.exe");
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("headless");
//        options.addArguments("window-size=1200x600");
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://www.instagram.com/accounts/login/");
        InstagramAccount account = instagramAccountRepo.findByLogin(username);
        utils.fluentWait(By.xpath("//*[@name=\"username\"]"), webDriver).sendKeys(account.getLogin());
        utils.fluentWait(By.xpath("//*[@name=\"password\"]"), webDriver).sendKeys(account.getPassword());
        utils.addLogLine(logbox, "Login using " + account.getLogin() + " " + account.getPassword());
        WebElement webElement = webDriver.findElement(By.xpath("//*[@id=\"react-root\"]/section/main/div/article/div/div[1]/div/form/span/button"));
        webElement.click();
        webDriver.get("https://www.instagram.com/explore/tags/" + hashtag + "/");
        WebElement pageBody = utils.fluentWait(By.tagName("body"), webDriver);
        while (links.size() < numberOfLikes) {
            pageBody.sendKeys(Keys.PAGE_DOWN);
            pageBody.sendKeys(Keys.PAGE_DOWN);
            utils.waitGivenSeconds(2);
            getPhotolinksFromRow(3, webDriver, links);
            getPhotolinksFromRow(4, webDriver, links);
            getPhotolinksFromRow(5, webDriver, links);
            getPhotolinksFromRow(6, webDriver, links);
            utils.addLogLine(logbox, "Number of scraped photos to follow " + links.size());
        }
        utils.addLogLine(logbox, "Starting likes job!");

        for (String link : links) {
            webDriver.get(link);
            if (isAvailable(webDriver)) {
                WebElement fav = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/div/div/article/div[2]/section[1]/a[1]"), webDriver);
                utils.waitGivenSeconds(2);
                fav.click();
                utils.waitGivenSeconds(1);
                WebElement profile = utils.fluentWait(By.xpath("//*[@id=\"react-root\"]/section/main/div/div/article/header/div[2]/div[1]/div[1]/a"), webDriver);
                String title = profile.getAttribute("title");
                utils.addLogLine(logbox, "Liked  " + title);
            }

        }
        utils.addLogLine(logbox, "Job finished");
        webDriver.quit();
        webDriver.close();
    }

}
