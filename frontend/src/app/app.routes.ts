import { Routes } from '@angular/router';
import { RoomsComponent } from './rooms/rooms.component';
import { AdminComponent } from './admin/admin.component';
import { LoginComponent } from './login/login.component';
import { RegisterComponent } from './register/register.component';
import { Login2Component } from './login2/login2.component';
import { RoomComponent } from './room/room.component';
import { GuardRegularService } from './services/guard-regular.service';
import { GuardModerService } from './services/guard-moder.service';
import { GuardAdminService } from './services/guard-admin.service';
import { GuardLogin2Service } from './services/guard-login2.service';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'login2', component: Login2Component, canActivate : [GuardLogin2Service] },
    { path: 'register', component: RegisterComponent },
    { path: 'room', component: RoomComponent, canActivate : [GuardRegularService] },
    { path: 'rooms', component: RoomsComponent, canActivate : [GuardModerService] },
    { path: 'admin', component: AdminComponent, canActivate : [GuardAdminService] },
    { path: '', redirectTo: '/login', pathMatch: 'full' },
];
