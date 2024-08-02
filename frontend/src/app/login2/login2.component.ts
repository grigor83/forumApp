import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import { AuthenticationService } from '../services/authentication.service';
import { User } from '../models/user';

@Component({
  selector: 'app-login2',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login2.component.html',
  styleUrl: './login2.component.css'
})
export class Login2Component {

  code!: number;

  constructor (private router : Router, private userService : UserService, private authService : AuthenticationService) {}

  loginWithCode(){
    if (this.userService.activeUser != null){
      this.userService.activeUser.code = this.code;
      this.authService
        .loginWithCode(this.userService.activeUser.id, this.userService.activeUser.code).subscribe({
          next: (response) => {
            const newUser = new User(response.username, null, null, response.role);
            newUser.id = response.id;
            newUser.permissions = response.permissions;
            newUser.verified = response.verified;
            newUser.banned = response.banned;
            this.userService.activeUser = newUser;
            this.userService.signedIn = true;
            if (localStorage !== undefined)
              localStorage.setItem('token', response.token);
          
            if (response.role === 'user')
              this.router.navigate(['/room']); 
            else
              this.router.navigate(['/rooms']); 
          },
          error: (error) => {
            //alert('Verifikacioni kod nije validan!')
            this.userService.logout();
            this.router.navigate(['/login']); 
          }
        });
    }
  }

}
