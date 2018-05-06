package instabot.repository;

import instabot.entitys.InstagramAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstagramAccountRepo extends JpaRepository<InstagramAccount,Long> {

    InstagramAccount findByLogin(String login);

}
