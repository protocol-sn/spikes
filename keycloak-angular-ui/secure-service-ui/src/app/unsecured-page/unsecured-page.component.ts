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

  doUnsecuredCall() {
    console.log("doing unsecured call...")
    this.http.get("http://localhost:8082/unsecured", {responseType: 'text', observe: 'response'})
      .subscribe(response => {
        this.unsecuredBody = response.body ?? '';
        this.unsecuredCode = response.status.toString()
      })
  }
}
