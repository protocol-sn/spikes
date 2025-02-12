import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {
  HTTP_INTERCEPTORS,
  provideHttpClient,
  withFetch,
  withInterceptorsFromDi
} from "@angular/common/http";
import {HomeComponent} from "./home/home.component";
import {DefaultOAuthInterceptor, OAuthModule, provideOAuthClient} from "angular-oauth2-oidc";
import {AuthGuard} from "./auth-guard";

@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    OAuthModule.forRoot({
      resourceServer: {
        allowedUrls: ['http://localhost:8082'],
        sendAccessToken: true
      },

    }),
    HomeComponent,
  ],
  providers: [
    {provide: HTTP_INTERCEPTORS, useClass: DefaultOAuthInterceptor, multi: true},
    provideHttpClient(withFetch(), withInterceptorsFromDi()),
    provideOAuthClient(),
    AuthGuard,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
