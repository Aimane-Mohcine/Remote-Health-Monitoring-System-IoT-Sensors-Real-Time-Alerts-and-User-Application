package org.example.serveur.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data

public class Patient {
    @Id
    @JsonProperty("sensor_ID") // Nom utilisé dans le JSON
    String SENSOR_ID;
    String nom;
    String prenom;
    String adresse;
    String telephone;
    String email;
    Double age;
    Double weight;
    String motDePasse;


    // Getter et Setter pour SENSOR_ID
    public String getSENSOR_ID() {
        return SENSOR_ID;
    }

    public void setSENSOR_ID(String SENSOR_ID) {
        this.SENSOR_ID = SENSOR_ID;
    }

    // Getter et Setter pour nom
    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // Getter et Setter pour prenom
    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    // Getter et Setter pour adresse
    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    // Getter et Setter pour telephone
    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    // Getter et Setter pour email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter et Setter pour age
    public Double getAge() {
        return age;
    }

    public void setAge(Double age) {
        this.age = age;
    }

    // Getter et Setter pour weight
    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    // Getter et Setter pour motDePasse
    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

}
