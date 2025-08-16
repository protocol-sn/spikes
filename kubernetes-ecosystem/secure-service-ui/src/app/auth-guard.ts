import {ActivatedRouteSnapshot, CanActivate, RouterStateSnapshot} from "@angular/router";
import {AuthService} from "./auth.service";
import {inject, Injectable} from "@angular/core";

@Injectable()
export class AuthGuard implements CanActivate {
  private authService: AuthService = inject(AuthService);

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot) {

    // var hasIdToken = this.authService.isAuthenticated();
    // var hasAccessToken = this.authService.hasValidAccessToken();
    console.log("Are we authenticated? " + this.authService.isAuthenticated());
    return this.authService.isAuthenticated();//(hasIdToken && hasAccessToken);
  }
}
