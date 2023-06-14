import { enableProdMode, importProvidersFrom } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { RouteReuseStrategy, provideRouter } from '@angular/router';
import { IonicModule, IonicRouteStrategy } from '@ionic/angular';
import {provideHttpClient, withInterceptors} from "@angular/common/http";

import { routes } from './app/app.routes';
import { AppComponent } from './app/app.component';
import { environment } from './environments/environment';
import {IonicStorageModule} from "@ionic/storage-angular";
import {tokenFnInterceptor} from "./app/security/token-fn.interceptor";
import {JwtDecodeService} from "./app/security/jwt-decode.service";

if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent, {
  providers: [
    { provide: RouteReuseStrategy, useClass: IonicRouteStrategy },
    importProvidersFrom(IonicModule.forRoot({}), IonicStorageModule.forRoot()),
    provideRouter(routes),
    provideHttpClient(withInterceptors([tokenFnInterceptor])),
    JwtDecodeService

  ],
});
