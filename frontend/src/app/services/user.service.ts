import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { User } from '../models/user';
import { Permission } from '../models/permission';

@Injectable({
  providedIn: 'root'
})

export class UserService {
  
  private url = 'http://localhost:8080/users';
  public activeUser: User | null = null;
  public signedIn: boolean = false;

  constructor(private http: HttpClient) { }

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.url);
  }

  verifyUser(userId : number){
    return this.http.put<Permission[]>(this.url + '/verify' + `/${userId}`, null);
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.url}/${user.id}`, user);    
  }

  logout(){
    this.activeUser = null;
    this.signedIn = false;
    localStorage.removeItem('token');
  }

}
