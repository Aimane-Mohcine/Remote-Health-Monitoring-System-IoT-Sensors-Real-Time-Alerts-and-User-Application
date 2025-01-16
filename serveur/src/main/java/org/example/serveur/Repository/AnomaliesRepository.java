package org.example.serveur.Repository;

import org.example.serveur.Entities.Anomalies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnomaliesRepository extends JpaRepository<Anomalies, String> {
    // Ajoutez des méthodes de requête si nécessaire
}
