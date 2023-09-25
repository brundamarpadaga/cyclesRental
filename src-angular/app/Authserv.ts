import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root',
})

export class AuthService {
    private apiUrl = 'http://localhost:8080/api/auth/token'; 
    constructor(private http: HttpClient) { }
    login(username: string, password: string): Observable<any> {
        const body = { username, password };
        return this.http.post(`${this.apiUrl}`, body);
    }

    setToken(token: string): void {
        localStorage.setItem('token', token);
    }
    getToken(): string | null {
        return localStorage.getItem('token');
    }
 
    getHeaders(): HttpHeaders {
        const token = this.getToken();
        return new HttpHeaders({
            'Content-Type': 'application/json',
            Authorization: `Bearer ${token}`,
        });
    }

}

 