import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Chart } from 'chart.js/auto';

@Component({
  selector: 'app-chart',
  templateUrl: './chart.component.html',
  styleUrls: ['./chart.component.css'], // Corrected the key
})
export class ChartComponent implements OnInit {
  private chart: any; // Instance du graphique
  private apiUrl = 'http://localhost:8085/data'; // URL de base de l'API
  sensorId = localStorage.getItem('idSenser'); // Récupérer l'idSenser depuis le stockage local

  constructor(private http: HttpClient) {}

  ngOnInit(): void {

    this.initChart();

    // Mise à jour du graphique toutes les secondes
    setInterval(() => {
      this.fetchSensorData();
    }, 1000);
  }

  // Initialisation du graphique
  initChart(): void {
    const ctx = document.getElementById('heartRateChart') as HTMLCanvasElement;
    this.chart = new Chart(ctx, {
      type: 'line',


      data: {
        labels: [], // Axe des X
        datasets: [
          {
            label: 'Fréquence cardiaque (BPM)',
            data: [], // Axe des Y
            borderColor: 'rgba(75, 192, 192, 1)',
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            tension: 0.1,
          },
        ],
      },
      options: {
        responsive: true,
        scales: {
          x: { title: { display: true, text: 'Temps' } },
          y: { title: { display: true, text: 'BPM' }, beginAtZero: true },
        },
      },
    });
  }

  // Récupérer les données depuis le backend
  fetchSensorData(): void {
    this.http.get<any>(`${this.apiUrl}/${this.sensorId}`).subscribe(
      (data) => {
        console.log('Données reçues :', data);
        this.updateChart(data.v); // Mise à jour du graphique
      },
      (error) => {
        console.error('Erreur lors de la récupération des données :', error);
      }
    );
  }

  // Mise à jour du graphique avec les nouvelles données
  updateChart(heartRate: number): void {
    const now = new Date();
    this.chart.data.labels.push(now.toLocaleTimeString()); // Ajouter l'heure actuelle
    this.chart.data.datasets[0].data.push(heartRate); // Ajouter la fréquence cardiaque

    // Limiter à 10 points
    if (this.chart.data.labels.length > 10) {
      this.chart.data.labels.shift();
      this.chart.data.datasets[0].data.shift();
    }

    this.chart.update(); // Mettre à jour le graphique
  }


}
