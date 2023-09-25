import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AuthService } from '../Authserv';

@Component({
  selector: 'app-borrowed-cycles',
  templateUrl: './borrowed-cycles.component.html',
  styleUrls: ['./borrowed-cycles.component.css']
})
export class BorrowedCyclesComponent implements OnInit {
    newdata: any;
    constructor(private _http: HttpClient, private head: AuthService) { }
    getallcycle() {
      return this._http.get('http://localhost:8080/api/cycles/list');
    }
  
    ngOnInit() {
        this.getallcycle().subscribe({
        next: (res) => {
          this.newdata = res;
          console.log('Success: Response from API:', this.newdata);
        },
        error: (error) => {
          console.error('Error: Failed to fetch data from API:', error);
        }
      });
    }
  
    borrowCycle(id: number, quantityToBorrow: number) {
      const requestBody = { id, count: quantityToBorrow };
      const url = `http://localhost:8080/api/cycles/${id}/addtocart`;
      const headers = this.head.getHeaders();
      this._http.post(url, requestBody, { headers, responseType: 'text' }).subscribe(response => {
        this.ngOnInit();
        console.log(`Cycle with ID ${id} rented successfully.`);
      });
    }
  }