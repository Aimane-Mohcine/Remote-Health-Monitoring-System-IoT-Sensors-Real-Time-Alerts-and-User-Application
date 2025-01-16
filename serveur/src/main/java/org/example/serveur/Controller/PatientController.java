package org.example.serveur.Controller;

import org.example.serveur.Entities.Patient;
import org.example.serveur.Entities.Statistiques;
import org.example.serveur.Repository.PatientRepository;
import org.example.serveur.Repository.StatistiquesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/iot")
public class PatientController {

    @Autowired
    PatientRepository patientRepository;
    @Autowired
    StatistiquesRepository statistiquesRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;




    @GetMapping("/pation/{sensorId}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String sensorId) {
        // Rechercher le patient dans la base de données
        Optional<Patient> patientOpt = patientRepository.findById(sensorId);

        // Si le patient n'existe pas, retourner une réponse 404
        if (patientOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Retourner le patient avec un statut 200 OK
        return new ResponseEntity<>(patientOpt.get(), HttpStatus.OK);
    }

    @PostMapping("/auth")

    public ResponseEntity<String> inscrire(@RequestBody Patient patient) {


        Optional<Patient> patientOptional = patientRepository.findById(patient.getSENSOR_ID());

        if (patientOptional.isPresent()) {
            throw new RuntimeException("ce capteur déjà utilisé !");
        }
// Hashage du mot de passe
        patient.setMotDePasse(passwordEncoder.encode(patient.getMotDePasse()));
        try {
            patientRepository.save(patient);

            Statistiques statistiques = new Statistiques(patient.getSENSOR_ID(),patient.getAge(),patient.getWeight());
            statistiquesRepository.save(statistiques);
            return new ResponseEntity<>("patient inscrit avec succès !" , HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }

    @PatchMapping("/update/{sensorId}")
    public ResponseEntity<Patient> updatePatient(@PathVariable String sensorId, @RequestBody Patient updatedPatient) {
        // Récupérer le patient existant
        Optional<Patient> existingPatientOpt = patientRepository.findById(sensorId);
        if (existingPatientOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Patient non trouvé
        }

        Patient existingPatient = existingPatientOpt.get();

        // Mise à jour des attributs fournis dans la requête
            Statistiques statistiques = statistiquesRepository.findById(sensorId).get();

        if (updatedPatient.getNom() != null) {
            existingPatient.setNom(updatedPatient.getNom());
        }
        if (updatedPatient.getPrenom() != null) {
            existingPatient.setPrenom(updatedPatient.getPrenom());
        }
        if (updatedPatient.getAdresse() != null) {
            existingPatient.setAdresse(updatedPatient.getAdresse());
        }
        if (updatedPatient.getTelephone() != null) {
            existingPatient.setTelephone(updatedPatient.getTelephone());
        }
        if (updatedPatient.getEmail() != null) {
            existingPatient.setEmail(updatedPatient.getEmail());
        }
        if (updatedPatient.getAge() != null) {
            existingPatient.setAge(updatedPatient.getAge());
            statistiques.setAge(updatedPatient.getAge());

        }
        if (updatedPatient.getWeight() != null) {
            existingPatient.setWeight(updatedPatient.getWeight());
            statistiques.setWeight(updatedPatient.getWeight());

        }
        if (updatedPatient.getMotDePasse() != null) {
            existingPatient.setMotDePasse(passwordEncoder.encode(updatedPatient.getMotDePasse()));
        }

        // Sauvegarder les changements dans la base de données
        Patient savedPatient = patientRepository.save(existingPatient);
        statistiquesRepository.save(statistiques);
        return new ResponseEntity<>(savedPatient, HttpStatus.OK); // Retourner le patient mis à jour
    }



    @DeleteMapping("/delete/{sensorId}")
    public ResponseEntity<String> deletePatient(@PathVariable String sensorId) {
        // Vérifier si le patient existe
        Optional<Patient> patientOpt = patientRepository.findById(sensorId);
        if (patientOpt.isEmpty()) {
            return new ResponseEntity<>("Patient non trouvé.", HttpStatus.NOT_FOUND);
        }

        // Supprimer le patient
        patientRepository.delete(patientOpt.get());
        return new ResponseEntity<>("Patient supprimé avec succès.", HttpStatus.OK);
    }


    @GetMapping("/test")
    public  String test() {

        return "test is here ";
        }
    }





