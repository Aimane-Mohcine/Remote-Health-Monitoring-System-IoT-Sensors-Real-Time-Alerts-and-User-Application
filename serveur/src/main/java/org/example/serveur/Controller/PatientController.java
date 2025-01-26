package org.example.serveur.Controller;


import jakarta.mail.internet.MimeMessage;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.serveur.Entities.Anomalies;
import org.example.serveur.Entities.Patient;
import org.example.serveur.Entities.Statistiques;

import org.example.serveur.Repository.AnomaliesRepository;
import org.example.serveur.Repository.PatientRepository;
import org.example.serveur.Repository.StatistiquesRepository;
import org.example.serveur.services.CloudinaryService;
import org.example.serveur.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/iot")
@CrossOrigin(origins = "http://localhost:4200") // Autorise Angular

public class PatientController {

    @Autowired
    PatientRepository patientRepository;
    @Autowired
    StatistiquesRepository statistiquesRepository;
    @Autowired
    CloudinaryService cloudinaryService;
    @Autowired
    JwtService jwtService;

    @Autowired
    private JavaMailSender mailSender;

    private static final String SALT = "$2a$10$abcdefghijklmnopqrstuv"; // Salt fixe
    @Autowired
    private AnomaliesRepository anomaliesRepository;

    public PatientController() throws MqttException {
    }


    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        String saltedPassword = SALT + password;
        byte[] hash = digest.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();

        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }

        return hexString.toString();
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
    public String test() {

        return "test is here ";
    }


