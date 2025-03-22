import {Component, inject, signal} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {FormControl, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {MyRecord} from './my-record';
import {rxResource} from '@angular/core/rxjs-interop';

@Component({
  selector: 'app-root',
  imports: [ReactiveFormsModule],
  template: `
    <div style="display: flex; flex-direction: row; height: 40%">
      <div style="width: 40%">
        @for (record of recordListQuery.value(); track record) {
          <div>
            <input type="button" value="{{record.title}}" (click)="showRecord(record.id)"/>
          </div>
        } @empty {
            no records
        }
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
        <input type="button" value="New record" (click)="saveNewRecord()"/>
      </form>
    </div>
  `,
  standalone: true,
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'resource-cache';
  private readonly http: HttpClient = inject(HttpClient);
  protected gottenRecord: MyRecord | null = null;

  query = signal<string>("");
  recordListQuery = rxResource<MyRecord[], string>({
    request: this.query,
    loader: () => this.getRecords()
  });

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

  getRecords() {
    console.log('getRecords');

    return this.http.get<MyRecord[]>('http://localhost:3000/records');
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
        this.recordListQuery.reload();
      });
  }

  saveNewRecord() {
    this.http.post<MyRecord>('http://localhost:3000/records', {
      title: this.newRecordForm.get('title')?.value,
      content: this.newRecordForm.get('content')?.value,
    }).subscribe(value => {
      this.gottenRecord = value;
      this.recordListQuery.reload();
    });
  }
}
