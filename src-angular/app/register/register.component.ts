import { Component } from '@angular/core';
import { AuthService } from '../Authserv';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  username: string;
  password: string;
  confirmPassword: string;
  error: string;

  constructor(private authService: AuthService, private router: Router,private http:HttpClient) {}

  onSubmit() {
    const userData = {
      name: this.username,
      password: ''
    };

    const url = `http://localhost:8080/api/register`;
    if (this.password !== this.confirmPassword) {
      alert("Passwords do not match. Please try again.");
      return;
    }

    this.http.post(url, userData, { responseType: 'text' }).subscribe({
      next: (response) => {
        console.log('User registered successfully:', response);
      },
      error: (error) => {
        console.error('Error registering user:', error);
      }
    });
  }
}
