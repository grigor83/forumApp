import { NgIf } from '@angular/common';
import { Component } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { filter } from 'rxjs/operators';
import { UserService } from '../services/user.service';

@Component({
  selector: 'app-sidenav',
  standalone: true,
  imports: [RouterOutlet, RouterLink, RouterLinkActive, NgIf],
  templateUrl: './sidenav.component.html',
  styleUrl: './sidenav.component.css'
})
export class SidenavComponent {
  
  currentRoute!: string;
  showRoom: boolean = false;
  showRooms: boolean = false;
  showAdmin: boolean = false;
  showLogout: boolean = false;

  constructor(private router: Router, private userService : UserService) {}

  ngOnInit(): void {
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      this.currentRoute = this.router.url;
      this.updateLinkVisibility();
    });
  }

  updateLinkVisibility() {
    if (this.currentRoute === '/login' || this.currentRoute === '/login2' || this.currentRoute === '/register') {
      this.showRoom = false;
      this.showRooms = false;
      this.showAdmin = false;
      this.showLogout = false;
    }
    else if (this.userService.activeUser?.role === 'user') {
      this.showRoom = true;
      this.showRooms = false;
      this.showAdmin = false;
      this.showLogout = true;
    }
    else if (this.userService.activeUser?.role === 'moder') {
      this.showRoom = false;
      this.showRooms = true;
      this.showAdmin = false;
      this.showLogout = true;
    }
    else {
      this.showRoom = false;
      this.showRooms = true;
      this.showAdmin = true;
      this.showLogout = true;
    }
  }

  logout() {
    this.userService.logout();
  }

}
