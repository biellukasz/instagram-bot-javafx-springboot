package instabot.repository;

import instabot.entitys.BotStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BotStatisticsRepo extends JpaRepository<BotStatistics,Long> {
}
