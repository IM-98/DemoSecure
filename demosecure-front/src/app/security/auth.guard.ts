import {CanActivateFn, Router} from '@angular/router';
import {TokenService} from "../services/token.service";
import {inject} from "@angular/core";
import {JwtDecodeService} from "./jwt-decode.service";

export const authGuard: CanActivateFn = (route, state) => {
  const authService: TokenService = inject(TokenService);
  const router: Router = inject(Router);


  if(!authService.isLogged) {
    console.log("authGuard : redirection vers /login");
    router.navigate(['/login'])
  }
  return authService.isLogged;
};
