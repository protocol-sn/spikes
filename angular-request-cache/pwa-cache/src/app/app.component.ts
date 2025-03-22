import {Component, inject, OnDestroy, OnInit} from '@angular/core';
import {SwUpdate} from '@angular/service-worker';
import {HttpClient} from '@angular/common/http';
import {MyRecord} from './my-record';
import {AsyncPipe, NgForOf, NgIf} from '@angular/common';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {Observable, shareReplay} from 'rxjs';

@Component({
  selector: 'app-root',
  imports: [NgForOf, ReactiveFormsModule, AsyncPipe, NgIf],
  template: `
    <div style="display: flex; flex-direction: row; height: 40%">
      <div style="width: 40%">
        <div *ngIf="recordList">
          <div *ngFor="let record of recordList | async">
            <input type="button" value="{{record.title}}" (click)="showRecord(record.id)"/>
          </div>
        </div>
      </div>
      <div style="width: 40%">
        <div>
          <form [formGroup]="updateRecordForm">
            <input type="hidden" value="{{gottenRecord?.id}}" formControlName="id"/>
            title: <input type="text" value="{{gottenRecord?.title}}" formControlName="title"/>
            <br/>
            content: <input type="text" value="{{gottenRecord?.content}}" formControlName="content"/>
            <br/>
            <input type="button" value="update record" (click)="updateRecord()"/>
          </form>
        </div>
      </div>
    </div>
    <div>
      <form [formGroup]="newRecordForm">
        <input type="hidden" formControlName="id">
        title: <input type="text" formControlName="title">
        <br/>
        content: <input type="text" formControlName="content">
        <br/>
        <input type="button" value="update record" (click)="saveNewRecord()"/>
      </form>
    </div>
  `,
  standalone: true,
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit {
  private readonly http: HttpClient = inject(HttpClient);
  protected recordList: Observable<MyRecord[]> | null = null;
  protected gottenRecord: MyRecord | null = null;
  title = 'pwa-cache';
  updateRecordForm: FormGroup = new FormGroup({
    id: new FormControl(''),
    title: new FormControl(''),
    content: new FormControl(''),
  });
  newRecordForm: FormGroup = new FormGroup({
    id: new FormControl(''),
    title: new FormControl(''),
    content: new FormControl(''),
  });

  constructor(private readonly swUpdate: SwUpdate) { }

  getRecords() {
    console.log('getRecords');

    return this.http.get<MyRecord[]>('http://localhost:3000/records');
  }

  ngOnInit(): void {
    if (this.swUpdate.isEnabled) {
      this.swUpdate.checkForUpdate().then(() => {
        console.log('checkForUpdate');
        return this.swUpdate.activateUpdate()
      });
    }
    else {
      console.log('Service Worker is not enabled');
    }
    this.getRecords();
  }

  showRecord(id: number) {
    console.log("get record " + id);
    this.http.get<MyRecord>(`http://localhost:3000/records/${id}`)
      .subscribe(value => {
        this.updateRecordForm = new FormGroup({
          id: new FormControl(value.id),
          title: new FormControl(value.title),
          content: new FormControl(value.content),
        })
      });
  }

  updateRecord() {
    this.http.put<MyRecord>(`http://localhost:3000/records/${this.updateRecordForm.get('id')?.value}`, {
      id: this.updateRecordForm.get('id')?.value,
      title: this.updateRecordForm.get('title')?.value,
      content: this.updateRecordForm.get('content')?.value,
    })
      .subscribe(value => {
      this.gottenRecord = value;
      this.swUpdate.activateUpdate().then(r => r);
    });
  }

  saveNewRecord() {
    this.http.post<MyRecord>('http://localhost:3000/records', {
      title: this.newRecordForm.get('title')?.value,
      content: this.newRecordForm.get('content')?.value,
    }).subscribe(value => {
      this.gottenRecord = value;
    });
  }
}
