import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../header/header.component";
import { SidenavComponent } from "../sidenav/sidenav.component";
import { NgFor, NgIf } from '@angular/common';
import { User } from '../models/user';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { Room } from '../models/room';
import { RoomService } from '../services/room.service';
import { Permission } from '../models/permission';
import { response } from 'express';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [HeaderComponent, SidenavComponent, NgIf, NgFor, FormsModule],
  templateUrl: './admin.component.html',
  styleUrl: './admin.component.css'
})

export class AdminComponent implements OnInit {

  selectedTab = 0;
  users : User[] = [];
  newUsers : User[] = [];
  displayUserModal : boolean = false;
  isAdmin : boolean = false;
  isRegular : boolean = false;
  selectedUser!: User;
  username!: string | null;
  password!: string | null;
  email!: string | null;
  banned!: boolean;
  role!: string | null;
  scienceRoom!: Room;
  cultureRoom!: Room;
  sportRoom!: Room;
  musicRoom!: Room;
  permissionValues = ['post', 'edit', 'delete'];
  selectedScienceRoomPermissions: string[] = [];
  selectedCultureRoomPermissions: string[] = [];
  selectedSportRoomPermissions: string[] = [];
  selectedMusicRoomPermissions: string[] = [];

  constructor(private userService : UserService, private roomService : RoomService, private router : Router) {}

  ngOnInit(): void {
    this.userService.getUsers().subscribe(response => {
      this.users = response;
      this.newUsers = response.filter(user => !user.verified);
    });

    this.roomService.getRooms().subscribe(response => {
      response.forEach(room => {
        switch(room.name){
          case 'Nauka' : this.scienceRoom = room;
                          break;
          case 'Kultura' : this.cultureRoom = room;
                          break;
          case 'Sport' : this.sportRoom = room;
                          break;
          case 'Muzika' : this.musicRoom = room;
                          break;
        }
      })
    });

    this.selectedUser = new User(null, null, null, null);
  }

  selectTab(index: number) {
    this.selectedTab = index;
  }

  register(user: User) {
    user.verified = true;
    this.newUsers = this.newUsers.filter(u => u.id !== user.id);
    this.userService.sendVerificationEmail(user).subscribe(response => { });
  }

  showUserInfo(user: User) {
    this.selectedUser = user;
    this.username = user.username;
    this.password = user.password;
    this.email = user.email;
    this.banned = user.banned;
    this.role = user.role;

    this.isAdmin = user.role === 'admin'; 
    this.selectedUser.permissions.forEach(perm => {
      if (perm.room?.id === this.scienceRoom.id){
        this.displayRoomPermissions(perm, this.selectedScienceRoomPermissions, this.scienceRoom.id);
      }
      else if (perm.room?.id === this.cultureRoom.id){
        this.displayRoomPermissions(perm, this.selectedCultureRoomPermissions, this.cultureRoom.id);
      }
      else if (perm.room?.id === this.sportRoom.id){
        this.displayRoomPermissions(perm, this.selectedSportRoomPermissions, this.sportRoom.id);
      }
      else{
        this.displayRoomPermissions(perm, this.selectedMusicRoomPermissions, this.musicRoom.id);
      }
    });

    if (this.selectedUser.role === 'regular'){
      this.isAdmin = false;
      this.isRegular = true;
    }
    this.displayUserModal = true;
  }

  updateUser(){
    this.selectedUser.role = this.role;
    this.selectedUser.username = this.username;
    this.selectedUser.password = this.password;
    this.selectedUser.email = this.email;
    this.selectedUser.banned = this.banned;
    this.changePermission(this.selectedScienceRoomPermissions, this.scienceRoom.id);
    this.changePermission(this.selectedCultureRoomPermissions, this.cultureRoom.id);
    this.changePermission(this.selectedSportRoomPermissions, this.sportRoom.id);
    this.changePermission(this.selectedMusicRoomPermissions, this.musicRoom.id);
    
    this.userService.updateUser(this.selectedUser).subscribe(response => {
      this.closeUserModal();
    });
  }

  closeUserModal() {
    this.selectedScienceRoomPermissions = [];
    this.selectedCultureRoomPermissions = [];
    this.selectedMusicRoomPermissions = [];
    this.selectedSportRoomPermissions = [];
    this.selectedUser = new User(null,null,null,null);
    this.isAdmin = false;
    this.isRegular = false;
    this.username = null;
    this.password = null;
    this.email = null;
    this.role = null;
    this.displayUserModal = false;
  }

  onCheckboxChange(values: string[], option: string, event: Event) {
    const checkbox = event.target as HTMLInputElement;
    const isChecked = checkbox.checked;

    if (isChecked) {
      values.push(option);
    } 
    else {
      const index = values.indexOf(option);
      if (index > -1)
        values.splice(index, 1);
    }
  }

  onSelectionChange(event: any) {
    if (this.role === 'admin'){
      this.isAdmin = true;
      this.isRegular = false;
      this.checkedAll();
    }
    else if (this.role === 'moder'){
      this.isAdmin = false;
      this.isRegular = false;
      this.checkedAll();
    }
    else {
      this.isAdmin = false;
      this.isRegular = true;
      this.checkOnlyPost();
    }
  }

  checkedAll(){
    this.selectedCultureRoomPermissions = ['post', 'edit', 'delete'];
    this.selectedMusicRoomPermissions = ['post', 'edit', 'delete'];
    this.selectedScienceRoomPermissions = ['post', 'edit', 'delete'];
    this.selectedSportRoomPermissions = ['post', 'edit', 'delete'];
  }

  checkOnlyPost(){
    this.selectedCultureRoomPermissions = ['post'];
    this.selectedMusicRoomPermissions = ['post'];
    this.selectedScienceRoomPermissions = ['post'];
    this.selectedSportRoomPermissions = ['post'];
  }

  changePermission(permissions: string[], roomId: number){
    let userPermission = this.selectedUser.permissions
                .find(perm => perm.room?.id === roomId);

    if (userPermission){
      if (permissions.length === 0){
          userPermission.post = userPermission.edit = userPermission.delete = false; 
      }
      else {
        if (permissions.includes('post'))
          userPermission.post = true;
        else
          userPermission.post = false;
        if (permissions.includes('edit'))
          userPermission.edit = true;
        else
          userPermission.edit = false;
        if (permissions.includes('delete'))
          userPermission.delete = true;
        else
          userPermission.delete = false;
      }
    }
  }

  displayRoomPermissions(permission: Permission, permissions: string[], roomId: number){
    if (permission.post)
      permissions.push('post');
    if (permission.edit)
      permissions.push('edit');
    if (permission.delete)
      permissions.push('delete');
  }

}
