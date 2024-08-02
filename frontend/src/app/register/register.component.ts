import { Component } from '@angular/core';
import { HeaderComponent } from "../header/header.component";
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [HeaderComponent, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})


export class RegisterComponent {

  username!: string;
  password!: string;
  email!: string;
  role! : string;

  constructor(private userService : UserService, private authService : AuthenticationService) {}

  register(){
    this.userService.logout();
    
    this.authService.registerUser(this.username, this.password, this.email, this.role).subscribe({
      next: response => {
            alert("Uspješno ste podnijeli zahtjev za registraciju! Sačekajte odobrenje administratora!");
      },
      error: error => {

      }
    });
  }

}
