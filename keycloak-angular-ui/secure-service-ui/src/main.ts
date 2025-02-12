import { platformBrowserDynamic } from '@angular/platform-browser-dynamic';

import { AppModule } from './app/app.module';
// import {HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi} from "@angular/common/http";
// import {AppComponent} from "./app/app.component";
// import {bootstrapApplication} from "@angular/platform-browser";
// import {DefaultOAuthInterceptor} from "angular-oauth2-oidc";

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err));

// bootstrapApplication(AppComponent, {
//   providers: [
//     {provide: HTTP_INTERCEPTORS, useClass: DefaultOAuthInterceptor, multi: true},
//     provideHttpClient(
//       withInterceptorsFromDi()
//     ),
//   ],
// });
