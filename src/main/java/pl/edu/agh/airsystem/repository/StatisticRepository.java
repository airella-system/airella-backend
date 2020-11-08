package pl.edu.agh.airsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import pl.edu.agh.airsystem.model.database.statistic.Statistic;

import java.util.Optional;

public interface StatisticRepository extends JpaRepository<Statistic, Long> {
    Optional<Statistic> findByStation_IdAndId(String stationId, String statisticId);
}