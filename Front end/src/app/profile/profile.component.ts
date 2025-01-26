import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {faHeartCircleCheck} from "@fortawesome/free-solid-svg-icons";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})

export class ProfileComponent implements OnInit {
  profile: any = null; // Objet pour stocker les données du profil
  errorMessage: string = ''; // Message d'erreur

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const sensorId = localStorage.getItem('idSenser'); // Récupérer l'idSenser depuis le stockage local
    if (sensorId) {
      this.fetchProfile(sensorId);
    } else {
      this.errorMessage = 'Aucun sensorId trouvé dans le stockage local.';
    }
  }

  // Récupérer les données du profil depuis le backend
  fetchProfile(sensorId: string): void {
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
  }


  confirmationPassword: string = ''; // Nouvelle variable pour la confirmation
  passwordMismatch: boolean = false; // Variable pour indiquer une erreur de correspondance

  // Méthode pour vérifier si les mots de passe correspondent
  checkPasswordMatch(): void {
    this.passwordMismatch = this.profile.motDePasse !== this.confirmationPassword;
  }

  successMessage: string = ''; // Message de succès

// Méthode appelée lors du clic sur "Save Profile"
  saveProfile(): void {

    console.log(this.profile)
    const sensorId = localStorage.getItem('idSenser'); // Récupérer l'idSenser depuis le stockage local
    const apiUrl = `http://localhost:8085/iot/update/${sensorId}`;
    this.http.patch<{ message: string }>(apiUrl, this.profile).subscribe({
      next: (response) => {
        console.log('Profil mis à jour avec succès :', response);
        this.successMessage = response.message; // Affichez le message de succès
        this.errorMessage = '';
        this.showSuccessPopup = true// Initialize as false      },
      },
      error: (error) => {
        console.error('Erreur lors de la mise à jour du profil :', error);
        this.errorMessage = 'Une erreur est survenue lors de la mise à jour.';
        this.successMessage = '';
        this.showErreurPopup = true// Initialize as false      },

      }
    });
  }

  protected readonly successIcon = faHeartCircleCheck;


  showSuccessPopup = false; // Initialize as false
  showErreurPopup = false; // Initialize as false


  closePopup(): void {
    this.showSuccessPopup = false;
    this.showErreurPopup = false;

  }

}
