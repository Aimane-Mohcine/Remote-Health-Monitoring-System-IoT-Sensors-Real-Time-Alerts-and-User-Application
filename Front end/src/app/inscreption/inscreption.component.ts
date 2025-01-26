import { Component } from '@angular/core';
import {faExclamationCircle, faHeartCircleCheck} from "@fortawesome/free-solid-svg-icons";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-inscreption',
  templateUrl: './inscreption.component.html',
  styleUrl: './inscreption.component.css'
})
export class InscreptionComponent {

  constructor(private router: Router, private http: HttpClient) {}

  user = {
    sensorId: '',
    nom: '',
    prenom: '',
    adresse: '',
    telephone: '',
    email: '',
    dateNaissance: '',
    weight:'',
    motDePasse: '',
  };

  // Set the maximum date as today (prevents future birthdates)
  maxDate = new Date().toISOString().split('T')[0];

  selectedImage: File | null = null;

  onSubmit(signupForm: any): void {
    if (signupForm.valid) {
      const formData = new FormData();

      // Append user data to FormData
      Object.entries(this.user).forEach(([key, value]) => {
        formData.append(key, value as string);
      });

      // Append the selected image to FormData
      if (this.selectedImage) {
        formData.append('image', this.selectedImage);
      }


      // Envoyer les données au backend
      this.http.post('http://localhost:8085/iot/register', formData)
        .subscribe({
          next: (response) => {
            console.log('Patient enregistré avec succès !', response);
            this.showSuccessPopup = true; // Afficher la popup de succès
          },
          error: (error) => {
            console.error('Erreur lors de l’enregistrement :', error);
            this.errorMessage = error.error?.error || 'Une erreur inconnue est survenue.'; // Extraire le message d'erreur
            this.showErrorPopup = true; // Afficher la popup d'erreur
          }
        });


    }


    }


  onImageSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      this.selectedImage = file;
    }
  }

  // Calculate age based on birthdate
  getAge(): number {
    if (this.user.dateNaissance) {
      const birthDate = new Date(this.user.dateNaissance);
      const today = new Date();
      let age = today.getFullYear() - birthDate.getFullYear();
      const month = today.getMonth() - birthDate.getMonth();
      if (month < 0 || (month === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }
      return age;
    }
    return 0;
  }



  successIcon = faHeartCircleCheck;

  showSuccessPopup = false; // Initialize as false


  closePopup(): void {
    this.showSuccessPopup = false;
    this.router.navigate(['/Login']);
  }


  showErrorPopup = false; // Nouvelle variable pour gérer la popup d'erreur
  errorMessage = ''; // Message d'erreur à afficher dans la popup
  errorIcon = faExclamationCircle; // Nouvelle icône pour les erreurs


  closePopup2(): void {
    this.showErrorPopup = false;

  }

  // Vérifier si une image est sélectionnée
  isImageSelected(): boolean {
    return this.selectedImage !== null; // Retourne true si une image est sélectionnée
  }

}
