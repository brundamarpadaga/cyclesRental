import { Component } from '@angular/core';
import { AuthService } from '../Authserv';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  error: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.authService.login(this.username, this.password).subscribe(
      () => {
        this.router.navigate(['/home']);
      },
      (error) => {
        this.error = 'Invalid Credentials';
      }
    );
  }
}
