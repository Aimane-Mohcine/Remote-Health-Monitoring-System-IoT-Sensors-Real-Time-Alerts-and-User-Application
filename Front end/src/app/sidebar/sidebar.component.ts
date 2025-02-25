import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent implements OnInit {
  activeTab: string = 'Dashboard'; // Default active tab

  profile: any = null; // Objet pour stocker les données du profil
  errorMessage: string = ''; // Message d'erreur
  constructor(private router: Router, private route: ActivatedRoute,private http: HttpClient) {}

  ngOnInit(): void {
    const sensorId = localStorage.getItem('idSenser'); // Récupérer l'idSenser depuis le stockage local
    if (sensorId) {
      const apiUrl = `http://localhost:8085/iot/pation/${sensorId}`;
      this.http.get<any>(apiUrl).subscribe({
        next: (data) => {
          console.log('Données du profil récupérées :', data);
          this.profile = data; // Stocker les données dans le composant
        },
        error: (error) => {
          console.error('Erreur lors de la récupération des données du profil :', error);
          this.errorMessage = error.status === 404
            ? 'Profil non trouvé pour ce capteur.'
            : 'Une erreur est survenue lors de la récupération des données.';
        }
      });
    } else {
      this.errorMessage = 'Aucun sensorId trouvé dans le stockage local.';
    }
  }



  setActiveTab(tab: string): void {
    this.activeTab = tab;
  }

  logout() {
    localStorage.removeItem('idSenser');  // Clear user data from localStorage
    this.router.navigate(['/Login']).then(() => {
      this.reloadPage();
    });// Redirect to login page
  }

  reloadPage() {
    window.location.reload();
  }

}
