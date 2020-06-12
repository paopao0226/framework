package org.csu.mypetstore.persistence;

import org.csu.mypetstore.domain.Daily;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyMapper {
    List<Daily> getDailyByUserId(String userId);
    void insertDaily(Daily daily);
    List<Daily> getDailyByUserIdAndKind(String userId, String kind);
}
