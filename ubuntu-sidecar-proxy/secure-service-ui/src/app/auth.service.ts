import {inject, Injectable} from '@angular/core';
import {AuthConfig, OAuthService} from "angular-oauth2-oidc";
import {Router} from "@angular/router";
import {HttpClient} from "@angular/common/http";
import {environment} from "../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private readonly oidcSecurityService = inject(OAuthService);
  private authenticated = false;
  private userData = null;

  authCodeFlowConfig: AuthConfig = {
    // Url of the Identity Provider
    //issuer: 'https://spike-reverse-proxy.fly.dev/auth/realms/spike-realm',
    issuer: environment.issuer,

    // URL of the SPA to redirect the user to after login
    redirectUri: window.location.origin,

    // The SPA's id. The SPA is registered with this id at the auth-server
    // clientId: 'server.code',
    clientId: 'angular-ui',

    // Just needed if your auth server demands a secret. In general, this
    // is a sign that the auth server is not configured with SPAs in mind
    // and it might not enforce further best practices vital for security
    // such applications.
    // dummyClientSecret: 'secret',

    responseType: 'code',

    // set the scope for the permissions the client should request
    // The first four are defined by OIDC.
    // Important: Request offline_access to get a refresh token
    // The api scope is a usecase specific one
    // scope: 'openid profile email offline_access api',
    scope: 'openid profile email roles',

    showDebugInformation: true,
    requireHttps: false,
  };
  private router: Router;
  private http: HttpClient = inject(HttpClient);

  constructor(router: Router) {
    this.oidcSecurityService.configure(this.authCodeFlowConfig);
    this.oidcSecurityService.loadDiscoveryDocumentAndTryLogin();
    this.router = router;
  }

  doConfigure(redirectUri: string) {
    this.oidcSecurityService.configure({...this.authCodeFlowConfig, redirectUri: redirectUri});
  }

  login(redirectRoute: string = '/') {
    console.log('doing login with redirect to ...');
    // this.oidcSecurityService.loadDiscoveryDocumentAndTryLogin().then(() => {
      this.oidcSecurityService.initLoginFlow();
      // this.oidcSecurityService.tryLogin({
      //   validationHandler: (context) => {
      //     var search = new URLSearchParams();
      //     search.set('token', context.idToken);
      //     search.set('client_id', oauthService.clientId);
      //     return this.http.get(validationUrl, { search }).toPromise();
      //   }
      // });
    // });
  }

  logout() {
    console.log('doing logout...');
    this.oidcSecurityService.logOut();
    // this.oidcSecurityService.revokeTokenAndLogout();
  }

  getUserData() {
    return this.oidcSecurityService.getIdentityClaims();
  }

  isAuthenticated() {
    return this.oidcSecurityService.hasValidIdToken() && this.oidcSecurityService.hasValidAccessToken();
  }

  getAccessToken() {
    return this.oidcSecurityService.getAccessToken();
  }
}
