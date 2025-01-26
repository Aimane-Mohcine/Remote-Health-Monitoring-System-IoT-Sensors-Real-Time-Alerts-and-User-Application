package org.example.serveur.Repository;

import org.example.serveur.Entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, String> {
    // Ajoutez des méthodes de requête si nécessaire
    Optional<Patient> findBySensorId(String sensorId);
    Optional<Patient> findByEmail(String email); // Recherche par email
}
