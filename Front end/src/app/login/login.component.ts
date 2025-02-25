import { Component } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {faHeartCircleCheck, faHeartPulse, faUserLock} from "@fortawesome/free-solid-svg-icons";
import {HttpClient} from "@angular/common/http";
import {Router} from "@angular/router";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  user = faUserLock


  // login


  loginForm: FormGroup;
  loginFailed: boolean = false;

  constructor(private fb: FormBuilder,private http: HttpClient, private router: Router) {
    // Initialize the form with validators for email and password
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  // Getter to easily access form controls
  get formControls() {
    return this.loginForm.controls;
  }





  // Function to handle login form submission
  onSubmit(): void {
    if (this.loginForm.valid) {
      const email = this.loginForm.value.email;
      const password = this.loginForm.value.password;



      const credentials = {
        email: this.loginForm.value.email,
        password: this.loginForm.value.password
      };

      // Envoyer les données au backend
      this.http.post('http://localhost:8085/iot/login', credentials).subscribe({
        next: (response: any) => {
          console.log('Login Successful:', response);

          // Stocker les variables dans localStorage
          localStorage.setItem('idSenser', response.idSenser); // Stocker l'idSenser
          localStorage.setItem('jwtToken', response.token); // Stocker le token JWT

          this.loginFailed = false; // Réinitialiser l'état d'échec
          console.log('Token et ID enregistrés avec succès.');
          console.log("voila "+response.idSenser);
          this.router.navigate(['/Profile_home']).then(() => {
            this.reloadPage();
          });
        },
        error: (error) => {
          console.error('Login failed:', error);
          this.loginFailed = true; // Définir l'état d'échec
        }
      });
    } else {
      console.log('Form is invalid!');
    }
  }
  // forgot password


  email: string = '';
  verificationCode: string = '';
  newPassword: string = '';
  confirmPassword: string = '';

  emailModalOpen: boolean = false;
  codeModalOpen: boolean = false;
  newPasswordModalOpen: boolean = false;


// Open the email modal
  openEmailModal(): void {
    this.emailModalOpen = true;
  }

  // Close the modal by name
  closeModal(modal: string): void {
    if (modal === 'emailModal') {
      this.emailModalOpen = false;
    } else if (modal === 'codeModal') {
      this.codeModalOpen = false;
    } else if (modal === 'newPasswordModal') {
      this.newPasswordModalOpen = false;
    }
  }

  showErrorPopup: boolean = false;
  errorMessage: string = '';
  // Send verification code to the email
  sendCode(): void {
// Requête POST vers le backend pour envoyer le code
    this.http.post('http://localhost:8085/iot/forgot-password', this.email, { responseType: 'json' })
      .subscribe({
        next: (response: any) => {
          console.log('Code de réinitialisation envoyé avec succès:', response);

          // Fermer le modal actuel et ouvrir le suivant
          this.closeModal('emailModal');
          this.codeModalOpen = true;
        },
        error: (error: any) => {
          console.error('Erreur lors de l\'envoi du code:', error);

          // Gérer l'erreur HTTP et mettre à jour les variables
          if (error.status === 404) {
            this.errorMessage = error.error?.message || 'Utilisateur introuvable.';
          } else if (error.status === 500) {
            this.errorMessage = 'Une erreur interne est survenue. Réessayez plus tard.';
          } else {
            this.errorMessage = 'Une erreur inconnue est survenue.';
          }

          // Afficher la popup d'erreur
        //  this.emailModalOpen = false;

          this.showErrorPopup = true;

        }
      });
  }

  // Fermer la popup d'erreur
  closePopup2(): void {
    this.showErrorPopup = false;
  }
  // Verify the code entered by the user
  verifyCode(): void {
    const payload = {
      email: this.email, // L'email de l'utilisateur
      code: this.verificationCode // Le code saisi par l'utilisateur
    };

    // Requête POST vers le backend pour vérifier le code
    this.http.post('http://localhost:8085/iot/verify-reset-code', payload, { responseType: 'json' })
      .subscribe({
        next: (response: any) => {
          console.log('Code vérifié avec succès:', response);

          // Fermer le modal actuel et ouvrir le modal suivant
          this.closeModal('codeModal');
          this.newPasswordModalOpen = true;
        },
        error: (error: any) => {
          console.error('Erreur lors de la vérification du code:', error);

          // Gestion de l'erreur et affichage de la popup
          if (error.status === 400) {
            this.errorMessage =  "Code incorrect. Veuillez réessayer.";
          } else {
            this.errorMessage = "Une erreur inconnue est survenue. Veuillez réessayer plus tard.";
          }

          // Afficher la popup d'erreur
          this.showErrorPopup = true;
        }
      });
  }


  // Create a new password
  createNewPassword(): void {
    if (this.newPassword === this.confirmPassword && this.newPassword.length >= 6) {
      console.log(`Creating new password: ${this.newPassword}`);
      this.closeModal('newPasswordModal');



      const payload = {
        email: this.email, // L'email de l'utilisateur
        newPassword: this.newPassword // Le nouveau mot de passe
      };

      // Envoyer la requête au backend pour réinitialiser le mot de passe
      this.http.post('http://localhost:8085/iot/reset-password', payload, { responseType: 'json' })
        .subscribe({
          next: (response: any) => {
            console.log('Mot de passe réinitialisé avec succès:', response);

            // Fermer le modal actuel et afficher une popup de succès
            this.closeModal('newPasswordModal');
            this.showSuccessPopup = true;
          },
          error: (error: any) => {
            console.error('Erreur lors de la réinitialisation du mot de passe:', error);

            // Gestion des erreurs et affichage du message approprié
            if (error.status === 404) {
              this.errorMessage = error.error?.message || "Patient introuvable.";
            } else if (error.status === 500) {
              this.errorMessage = "Une erreur interne est survenue. Réessayez plus tard.";
            } else {
              this.errorMessage = "Une erreur inconnue est survenue.";
            }

            // Afficher la popup d'erreur
            this.showErrorPopup = true;
          }
        });
    } else {
      console.log('Password mismatch or not valid.');
    }
  }

  protected readonly heart = faHeartPulse;
  // alert pop up

  successIcon = faHeartCircleCheck;

  showSuccessPopup = false; // Initialize as false


  closePopup(): void {
    this.showSuccessPopup = false;
  }




  reloadPage() {
    window.location.reload();
  }
}
