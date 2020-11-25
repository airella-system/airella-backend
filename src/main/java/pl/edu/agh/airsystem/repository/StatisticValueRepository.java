package pl.edu.agh.airsystem.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;
import pl.edu.agh.airsystem.model.database.statistic.StatisticValue;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StatisticValueRepository extends CrudRepository<StatisticValue, Long> {
    Optional<StatisticValue> findFirstByStatisticAndTimestampAfterAndTimestampBeforeOrderByTimestampDesc(Statistic statistic, Instant after, Instant before);

    List<StatisticValue> findAllByStatisticAndTimestampAfterAndTimestampBeforeOrderByTimestampDesc(Statistic statistic, Instant after, Instant before);

    @Query("SELECT AVG(m.value) FROM StatisticValueFloat m WHERE m.statistic=(:statistic) AND m.timestamp < (:before) AND m.timestamp > (:after)")
    Double findAverageByStatisticAndTimestampAfterAndTimestampBefore(@Param("statistic") Statistic statistic, @Param("after") Instant after, @Param("before") Instant before);

    @Transactional
    @Modifying
    @Query("DELETE FROM StatisticValue sv WHERE sv.statistic.dbId IN (:ids)")
    void deleteAllMeasurementsForSelectedStatistics(@Param("ids") Set<Long> ids);
}
