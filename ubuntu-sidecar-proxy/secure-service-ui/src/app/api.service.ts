import {inject, Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {AuthService} from "./auth.service";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private readonly http: HttpClient = inject(HttpClient);
  private readonly authService: AuthService = inject(AuthService);


  public doSecureGET(url: string) {
    console.log("Doing secure call with access token " + this.authService.getAccessToken());

    let headers = new HttpHeaders();
    headers = headers.set('Accept', '*/*');
    headers = headers.set('Authorization', 'Bearer ' + this.authService.getAccessToken())

    console.log("auth header: " + headers.get('Authorization'));

    return this.http
      .get(url, { headers: headers, responseType: 'text', observe: "response"} )
  }

  public doUnsecureGET(url: string) {
    console.log("Doing unsecure call...");

    let headers = new HttpHeaders();
    headers.set('Accept', 'text/json');

    return this.http
      .get(url, { headers: headers })
  }
}
