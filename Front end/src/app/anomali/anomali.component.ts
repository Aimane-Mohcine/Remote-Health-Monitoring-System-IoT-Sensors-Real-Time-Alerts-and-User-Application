import {Component, OnInit} from '@angular/core';
import {faCircleDot, faWeightScale} from "@fortawesome/free-solid-svg-icons";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-anomali',
  templateUrl: './anomali.component.html',
  styleUrl: './anomali.component.css'
})
export class AnomaliComponent implements OnInit {
  dot = faCircleDot;

  // Transactions pour stocker les anomalies récupérées
  transactions: any[] = [];

  errorMessage: string = ''; // Message d'erreur en cas de problème

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const sensorId = localStorage.getItem('idSenser'); // Récupérer l'idSenser depuis le stockage local
    if (sensorId) {
      this.fetchLatestAnomalies(sensorId);
    } else {
      this.errorMessage = 'Aucun sensorId trouvé dans le stockage local.';
    }
  }

  // Méthode pour récupérer les anomalies depuis le backend
  fetchLatestAnomalies(sensorId: string): void {
    const apiUrl = `http://localhost:8085/iot/latest/${sensorId}`;
    this.http.get<any[]>(apiUrl).subscribe({
      next: (data) => {
        console.log('Anomalies récupérées :', data);
        this.transactions = data; // Stocker les anomalies récupérées
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des anomalies :', error);
        this.errorMessage = 'Une erreur est survenue lors de la récupération des anomalies.';
        if (error.status === 404) {
          this.errorMessage = 'Aucune anomalie trouvée pour ce capteur.';
        }
      }
    });
  }

  protected readonly wieght = faWeightScale;
}
