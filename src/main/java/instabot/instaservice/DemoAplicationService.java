package instabot.instaservice;

import instabot.entitys.BotStatistics;
import instabot.repository.BotStatisticsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemoAplicationService {

    private BotStatisticsRepo botStatisticsRepo;

    @Autowired
    public DemoAplicationService(BotStatisticsRepo botStatisticsRepo) {
        this.botStatisticsRepo = botStatisticsRepo;
    }

    public boolean checkNumbersOfFollowed(){
        List<BotStatistics> all = botStatisticsRepo.findAll();
        if(all.size()>50){
            return true;
        }
        return false;
    }
}
