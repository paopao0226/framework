package org.csu.mypetstore.service;

import org.csu.mypetstore.domain.Daily;
import org.csu.mypetstore.persistence.DailyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DailyService {
    @Autowired
    private DailyMapper dailyMapper;

    public List<Daily> getDailyByUserId(String userId){
        return dailyMapper.getDailyByUserId(userId);
    }
    public List<Daily> getDailyByUserIdAndKind(String userId,String kind){
        return  dailyMapper.getDailyByUserIdAndKind(userId,kind);
    }
    public void insertDaily(Daily daily){
        dailyMapper.insertDaily(daily);
    }
}
