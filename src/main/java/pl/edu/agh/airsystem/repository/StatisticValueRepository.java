package pl.edu.agh.airsystem.repository;

import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface StatisticValueRepository extends CrudRepository<StatisticValue, Long> {
    Optional<StatisticValue> findFirstByStatisticAndTimestampAfterAndTimestampBeforeOrderByTimestampDesc(Statistic statistic, Instant after, Instant before);

    List<StatisticValue> findAllByStatisticAndTimestampAfterAndTimestampBeforeOrderByTimestampDesc(Statistic statistic, Instant after, Instant before);

}
