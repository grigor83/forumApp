import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { UserService } from '../services/user.service';

export const login2Guard: CanActivateFn = (route, state) => {

  const userService = inject(UserService);
  const router = inject(Router);
  
  if (userService.activeUser != null)
    return true;
  else {
    router.navigate(['']);
    return false;
  }
};
