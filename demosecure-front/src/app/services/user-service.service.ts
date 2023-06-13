import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../environments/environment";
import {map, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserServiceService {

  constructor(protected http: HttpClient) { }

  private readonly BASE_URL = `${environment.url_api}/user/hello`;


  hello(): Observable<string> {
    return this.http.get(`${this.BASE_URL}`, { responseType: 'text' })
  }
}
