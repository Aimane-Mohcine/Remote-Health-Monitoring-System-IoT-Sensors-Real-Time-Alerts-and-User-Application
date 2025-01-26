package org.example.serveur.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Entity


public class Statistiques {
    @Id
    private String sensorId; // Identifiant unique du capteur
    private Double moyenne;
    private Double min;
    private Double max;
    private Long nombreAnomalie;
    private LocalDateTime  lastUpdate;
    String dateNaissance;
    private Double weight;


    public Statistiques() {
    }
    public Statistiques(String sensorId, String dateNaissance,Double weight){
        this.sensorId = sensorId;
        this.moyenne = 0.0;
        this.min = 0.0;
        this.max = 0.0;
        this.nombreAnomalie = 0L;
        this.lastUpdate = LocalDateTime.now();
        this.dateNaissance = dateNaissance;
        this.weight = weight;
    }





    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public Double getMoyenne() {
        return moyenne;
    }

    public void setMoyenne(Double moyenne) {
        this.moyenne = moyenne;
    }

    public Double getMin() {
        return min;
    }

    public void setMin(Double min) {
        this.min = min;
    }

    public Double getMax() {
        return max;
    }

    public void setMax(Double max) {
        this.max = max;
    }

    public Long getNombreAnomalie() {
        return nombreAnomalie;
    }

    public void setNombreAnomalie(Long nombreAnomalie) {
        this.nombreAnomalie = nombreAnomalie;
    }

    public LocalDateTime  getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

}
