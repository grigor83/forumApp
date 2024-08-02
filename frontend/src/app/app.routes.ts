import { Routes } from '@angular/router';
import { RoomsComponent } from './rooms/rooms.component';
import { AdminComponent } from './admin/admin.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { Login2Component } from './login2/login2.component';
import { RoomComponent } from './room/room.component';
import { userGuard } from './guards/user.guard';
import { moderatorGuard } from './guards/moderator.guard';
import { administratorGuard } from './guards/administrator.guard';
import { login2Guard } from './guards/login2.guard';

export const routes: Routes = [
    { path: 'register', component: RegisterComponent },
    { path: 'login', component: LoginComponent },
    { path: 'login2', component: Login2Component, canActivate : [login2Guard] },
    { path: 'room', component: RoomComponent, canActivate : [userGuard] },
    { path: 'rooms', component: RoomsComponent, canActivate : [moderatorGuard] },
    { path: 'admin', component: AdminComponent, canActivate : [administratorGuard] },
    { path: '', redirectTo: '/login', pathMatch: 'full' },
];
