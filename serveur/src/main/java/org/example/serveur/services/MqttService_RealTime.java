package org.example.serveur.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.eclipse.paho.client.mqttv3.*;
import org.example.serveur.Model.SensorData;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class MqttService_RealTime {
    private static final String BROKER = "tcp://mqtt.eclipseprojects.io:1883";
    private static final String TOPIC = "iot/heart_rate/#"; // Souscrire à tous les capteurs
    private static final String CLIENT_ID = "Server";

    private ExecutorService executorService = Executors.newFixedThreadPool(10); // 10 threads pour traiter les messages


    private static final long ALERT_INTERVAL = 1; // Intervalle d'alerte en minutes
    private final Cache<String, Long> lastAlertTimestamps;

    private final MqttClient mqttClient;

    public MqttService_RealTime() throws MqttException {
        // Initialiser Caffeine Cache avec expiration après 15 minutes
        lastAlertTimestamps = Caffeine.newBuilder()
                .expireAfterWrite(ALERT_INTERVAL, TimeUnit.MINUTES)
                .build();

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
        mqttClient.subscribe(TOPIC);
        System.out.println("Souscrit au topic : " + TOPIC);
    }

    private void handleMessage(String topic, byte[] payload) throws IOException {
        // Initialiser Jackson avec CBORFactory
        CBORFactory factory = new CBORFactory();
        ObjectMapper mapper = new ObjectMapper(factory);

        // Lire le payload en tant que JsonNode
        JsonNode rootNode = mapper.readTree(payload);

        // Extraire les informations nécessaires
        if (rootNode.has("e") && rootNode.get("e").isArray()) {
            JsonNode firstElement = rootNode.get("e").get(0);
            ((ObjectNode) rootNode).put("n", firstElement.get("n").asText());
            ((ObjectNode) rootNode).put("u", firstElement.get("u").asText());
            ((ObjectNode) rootNode).put("v", firstElement.get("v").asDouble());
            ((ObjectNode) rootNode).remove("e"); // Supprimer le tableau "e"
        }


        // Transformer en objet SensorData
        SensorData sensorData = mapper.treeToValue(rootNode, SensorData.class);
        Double avgHeartRate = sensorData.getV();
        String sensorId = sensorData.getBn(); // Identifiant unique du capteur

        System.out.println("this is "+ sensorId);

        // Détection des Anomalies
        String anomalyType = null;
        String explanation = null;

        if (avgHeartRate <= 0 || avgHeartRate > 290) {
            anomalyType = "Données incohérentes";
            explanation = "🔎 Explication : Les données reçues semblent incorrectes. Cela peut être dû à un problème technique ou à un mauvais positionnement du capteur.\n" +
                    "➡️ Recommandation : Vérifiez que le capteur est bien positionné ou remplacez-le s'il est défectueux.";
        } else if (avgHeartRate < 40) {
            anomalyType = "Fréquence critique basse";
            explanation = "🔎 Explication : La fréquence cardiaque est dangereusement basse. Risque d'arrêt cardiaque.\n" +
                    "➡️ Recommandation : Consulter immédiatement un médecin ou appeler les secours.";
        } else if (avgHeartRate >= 40 && avgHeartRate < 60) {
            anomalyType = "Bradycardie";
            explanation = "🔎 Explication : Fréquence cardiaque basse pouvant causer de la fatigue ou des vertiges.\n" +
                    "➡️ Recommandation : Surveiller les symptômes. Consulter un médecin si cela persiste.";
        } else if (avgHeartRate >= 60 && avgHeartRate <= 100) {
            anomalyType = null;
            explanation = "✅ Explication : Fréquence cardiaque normale. Aucun problème détecté.";
        } else if (avgHeartRate > 100 && avgHeartRate <= 180) {
            anomalyType = "Tachycardie";
            explanation = "🔎 Explication : Fréquence cardiaque élevée. Cela peut être lié au stress ou à l'effort.\n" +
                    "➡️ Recommandation : Reposez-vous immédiatement et surveillez les symptômes. Consulter un médecin si cela persiste.";
        } else if (avgHeartRate > 180 && avgHeartRate <= 290) {
            anomalyType = "Fréquence critique élevée";
            explanation = "🔎 Explication : La fréquence cardiaque est extrêmement élevée, ce qui peut indiquer une urgence médicale.\n" +
                    "➡️ Recommandation : Contactez immédiatement les secours ou rendez-vous aux urgences.";
        }

        // Gestion des alertes avec Caffeine Cache
        if (anomalyType != null) {
            long currentTime = System.currentTimeMillis();

            // Vérifier le dernier horodatage pour ce capteur
            Long lastAlertTime = lastAlertTimestamps.getIfPresent(sensorId);
            if (lastAlertTime == null || (currentTime - lastAlertTime) > ALERT_INTERVAL * 60 * 1000) {
                // Envoyer l'alerte
                System.out.println("⚠️ Alerte pour le capteur " + sensorId + " : " + anomalyType);
                System.out.println(explanation);

                // Mettre à jour l'horodatage dans la cache
                lastAlertTimestamps.put(sensorId, currentTime);
            } else {
                // Ignorer les alertes répétées
                System.out.println("🔔 Aucune nouvelle alerte pour le capteur " + sensorId +
                        ". Dernière alerte envoyée il y a moins de 15 minutes.");
            }
        }
    }
}
