import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EMPTY, Observable, catchError, map } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class TokenService {

  private readonly BASE_URL = `${environment.url_api}/auth/user`;

  constructor(protected http: HttpClient) { }

  /**
   * Connexion a l'API
   * @param email : email
   * @param password : mot de passe
   * @returns
   */
  login(email: string, password: string): Observable<string> {
    return this.http.post<{accessToken: string}>(`${this.BASE_URL}/signin`, { usernameOrEmail: email, password})
      .pipe(map(tokenReponse => {
          return tokenReponse.accessToken;
      }),
      catchError((err, caught) => {
        console.error("login-Erreur :", err, caught);
        return EMPTY;
      }));
  }

}
