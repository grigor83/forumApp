import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { AuthenticationService } from '../services/authentication.service';
import { User } from '../models/user';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [HeaderComponent, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  username!: string;
  password!: string;

  constructor (private router : Router, private userService : UserService, private authService : AuthenticationService) { }

  login(){
    this.userService.logout();

    this.authService.loginUser(this.username, this.password).subscribe(
      response => {
        const newUser = new User(this.username, this.password, null, null);
        newUser.id = response;
        this.userService.activeUser = newUser;
        this.userService.signedIn = false;
        alert("Unesite verifikacioni kod koji ste dobili elektronskom poštom!");
        this.router.navigate(['/login2']); 
      },
      error => {
        //alert("Uneseni kredencijali nisu validni ili je problem sa vašim nalogom!");
      }
    );
  }

}