/////////////////////////////////////////////////////////////////////////////////////////


    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerPatient(
            @RequestParam("sensorId") String sensorId,
            @RequestParam("nom") String nom,
            @RequestParam("prenom") String prenom,
            @RequestParam("adresse") String adresse,
            @RequestParam("telephone") String telephone,
            @RequestParam("email") String email,
            @RequestParam("dateNaissance") String dateNaissance,
            @RequestParam("weight") String weight,
            @RequestParam("motDePasse") String motDePasse,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException, NoSuchAlgorithmException {

        // Vérifiez si le patient existe déjà dans la base de données
        if (patientRepository.findBySensorId(sensorId).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Un patient avec cet ID capteur existe déjà."));
        }

        if (patientRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Un patient avec cet email existe déjà."));
        }

        // Créez un nouvel objet Patient
        Patient patient = new Patient();

        // Hachage du mot de passe
        motDePasse = hashPassword(motDePasse);

        // Téléchargement de l'image vers Cloudinary
        String imageUrl = null;
        String publicId = null;
        if (image != null && !image.isEmpty()) {
            Map<String, Object> uploadResult = cloudinaryService.uploadImage(image);
            imageUrl = (String) uploadResult.get("url");
            publicId = (String) uploadResult.get("public_id");
        }

        // Configuration des données du patient
        patient.setSensorId(sensorId);
        patient.setNom(nom);
        patient.setPrenom(prenom);
        patient.setAdresse(adresse);
        patient.setTelephone(telephone);
        patient.setWeight(Double.parseDouble(weight));
        patient.setEmail(email);
        patient.setDateNaissance(dateNaissance);
        patient.setCloudinaryPublicId(publicId);
        patient.setPhotoUrl(imageUrl);
        patient.setMotDePasse(motDePasse);

        try {
            // Sauvegarder le patient dans la base de données
            patientRepository.save(patient);

            // Enregistrer les statistiques du patient
            Statistiques statistiques = new Statistiques(sensorId, dateNaissance, Double.parseDouble(weight));
            statistiquesRepository.save(statistiques);

            // Retourner une réponse JSON de succès
            return ResponseEntity.ok(Map.of("message", "Patient enregistré avec succès !"));
        } catch (RuntimeException e) {
            // Retourner une réponse JSON d'erreur
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Erreur lors de l'enregistrement : " + e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) throws NoSuchAlgorithmException {
        String email = credentials.get("email");
        String password = credentials.get("password");

        // Vérifier si l'utilisateur existe
        Optional<Patient> userOptional = patientRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email introuvable.");
        }

        Patient patient = userOptional.get();
        // Hacher le mot de passe reçu pour comparaison
        String hashedPassword = hashPassword(password);


        // Comparer le mot de passe haché
        if (!patient.getMotDePasse().equals(hashedPassword)) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Mot de passe incorrect.");
        }


        String token = jwtService.generateToken(patient.getSENSOR_ID());
        return ResponseEntity.ok(Map.of(
                "idSenser", patient.getSENSOR_ID(),
                "token", token
        ));

    }


    private Map<String, String> resetCodes = new HashMap<>();

    @PostMapping("/forgot-password")
    public ResponseEntity<?> sendResetCode(@RequestBody String email) {

        Optional<Patient> patientOptional = patientRepository.findByEmail(email);
        if (patientOptional.isPresent()) {
            String resetCode = UUID.randomUUID().toString().substring(0, 6); // Generate a 6-character code
            resetCodes.put(email, resetCode);

            // Send reset code to user's email
            try {
                MimeMessage mimeMessage = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

                helper.setTo(email);
                helper.setSubject("Password Reset Code");

                // HTML message with the reset code in bold
                String htmlMsg = "<p>Hello,</p>"
                        + "<p>You requested to reset your password. Here is your reset code:</p>"
                        + "<h3><strong>" + resetCode + "</strong></h3>"  // Bold code
                        + "<p>Please use this code to reset your password.</p>"
                        + "<p>If you didn't request a password reset, you can ignore this email.</p>"
                        + "<p>Best regards,<br>HeartSense Team</p>";

                helper.setText(htmlMsg, true); // true indicates HTML content

                mailSender.send(mimeMessage);
            } catch (Exception e) {
                return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"Failed to send email\"}");
            }

            return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Reset code sent to your email\"}");
        } else {
            return ResponseEntity.status(404).body("{\"success\": false, \"message\": \"L'email fourni est introuvable \"}");
        }
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyResetCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        if (resetCodes.containsKey(email) && resetCodes.get(email).equals(code)) {
            return ResponseEntity.ok("{\"success\": true, \"message\": \"Code vérifié avec succès.\"}");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"success\": false, \"message\": \"Code incorrect.\"}");
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        Optional<Patient> patientOptional = patientRepository.findByEmail(email);
        if (patientOptional.isPresent()) {
            Patient patient = patientOptional.get();

            try {
                // Hacher le mot de passe avec gestion de l'exception
                String hashedPassword = hashPassword(newPassword);
                patient.setMotDePasse(hashedPassword);
                patientRepository.save(patient);
                return ResponseEntity.ok().body("{\"success\": true, \"message\": \"Password updated\"}");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
                return ResponseEntity.status(500).body("{\"success\": false, \"message\": \"Error hashing password\"}");
            }
        } else {
            return ResponseEntity.status(404).body("{\"success\": false, \"message\": \"User not found\"}");
        }

    }





    @GetMapping("/statistique/{sensorId}")

    public ResponseEntity<?> getBySensorId(@PathVariable String sensorId) {
        Optional<Statistiques> optionalStatistiques = statistiquesRepository.findBySensorId(sensorId);

        if (optionalStatistiques.isPresent()) {
            Statistiques statistiques = optionalStatistiques.get();

            LocalDate today = LocalDate.now(); // Date actuelle
            Long count = anomaliesRepository.countAnomaliesBySensorIdAndToday(sensorId, today);
            statistiques.setNombreAnomalie(count);




            return ResponseEntity.ok(statistiques); // Retourner les données trouvées
        } else {
            return ResponseEntity.status(404).body("{\"message\": \"Sensor not found\"}");
        }
    }



    @GetMapping("/Anomalies/{sensorId}")
    public ResponseEntity<?> getAnomalies(@PathVariable String sensorId) {
        List<Anomalies> anomalies = anomaliesRepository.findTop3BySensorIdOrderByDateDescHeureDesc(sensorId);

        if (anomalies.isEmpty()) {
            return ResponseEntity.status(404).body("{\"message\": \"No anomalies found for the given SENSOR_ID\"}");
        }

        return ResponseEntity.ok(anomalies);
    }


    @GetMapping("/latest/{sensorId}")
    public ResponseEntity<?> getLatestAnomalies(@PathVariable String sensorId) {
        // Limiter à 3 résultats
        List<Anomalies> anomalies = anomaliesRepository.findBySensorIdOrderByDateDescHeureDesc(sensorId, PageRequest.of(0, 3));

        if (anomalies.isEmpty()) {
            return ResponseEntity.status(404).body("{\"message\": \"No anomalies found for the given SENSOR_ID\"}");
        }

        return ResponseEntity.ok(anomalies);
    }


    @GetMapping("/pation/{sensorId}")
    public ResponseEntity<Patient> getPatientById(@PathVariable String sensorId) {
        // Rechercher le patient dans la base de données
        Optional<Patient> patientOpt = patientRepository.findBySensorId(sensorId);

        // Si le patient n'existe pas, retourner une réponse 404
        if (patientOpt.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Retourner le patient avec un statut 200 OK
        return new ResponseEntity<>(patientOpt.get(), HttpStatus.OK);
    }



    @PatchMapping("/update/{sensorId}")
    public ResponseEntity<Map<String, String>> updatePatient(@PathVariable String sensorId, @RequestBody Map<String, Object> profileData) throws NoSuchAlgorithmException {
        Optional<Patient> existingPatientOpt = patientRepository.findBySensorId(sensorId);
        if (existingPatientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Patient introuvable."));
        }

        Patient existingPatient = existingPatientOpt.get();

        if (profileData.containsKey("nom")) {
            existingPatient.setNom((String) profileData.get("nom"));
        }
        if (profileData.containsKey("prenom")) {
            existingPatient.setPrenom((String) profileData.get("prenom"));
        }
        if (profileData.containsKey("adresse")) {
            existingPatient.setAdresse((String) profileData.get("adresse"));
        }
        if (profileData.containsKey("telephone")) {
            existingPatient.setTelephone((String) profileData.get("telephone"));
        }
        if (profileData.containsKey("email")) {
            existingPatient.setEmail((String) profileData.get("email"));
        }
        if (profileData.containsKey("dateNaissance")) {
            existingPatient.setDateNaissance((String) profileData.get("dateNaissance"));
        }
        if (profileData.containsKey("weight")) {
            existingPatient.setWeight(Double.valueOf(profileData.get("weight").toString()));
        }
        if (profileData.containsKey("motDePasse")) {
            existingPatient.setMotDePasse(hashPassword((String) profileData.get("motDePasse")));
        }

        patientRepository.save(existingPatient);

        return ResponseEntity.ok(Map.of("message", "Profil mis à jour avec succès."));
    }


    ////////////////////////////////////////////////////////////////////////


}


