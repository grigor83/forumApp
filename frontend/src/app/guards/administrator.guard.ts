import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../services/user.service';

export const administratorGuard: CanActivateFn = (route, state) => {
  const userService = inject(UserService);
  const router = inject(Router);
  
  if (localStorage !== undefined){
    const token = localStorage.getItem('token');
    if (!token){
      router.navigate(['']);
      return false;
    }
  }

  if (userService.signedIn && userService.activeUser?.role === 'admin'
        && userService.activeUser.verified)
        return true;
  else {
      router.navigate(['']);
      return false;
  }
  
};
