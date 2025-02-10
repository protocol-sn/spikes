import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {HomeComponent} from "./home/home.component";
import {SecuredPageComponent} from "./secured-page/secured-page.component";
import {UnsecuredPageComponent} from "./unsecured-page/unsecured-page.component";
// import {AuthGuard} from "./auth-guard";

const routes: Routes = [
  { path: '', pathMatch: 'full', redirectTo: 'home' },
  { path: 'home', component: HomeComponent },
  {
    path: 'secured',
    component: SecuredPageComponent,
    // canActivate: [AuthGuard]
  },
  {
    path: 'unsecured',
    component: UnsecuredPageComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
