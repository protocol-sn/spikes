import { Component } from '@angular/core';
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-navigation',
  standalone: true,
  template: `
    <div style="flex-direction: row; width: 100%; row-gap: 5px;align-content: center">
      <a [routerLink]="['/home']">Home</a>&nbsp;
      <a [routerLink]="['/secured']">Secured links</a>&nbsp;
      <a [routerLink]="['/unsecured']">Unsecured links</a>
    </div>`,
  imports: [
    RouterLink
  ],
  styleUrl: './navigation.component.css'
})
export class NavigationComponent {

}
