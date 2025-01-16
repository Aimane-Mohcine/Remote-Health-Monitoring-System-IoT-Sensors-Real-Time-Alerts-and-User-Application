package org.example.serveur.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.eclipse.paho.client.mqttv3.*;
import org.example.serveur.Entities.Statistiques;
import org.example.serveur.Model.stats_data;
import org.example.serveur.Repository.StatistiquesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.springframework.core.io.support.SpringFactoriesLoader.FailureHandler.handleMessage;

@Service
public class MqttStatistiquesService {

    private static final String BROKER = "tcp://mqtt.eclipseprojects.io:1883";
    private static final String TOPIC_STATS = "iot/update/#"; // Topic pour les statistiques
    private static final String CLIENT_ID = "Server_Update";


    @Autowired
    private StatistiquesRepository statistiquesRepository;
    private final MqttClient mqttClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private ExecutorService executorService = Executors.newFixedThreadPool(10); // 10 threads pour traiter les messages


    public MqttStatistiquesService() throws MqttException {


        // Initialisation du client MQTT
        mqttClient = new MqttClient(BROKER, CLIENT_ID);
        mqttClient.connect();

        // Callback pour traiter les messages reçus
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                System.out.println("Connexion perdue avec le broker MQTT");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                executorService.submit(() -> {
                    try {
                        handleMessage(topic, message.getPayload());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {
                // Pas d'action nécessaire pour un client qui ne publie pas
            }
        });

        // S'abonner au topic
        mqttClient.subscribe(TOPIC_STATS);
        System.out.println("Souscrit au topic : " + TOPIC_STATS);
    }

    private final Object lock = new Object();

    private void handleMessage(String topic, byte[] payload) throws IOException {
        synchronized (lock) {
            System.out.println("update de capteur id  :" + topic);


            // Créer un ObjectMapper pour CBOR
            CBORFactory factory = new CBORFactory();
            ObjectMapper objectMapper = new ObjectMapper(factory);

            // Lire le message CBOR en tant que JSON
            JsonNode rootNode = objectMapper.readTree(payload);

            // Extraire les champs principaux
            String bn = rootNode.get("bn").asText();
            long bt = rootNode.get("bt").asLong();

            // Lire le tableau "e" pour les métriques
            JsonNode measurements = rootNode.get("e");
            Double averageHeartRate = null;
            Double minHeartRate = null;
            Double maxHeartRate = null;

            if (measurements.isArray()) {
                for (JsonNode node : measurements) {
                    String name = node.get("n").asText();
                    Double value = node.get("v").asDouble();

                    switch (name) {
                        case "average_heart_rate":
                            averageHeartRate = value;
                            break;
                        case "min_heart_rate":
                            minHeartRate = value;
                            break;
                        case "max_heart_rate":
                            maxHeartRate = value;
                            break;
                    }
                }
            }

            // Construire l'objet StatsData
            stats_data statsData = new stats_data();
            statsData.setBn(bn);
            statsData.setBt(bt);
            statsData.setAverageHeartRate(averageHeartRate);
            statsData.setMinHeartRate(minHeartRate);
            statsData.setMaxHeartRate(maxHeartRate);

        System.out.println("upadte sa : "+statsData);



            // Récupérer les statistiques par l'identifiant du capteur
            Optional<Statistiques> optionalStatistiques = statistiquesRepository.findById(statsData.getBn());

// Vérifier si la ligne existe
            if (optionalStatistiques.isPresent()) {
                // Si la ligne existe, effectuer la mise à jour
                Statistiques s = optionalStatistiques.get();
                s.setLastUpdate(LocalDateTime.now());
                s.setMax(statsData.getMaxHeartRate());
                s.setMin(statsData.getMinHeartRate());
                s.setMoyenne(statsData.getAverageHeartRate());

                // Sauvegarder les modifications
                statistiquesRepository.save(s);
                System.out.println("Update effectué pour le capteur : " + statsData.getBn());
            } else {
                // Si la ligne n'existe pas, ne rien faire
                System.out.println("Aucune donnée trouvée pour le capteur : " + statsData.getBn() + ". Aucun update effectué.");
            }


        }
    }
}

