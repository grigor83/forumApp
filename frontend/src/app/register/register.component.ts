import { Component } from '@angular/core';
import { HeaderComponent } from "../header/header.component";
import { FormsModule } from '@angular/forms';
import { UserService } from '../services/user.service';
import { User } from '../models/user';
import { RoomService } from '../services/room.service';
import { Permission } from '../models/permission';
import { PermissionService } from '../services/permission.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [HeaderComponent, FormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})


export class RegisterComponent {

  username!: string;
  password!: string;
  email!: string;
  role! : string;

  constructor(private userService : UserService, private roomService : RoomService,
              private permService : PermissionService) {}

  register() {
    let users : User[];

    this.userService.getUsers().subscribe(response => {
      users = response.filter(user => user.username === this.username);
      if (users.length > 0)
        alert("Uneseno korisničko ime je zauzeto! Izaberite neko drugo!");
      else {
        const newUser = new User(this.username, this.password, this.email, this.role);
        
        this.userService.postUser(newUser).subscribe(user => {
          newUser.id = user.id;
          this.roomService.getRooms().subscribe(response => {
            const rooms = response;
            rooms.forEach(room => {
              if (this.role === 'moder' || this.role === 'admin') {
                let perm = new Permission(true, true, true, room, newUser);
                this.permService.postPermission(perm).subscribe(permission => {
                    user.permissions.push(permission);
                });
              }
              else {
                let perm = new Permission(true, false, false, room, newUser);
                this.permService.postPermission(perm).subscribe(permission => {
                    user.permissions.push(permission);
                });
              }
            });
          })
          alert("Uspješno ste podnijeli zahtjev za registraciju! Sačekajte odobrenje administratora!");
        });
      }
    });      
  }

}
