import { Component, OnInit } from '@angular/core';
import { Cycles } from '../cycles';

import { HttpClient, HttpHeaders } from '@angular/common/http';

//import { CYCLES } from '../mock-cycles';
@Component({
  selector: 'app-cycles',
  templateUrl: './cycles.component.html',
  styleUrls: ['./cycles.component.css']
})
export class CyclesComponent {
  cycles : Cycles[] = [];
  rentedCycles: any[] = [];
  opencartlist=false;
  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.loadRentedCycles();
    this.http.get<Cycles[]>('http://localhost:8080/api/cycle/list')
      .subscribe(cycles => {
        this.cycles = cycles;
      });
  }
  
  loadRentedCycles() {
    // Make an HTTP request to get the rented cycles data.
    // Update this URL to match your API endpoint for rented cycles.
    this.http.get<any[]>('http://localhost:8080/api/cart').subscribe((cycles) => {
      this.rentedCycles = cycles;
    });
  }
  onRestock(id: number,value: string) {
    let numVal = 0;
    if(value!="")
      numVal = parseInt(value);
    const path = 'http://localhost:8080/api';
    const followpath = 'restock';
    const mainpath = `${path}/${id}/${followpath}?count=${numVal}`;

  
    this.http.post<Cycles[]>(mainpath,null).subscribe(cycles => {
      this.cycles = cycles;
    });
}
  onReturn(id:number,value:string){
    let numVal = 0;
    if(value!="")
      numVal = parseInt(value);
      const requestheaders = new HttpHeaders({
        'Content-Type' : 'application/json'
      });
      const path = 'http://localhost:8080/api';
      const followpath = 'return';
    const mainpath = `${path}/${id}/${followpath}?count=${numVal}`;
    this.http.post<Cycles[]>(mainpath,null,{
        responseType : 'json'
      }).subscribe(cycles => {
      this.cycles = cycles;

    });
  }
onBorrow(id: number,value : string) {
  let numVal = 0;
    if(value!="")
      numVal = parseInt(value);
      const requestheaders = new HttpHeaders({
        'Content-Type' : 'application/json'
      });
      const path = 'http://localhost:8080/api';
      const followpath = 'borrow';
    const mainpath = `${path}/${id}/${followpath}?count=${numVal}`;
    this.http.post<Cycles[]>(mainpath,null,{
        responseType : 'json'
      }).subscribe(cycles => {
      this.cycles = cycles;

    });
}
openCart(id: number,value : string ,noofday: string) {

  // const cartUrl = `http://localhost:8080/api/${id}/cart?quantity=${value}&days=${noofday}`;
  // window.open(cartUrl, '_blank');
  let quan = 0;
  let dayCount = 0;
  if(value!="")
    quan = parseInt(value);
    const requestheader = new HttpHeaders({
      'Content-Type' : 'application/json'
    });
  
  if(noofday!="")
    dayCount = parseInt(noofday);
    const requestheaders = new HttpHeaders({
      'Content-Type' : 'application/json'
    });

    const path = 'http://localhost:8080/api';
    const followpath = 'cart';
    const mainpath = `${path}/${id}/${followpath}?quantity=${quan}`;
  this.http.post<Cycles[]>(mainpath,null,{
      responseType : 'json'
    }).subscribe(cycles => {
    this.cycles = cycles;

  });
 this.opencartlist=!this.opencartlist;
}
}


