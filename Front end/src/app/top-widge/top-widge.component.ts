import {Component, OnInit} from '@angular/core';
import {
  faHeartPulse, faWeightScale, faHeartBroken, faCalendarDay, faHeartCirclePlus, faHeartCircleMinus,
} from '@fortawesome/free-solid-svg-icons';
import {FormBuilder} from "@angular/forms";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";
@Component({
  selector: 'app-top-widge',
  templateUrl: './top-widge.component.html',
  styleUrl: './top-widge.component.css'
})
export class TopWidgeComponent implements OnInit {

  heart = faHeartPulse;
  wieght = faWeightScale;
  heartch = faHeartBroken;
  age = faCalendarDay;
  heart_max = faHeartCirclePlus;
  heart_min = faHeartCircleMinus;


  // Définir le modèle directement dans le composant
  statistiques: {
    sensorId: string;
    moyenne: number;
    min: number;
    max: number;
    nombreAnomalie: number;
    lastUpdate: string;
    dateNaissance: string;
    weight: number;
  } | null = null; // Initialisation à null

  errorMessage: string = ''; // Message d'erreur


  constructor(private fb: FormBuilder,private http: HttpClient, private router: Router) {

  }
  ngOnInit(): void {
    const sensorId = localStorage.getItem('idSenser'); // Récupérer l'idSenser depuis le localStorage
    if (sensorId) {
      this.fetchStatistiques(sensorId);
    } else {
      this.errorMessage = 'Aucun idSenser trouvé dans le stockage local.';
    }
  }

  fetchStatistiques(sensorId: string): void {
    const apiUrl = `http://localhost:8085/iot/statistique/${sensorId}`;
    this.http.get<any>(apiUrl).subscribe({
      next: (data) => {
        this.statistiques = data; // Stocker l'objet des statistiques
      },
      error: (error) => {
        console.error('Erreur lors de la récupération des statistiques:', error);
        if (error.status === 404) {
          this.errorMessage = 'Capteur introuvable.';
        } else {
          this.errorMessage = 'Une erreur est survenue lors de la récupération des statistiques.';
        }
      }
    });
  }



  // Méthode pour calculer l'âge
  calculateAge(dateNaissance: string): number {
    const birthDate = new Date(dateNaissance);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDifference = today.getMonth() - birthDate.getMonth();

    // Ajuster l'âge si l'anniversaire n'est pas encore passé cette année
    if (monthDifference < 0 || (monthDifference === 0 && today.getDate() < birthDate.getDate())) {
      age--;
    }
    return age;
  }

}
