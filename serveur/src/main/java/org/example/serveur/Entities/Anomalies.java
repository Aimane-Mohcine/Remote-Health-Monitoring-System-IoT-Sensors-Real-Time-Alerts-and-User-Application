package org.example.serveur.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;


@Entity

public class Anomalies {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    Long id;
    @Column(name = "SENSOR_ID")
    private String sensorId; // Nom conforme à camelCase
    private LocalDate date; // Attribut pour la date (YYYY-MM-DD)
    private LocalTime heure; // Attribut pour l'heure (HH:mm)
     Double heart_rate;
    String anomalyType;
    String commentaire;


    public Anomalies() {
    }
    public Anomalies(String SENSOR_ID,Long  timestamp, Double heart_rate,String anomalyType,String commentaire) {
        this.sensorId = SENSOR_ID;
        this.date = LocalDate.now();
        // Conversion en heure locale
      Long   timestampInMilliseconds = timestamp * 1000;
        heure = Instant.ofEpochMilli(timestampInMilliseconds)
                .atZone(ZoneId.systemDefault()) // Fuseau horaire système
                .toLocalTime();


        this.heart_rate = heart_rate;
this.anomalyType = anomalyType;
this.commentaire = commentaire;
    }


    public String getSENSOR_ID() {
        return sensorId;
    }

    public void setSENSOR_ID(String SENSOR_ID) {
        this.sensorId = SENSOR_ID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeure() {
        return heure;
    }

    public void setHeure(LocalTime heure) {
        this.heure = heure;
    }


    public Double getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(Double heart_rate) {
        this.heart_rate = heart_rate;
    }

    public String getAnomalyType() {
        return anomalyType;
    }

    public void setAnomalyType(String anomalyType) {
        this.anomalyType = anomalyType;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    @Override
    public String toString() {
        return "Anomalies{" +
                "id=" + id +
                ", SENSOR_ID='" + sensorId + '\'' +
                ", date=" + (date != null ? date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "null") +
                ", heure=" + (heure != null ? heure.format(DateTimeFormatter.ofPattern("HH:mm")) : "null") +
                ", heart_rate=" + heart_rate +
                ", anomalyType='" + anomalyType + '\'' +
                ", commentaire='" + commentaire + '\'' +
                '}';
    }

}
