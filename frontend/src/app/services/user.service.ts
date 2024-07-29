import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { User } from '../models/user';

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

  postUser(user : User) {
    return this.http.post<User>(this.url, user);
  }

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(this.url + `/${id}`);
  }

  updateUser(user: User): Observable<User> {
    return this.http.put<User>(`${this.url}/${user.id}`, user);    
  }

  sendVerificationCode(user : User) {
    let emailUrl = 'http://localhost:8080/email';
    return this.http.post<User>(emailUrl, user);
  }

  sendVerificationEmail(user : User) {
    let emailUrl = 'http://localhost:8080/email';
    return this.http.put<User>(`${emailUrl}/${user.id}`, user);
  }

  logout(){
    this.activeUser = null;
    this.signedIn = false;
  }


}
