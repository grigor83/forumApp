import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-login2',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login2.component.html',
  styleUrl: './login2.component.css'
})
export class Login2Component {

  code!: number;

  constructor (private router : Router, private userService : UserService) {}

  login2() {
    if (this.userService.activeUser != null){
      this.userService.getUserById(this.userService.activeUser.id)
          .subscribe(response => {
            if (response.code == this.code){
              this.userService.signedIn = true;
              if (response.role === 'regular')
                this.router.navigate(['/room']); 
              else
                this.router.navigate(['/rooms']); 
            }
            else {
              this.userService.logout();
              this.router.navigate(['/login']); 
            }
          });
    }
  }

}
