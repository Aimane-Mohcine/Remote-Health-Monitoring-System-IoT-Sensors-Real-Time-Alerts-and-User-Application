package org.example.serveur.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
    private Integer nombreAnomalie;
    private LocalDateTime  lastUpdate;
    private Double age;
    private Double weight;


    public Statistiques() {
    }
    public Statistiques(String sensorId, Double age,Double weight){
        this.sensorId = sensorId;
        this.moyenne = 0.0;
        this.min = 0.0;
        this.max = 0.0;
        this.nombreAnomalie = 0;
        this.lastUpdate = LocalDateTime.now();
        this.age = age;
        this.weight = weight;
    }


    // Getters et Setters
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

    public Integer getNombreAnomalie() {
        return nombreAnomalie;
    }

    public void setNombreAnomalie(Integer nombreAnomalie) {
        this.nombreAnomalie = nombreAnomalie;
    }

    public LocalDateTime  getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Double getAge() {
        return age;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

}
