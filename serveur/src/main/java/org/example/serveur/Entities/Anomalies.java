package org.example.serveur.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

@Entity

public class Anomalies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long id;
    String SENSOR_ID;
    Date timestamp;
    Double heart_rate;
    String anomalyType;
    String commentaire;

}
