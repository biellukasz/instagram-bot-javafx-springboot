package instabot.entitys;

import javax.persistence.Convert;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
public class BotStatistics extends BaseEntity {

    private String accountUUID;

    private String username;


    @Convert(converter = LocalDateTimeConverter.class)
    private LocalDateTime followedtime;

    public BotStatistics(String accountUUID, String username, LocalDateTime followedtime) {
        this.accountUUID = accountUUID;
        this.username = username;
        this.followedtime = followedtime;
    }

    public BotStatistics() {
    }

    public String getAccountUUID() {
        return accountUUID;
    }

    public void setAccountUUID(String accountUUID) {
        this.accountUUID = accountUUID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getFollowedtime() {
        return followedtime;
    }

    public void setFollowedtime(LocalDateTime followedtime) {
        this.followedtime = followedtime;
    }
}
