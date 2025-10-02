import {Component, inject, OnInit} from '@angular/core';
import {AuthService} from "../auth.service";
import {KeyValuePipe, NgFor} from "@angular/common";
import {NavigationComponent} from "../navigation/navigation.component";

@Component({
  selector: 'app-home',
  imports: [NgFor, KeyValuePipe, NavigationComponent],
  standalone: true,
  templateUrl: './home.component.html',
  styleUrl: './home.component.css'
})
export class HomeComponent implements OnInit {
  protected readonly authService: AuthService = inject(AuthService);

  ngOnInit(): void {
    console.log("attempting login...");
    // this.authService.login();
  }
}
