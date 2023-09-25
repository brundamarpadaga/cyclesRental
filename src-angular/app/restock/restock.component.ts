import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';

@Component({
  selector: 'app-restock',
  templateUrl: './restock.component.html',
  styleUrls: ['./restock.component.css']
})
export class RestockComponent {
  cycleid: string = '';
  cyclecount: string = '';
  cyclestock: number = 0;
  cyclebrand: string = '';
  newdata: any;

  constructor(private _http: HttpClient) { } 
  submitForm() {
    const cycleData = {
      id: this.cycleid,
      count: this.cyclecount
    };
    const id = this.cycleid;
    const url = `http://localhost:8080/api/cycles/${id}/restock`;
    this._http.post(url, cycleData, { responseType: 'text' }).subscribe({
      next: (response) => {
        console.log('Cycle restocked successfully:', response);
      },
      error: (error) => {
        console.error('Error restocking cycle:', error);
      }
    });
  }

}
