package org.example.serveur.Repository;

import org.example.serveur.Entities.Anomalies;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface AnomaliesRepository extends JpaRepository<Anomalies, String> {
    // Ajoutez des méthodes de requête si nécessaire
    @Query("SELECT COUNT(a) FROM Anomalies a WHERE a.sensorId = :sensorId AND a.date = :currentDate")
    Long countAnomaliesBySensorIdAndToday(@Param("sensorId") String sensorId, @Param("currentDate") LocalDate currentDate);
    List<Anomalies> findBySensorId(String sensorId); // Nom correct
    @Query("SELECT a FROM Anomalies a WHERE a.sensorId = :sensorId ORDER BY a.date DESC, a.heure DESC")
    List<Anomalies> findTop3BySensorIdOrderByDateDescHeureDesc(@Param("sensorId") String sensorId);

    List<Anomalies> findBySensorIdOrderByDateDescHeureDesc(String sensorId, Pageable pageable);

}

