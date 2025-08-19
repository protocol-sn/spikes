import {Component, inject, OnInit, SkipSelf} from '@angular/core';
import {NavigationComponent} from "../navigation/navigation.component";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {AuthService} from "../auth.service";
import {ResponseComponent} from "../response/response.component";
import {catchError, map, of} from "rxjs";
import {ApiService} from "../api.service";
import {environment} from "../../environments/environment";

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

  doSpike2RoleCall() {
    console.log("Doing spike2 role call...")
    this.apiService.doSecureGET(environment.backendOne + "/secured/spike")
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

  doNoRoleCall() {
    console.log("Doing no role call...")
    this.apiService.doSecureGET(environment.backendOne + "/secured/")
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
