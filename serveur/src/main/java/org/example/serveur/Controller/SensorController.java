package org.example.serveur.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.cbor.CBORFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.example.serveur.Model.SensorData;
import org.springframework.web.bind.annotation.*;
@RestController
@CrossOrigin
public class SensorController {
    private static final String BROKER = "tcp://mqtt.eclipseprojects.io:1883";
    private static final String CLIENT_ID = "chart";
    private final MqttClient mqttClient;
    private final ObjectMapper mapper;

    public SensorController() throws MqttException {
        this.mqttClient = new MqttClient(BROKER, CLIENT_ID);
        this.mapper = new ObjectMapper(new CBORFactory());

        // Connecter le client lors de l'initialisation
        if (!mqttClient.isConnected()) {
            mqttClient.connect();
        }
    }

    @GetMapping("/data/{sensorId}")
    public SensorData getSensorData(@PathVariable String sensorId) throws Exception {
        System.out.println("Récupération des données pour le capteur : " + sensorId);

        // Correction du topic
        String topic = "iot/heart_rate/" + sensorId;
        final byte[][] sensorPayload = {null};

        // Vérifier si le client est connecté
        if (!mqttClient.isConnected()) {
            mqttClient.connect();
        }

        // S'abonner au topic MQTT pour le capteur spécifié
        mqttClient.subscribe(topic, (receivedTopic, message) -> {
            if (receivedTopic.equals(topic)) {
                sensorPayload[0] = message.getPayload();
            }
        });

        // Attendre pour recevoir les données
        Thread.sleep(7000);

        if (sensorPayload[0] == null) {
            throw new RuntimeException("Aucune donnée reçue pour le capteur : " + sensorId);
        }

        // Décoder le payload CBOR en JSON
        JsonNode rootNode = mapper.readTree(sensorPayload[0]);

        // Extraire les informations
        if (rootNode.has("e") && rootNode.get("e").isArray() && rootNode.get("e").size() > 0) {
            JsonNode firstElement = rootNode.get("e").get(0);
            ((ObjectNode) rootNode).put("n", firstElement.get("n").asText());
            ((ObjectNode) rootNode).put("u", firstElement.get("u").asText());
            ((ObjectNode) rootNode).put("v", firstElement.get("v").asDouble());
            ((ObjectNode) rootNode).remove("e");
        }

        // Transformer en objet SensorData
        return mapper.treeToValue(rootNode, SensorData.class);
    }
}
