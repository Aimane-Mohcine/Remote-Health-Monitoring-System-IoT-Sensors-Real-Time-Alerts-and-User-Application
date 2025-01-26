import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class PatientService {
  private apiUrl = 'http://localhost:8085/iot'; // Lien vers le backend

  constructor(private http: HttpClient) {}

  registerPatientWithImage(user: any, image: File): Observable<any> {
    const formData = new FormData();

    // Ajouter chaque champ dans le FormData
    formData.append('sensorId', user.sensorId);
    formData.append('nom', user.nom);
    formData.append('prenom', user.prenom);
    formData.append('adresse', user.adresse);
    formData.append('telephone', user.telephone);
    formData.append('email', user.email);
    formData.append('dateNaissance', user.dateNaissance);
    formData.append('weight', user.weight);
    formData.append('motDePasse', user.motDePasse);

    // Ajouter l'image au FormData
    if (image) {
      formData.append('image', image);
    }

    return this.http.post('http://localhost:8080/api/patients/register', formData);
  }

}
