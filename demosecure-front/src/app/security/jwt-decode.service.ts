import { Injectable } from '@angular/core';
import jwt_decode from "jwt-decode";
import {DecodedToken} from "../model/DecodedToken";

@Injectable({
  providedIn: 'root'
})
export class JwtDecodeService {

  constructor() { }
  DecodeToken(token: string): DecodedToken {
    return jwt_decode(token);
  }
  getRole(token: string): string {
    return this.DecodeToken(token).role[0];
  }
}
