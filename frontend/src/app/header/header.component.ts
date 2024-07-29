import { NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';
import { UserService } from '../services/user.service';
import { User } from '../models/user';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [NgIf],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  
  currentRoute!: string;
  showUserButton: boolean = false;
  showRegisterButton: boolean = false;
  showLoginButton: boolean = false;
  username: string | null = null;
  role: string | null = null;

  constructor(private router: Router, private userService : UserService) {}

  ngOnInit(): void {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.currentRoute = this.router.url;
      this.updateButtonVisibility();
    });
  }

  updateButtonVisibility() {
    if (this.currentRoute === '/room' || this.currentRoute === '/rooms' || this.currentRoute === '/admin') {
      this.showUserButton = true;
      this.showRegisterButton = false;
      this.showLoginButton = false;
      if (this.userService.activeUser != null){
        this.username = this.userService.activeUser.username;
        this.role = this.userService.activeUser.role;
      }
    }
    else if (this.currentRoute === '/register') {
      this.showUserButton = false;
      this.showRegisterButton = false;
      this.showLoginButton = true;
      this.username = null;
    }
    else {
      this.showUserButton = false;
      this.showRegisterButton = true;
      this.showLoginButton = false;
      this.username = null;
    }
  }

  register() {
    this.router.navigate(['/register']);
  }

  login() {
    this.router.navigate(['/login']);
  }

}
