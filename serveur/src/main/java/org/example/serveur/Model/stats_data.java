package org.example.serveur.Model;

public class stats_data {
     String bn; // Base Name (identifiant du capteur)
     long bt;   // Base Time (timestamp)
    Double average_heart_rate;
    Double min_heart_rate;
    Double max_heart_rate;


    // Getter et Setter pour 'bn'
    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    // Getter et Setter pour 'bt'
    public long getBt() {
        return bt;
    }

    public void setBt(long bt) {
        this.bt = bt;
    }

    // Getter et Setter pour 'average_heart_rate'
    public Double getAverageHeartRate() {
        return average_heart_rate;
    }

    public void setAverageHeartRate(Double average_heart_rate) {
        this.average_heart_rate = average_heart_rate;
    }

    // Getter et Setter pour 'min_heart_rate'
    public Double getMinHeartRate() {
        return min_heart_rate;
    }

    public void setMinHeartRate(Double min_heart_rate) {
        this.min_heart_rate = min_heart_rate;
    }

    // Getter et Setter pour 'max_heart_rate'
    public Double getMaxHeartRate() {
        return max_heart_rate;
    }

    public void setMaxHeartRate(Double max_heart_rate) {
        this.max_heart_rate = max_heart_rate;
    }


    // Méthode toString
    @Override
    public String toString() {
        return "stats_data {" +
                "bn='" + bn + '\'' +
                ", bt=" + bt +
                ", average_heart_rate=" + average_heart_rate +
                ", min_heart_rate=" + min_heart_rate +
                ", max_heart_rate=" + max_heart_rate +
                '}';
    }
}
