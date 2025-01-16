package org.example.serveur.Model;

import java.util.List;



public class SensorData {
    private String bn; // Base Name (identifiant du capteur)
    private long bt;   // Base Time (timestamp)
    private String n;  // Name of the measurement
    private String u;  // Unit
    private double v;  // Value


    // Getters et setters
    public String getBn() {
        return bn;
    }

    public void setBn(String bn) {
        this.bn = bn;
    }

    public long getBt() {
        return bt;
    }

    public void setBt(long bt) {
        this.bt = bt;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getU() {
        return u;
    }

    public void setU(String u) {
        this.u = u;
    }

    public double getV() {
        return v;
    }

    public void setV(double v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "bn='" + bn + '\'' +
                ", bt=" + bt +
                ", n='" + n + '\'' +
                ", u='" + u + '\'' +
                ", v=" + v +
                '}';
    }

}
