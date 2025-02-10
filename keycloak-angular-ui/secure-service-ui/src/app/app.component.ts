import { Component } from '@angular/core';

@Component({
    selector: 'app-root',
    template: '<router-outlet></router-outlet>',
    styleUrls: ['./app.component.css'],
    standalone: false
})
export class AppComponent {
  title = 'secure-service-ui';
}


// export const authCodeFlowConfig: AuthConfig = {
//   // Url of the Identity Provider
//   issuer: 'http://localhost:9080/realms/spike-realm',
//
//   // URL of the SPA to redirect the user to after login
//   redirectUri: window.location.origin + '/index.html',
//
//   // The SPA's id. The SPA is registerd with this id at the auth-server
//   // clientId: 'server.code',
//   clientId: 'angular-ui',
//
//   // Just needed if your auth server demands a secret. In general, this
//   // is a sign that the auth server is not configured with SPAs in mind
//   // and it might not enforce further best practices vital for security
//   // such applications.
//   // dummyClientSecret: 'secret',
//
//   responseType: 'code',
//
//   // set the scope for the permissions the client should request
//   // The first four are defined by OIDC.
//   // Important: Request offline_access to get a refresh token
//   // The api scope is a usecase specific one
//   scope: 'openid profile email offline_access api',
//
//   showDebugInformation: true,
// };
