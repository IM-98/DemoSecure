import {CanActivateFn, Router} from '@angular/router';
import {JwtDecodeService} from "./jwt-decode.service";
import {inject} from "@angular/core";
import {StorageService} from "../services/storage.service";

export const adminGuard: CanActivateFn = (route, state) => {
  const jwtService: JwtDecodeService = inject(JwtDecodeService);
  const router: Router = inject(Router);
  const storageService: StorageService = inject(StorageService);

  if (storageService.getToken() !== undefined) {
    if (jwtService.getRole(storageService.getToken()!) !== 'ADMIN') {
      console.log("adminGuard : redirection vers /tabs1");
      router.navigate(['/tabs/tab1'])
      return false;
    }
    return true;
  }
return false;
};
