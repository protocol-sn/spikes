import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-response',
  templateUrl: './response.component.html',
  standalone: true,
  styleUrl: './response.component.css'
})
export class ResponseComponent {
  @Input() code: string = '';
  @Input() body: string = '';

}
