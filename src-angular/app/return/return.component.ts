import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-return',
  templateUrl: './return.component.html',
  styleUrls: ['./return.component.css']
})
export class ReturnComponent {

  newdata: any;
  constructor(private http: HttpClient) { }
  getallcycle() {
    return this.http.get('http://localhost:8080/api/cycles/list-data');
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
  returnCycle(id: number, quantityToBorrow: number) {
    const requestBody = { id, count: quantityToBorrow };
    const url = `http://localhost:8080/api/cycles/${id}/return`;
    this.http.post(url, requestBody, { responseType: 'text' }).subscribe(response => {
      this.ngOnInit();
      console.log(`Cycle with ID ${id} rented successfully.`);
    });
  }
}


