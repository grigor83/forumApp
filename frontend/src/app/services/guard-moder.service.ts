import { Injectable } from '@angular/core';
import { UserService } from './user.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class GuardModerService {

  constructor(private userService : UserService, private router : Router) { }

  canActivate() : boolean {
    if (this.userService.signedIn && this.userService.activeUser?.verified &&
        (this.userService.activeUser?.role === 'moder' || this.userService.activeUser?.role === 'admin'))
      return true;
    else {
      this.router.navigate(['']);
      return false;
    }
  }
}
