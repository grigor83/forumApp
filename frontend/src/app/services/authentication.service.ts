import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private url = 'http://localhost:8080/auth';

  constructor(private http: HttpClient) { }

  registerUser(username: string, password: string, email: string, role: string) {
    return this.http.post<any>(this.url + '/register', {
      username : username,
      password : password,
      email : email,
      role : role
    });
  }

  loginUser(username: string, password: string){
    return this.http.post<number>(this.url + '/login', {
      username : username,
      password : password,
    });
  }

  loginWithCode(userId: number, code: number){
    return this.http.post<any>(this.url + '/login-with-code', {
      id : userId,
      code : code,
    });
  }

}
