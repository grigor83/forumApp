import { Injectable } from '@angular/core';
import { UserService } from './user.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class GuardLogin2Service {

  constructor(private userService : UserService, private router : Router) { }

  canActivate() : boolean {
    if (this.userService.activeUser != null && !this.userService.signedIn && this.userService.activeUser.verified)
      return true;
    else {
      this.router.navigate(['']);
      return false;
    }
  }
  
}
