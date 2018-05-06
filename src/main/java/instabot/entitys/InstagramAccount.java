package instabot.entitys;



import javax.persistence.Convert;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class InstagramAccount extends BaseEntity {

    public InstagramAccount(String login, String password, Long followers, LocalDateTime datecreated) {
        this.login = login;
        this.password = password;
        this.followers = followers;
        this.datecreated = datecreated;
    }
    public InstagramAccount(){};

    private String login;


    private String password;


    private Long followers;


    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime datecreated;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getFollowers() {
        return followers;
    }

    public void setFollowers(Long followers) {
        this.followers = followers;
    }

    public LocalDateTime getTimestamp() {
        return datecreated;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.datecreated = timestamp;
    }
}
