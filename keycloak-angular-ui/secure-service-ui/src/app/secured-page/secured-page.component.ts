import {Component, inject, OnInit, SkipSelf} from '@angular/core';
import {NavigationComponent} from "../navigation/navigation.component";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {AuthService} from "../auth.service";
import {ResponseComponent} from "../response/response.component";
import {catchError, map, of} from "rxjs";
import {ApiService} from "../api.service";

@Component({
  selector: 'app-secured-page',
  templateUrl: './secured-page.component.html',
  standalone: true,
  imports: [
    NavigationComponent,
    ResponseComponent
  ],
  styleUrl: './secured-page.component.css'
})
export class SecuredPageComponent implements OnInit {
  spikeBody: string = '';
  spikeCode: string = '';
  spike2Body: string = '';
  spike2Code: string = '';
  noRoleBody: string = '';
  noRoleCode: string = '';
  workaroundBody: string = '';
  workaroundCode: string = '';

  authService: AuthService = inject(AuthService);
  private readonly http: HttpClient;
  private readonly apiService: ApiService = inject(ApiService);

  constructor(@SkipSelf() http:HttpClient) {
    this.http = http;
  }

  doSpikeRoleCall() {
    console.log("Doing spike role call...")
    this.apiService.doSecureGET("http://localhost:8082/secured/spike", )
      .pipe(
        map(response => {
          return {body: response.body ?? '', code: response.status.toString()}
        }),
        catchError((errResponse: HttpErrorResponse) => of(
          {body: errResponse.error.message, code: errResponse.status.toString()}
        ))
      ).subscribe(response => {
      this.spikeBody = response.body ?? '';
      this.spikeCode = response.code
    });
  }

  doSpike2RoleCall() {
    console.log("Doing spike2 role call...")
    this.apiService.doSecureGET("http://localhost:8082/secured/spike2")
      .pipe(
        map(response => {
          return {body: response.body ?? '', code: response.status.toString()}
        }),
        catchError((errResponse: HttpErrorResponse) => of(
          {body: errResponse.error.message, code: errResponse.status.toString()}
        ))
      ).subscribe(response => {
        this.spike2Body = response.body ?? '';
        this.spike2Code = response.code
    });
  }

  doWorkaroundCall() {
    console.log("Doing workaround call...")
    this.apiService.doSecureGET("http://localhost:8081/secured/workaround")
      .pipe(
        map(response => {
          return {body: response.body ?? '', code: response.status.toString()}
        }),
        catchError((errResponse: HttpErrorResponse) => of(
          {body: errResponse.error.message, code: errResponse.status.toString()}
        ))
      ).subscribe(response => {
      this.workaroundBody = response.body ?? '';
      this.workaroundCode = response.code
    });
  }

  doNoRoleCall() {
    console.log("Doing no role call...")
    this.apiService.doSecureGET("http://localhost:8082/secured/noRole")
      .pipe(
        map(response => {
          return {body: response.body ?? '', code: response.status.toString()}
        }),
        catchError((errResponse: HttpErrorResponse) => of(
          {body: errResponse.error.message, code: errResponse.status.toString()}
        ))
      ).subscribe(response => {
      this.noRoleBody = response.body ?? '';
      this.noRoleCode = response.code
    });
  }

  ngOnInit(): void {
    if (!this.authService.isAuthenticated()) {
      console.log('not authenticated, redirecting to login...');
      this.authService.login('/secured');
    }
  }
}
