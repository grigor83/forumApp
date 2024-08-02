import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../services/user.service';

export const moderatorGuard: CanActivateFn = (route, state) => {
  const userService = inject(UserService);
  const router = inject(Router);
  
  if (localStorage !== undefined){
    const token = localStorage.getItem('token');
    if (!token){
      router.navigate(['']);
      return false;
    }
  }

  if (userService.signedIn && userService.activeUser?.verified &&
            (userService.activeUser?.role === 'moder' || userService.activeUser?.role === 'admin'))
      return true;
  else {
      router.navigate(['']);
      return false;
  }

};
