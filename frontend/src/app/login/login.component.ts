import { Component } from '@angular/core';
import { HeaderComponent } from '../header/header.component';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';

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

  constructor (private router : Router, private userService : UserService) { }

  login() {
    this.userService.getUsers().subscribe(response => {
      let users = response.filter(user => user.username === this.username && user.password === this.password);
      if (users.length === 0) {
        alert("Neispravno korisničko ime ili lozinka!");
        this.userService.activeUser = null;
        this.userService.signedIn = false;
        return;
      }  

      if (users[0].banned){
        alert("Zabranjen vam je pristup forumu!");
        this.userService.activeUser = null;
        this.userService.signedIn = false;
        return;
      }

      if (!users[0].verified){
        alert("Administrator još nije odobrio vaš nalog!");
        this.userService.activeUser = null;
        this.userService.signedIn = false;
      }
      else {
        this.userService.activeUser = users[0];
        this.userService.signedIn = false;
        this.userService.sendVerificationCode(this.userService.activeUser).subscribe(response => {
          alert("Unesite verifikacioni kod koji ste dobili elektronskom poštom!");
          this.router.navigate(['/login2']); 
        });
      }
    });
  }

}
