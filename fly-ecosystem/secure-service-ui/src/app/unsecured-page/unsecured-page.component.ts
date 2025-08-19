import {Component, inject} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ResponseComponent} from "../response/response.component";
import {NavigationComponent} from "../navigation/navigation.component";

@Component({
  selector: 'app-unsecured-page',
  templateUrl: './unsecured-page.component.html',
  imports: [
    ResponseComponent,
    NavigationComponent
  ],
  standalone: true,
  styleUrl: './unsecured-page.component.css'
})
export class UnsecuredPageComponent {
  http: HttpClient = inject(HttpClient);
  unsecuredCode: string = '';
  unsecuredBody: string = '';
  requestBody: string = '';
  requestCode: string = '';

  doUnsecuredCall() {
    console.log("doing unsecured call...")
    this.http.get("https://spike-reverse-proxy.fly.dev", {responseType: 'text', observe: 'response'})
      .subscribe(response => {
        this.unsecuredBody = response.body ?? '';
        this.unsecuredCode = response.status.toString()
      })
  }

  request() {
    console.log("doing unsecured call...")
    this.http.get("https://spike-reverse-proxy.fly.dev/request", {responseType: 'text', observe: 'response'})
      .subscribe(response => {
        this.requestBody = response.body ?? '';
        this.requestCode = response.status.toString()
      })
  }

  backendTwo() {
    console.log("doing unsecured call...")
    this.http.get("https://spike-reverse-proxy.fly.dev/", {responseType: 'text', observe: 'response'})
      .subscribe(response => {
        this.requestBody = response.body ?? '';
        this.requestCode = response.status.toString()
      })
  }
}
