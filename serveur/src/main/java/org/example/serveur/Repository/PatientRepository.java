package org.example.serveur.Repository;

import org.example.serveur.Entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, String> {
    // Ajoutez des méthodes de requête si nécessaire
}
