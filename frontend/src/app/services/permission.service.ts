import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Permission } from '../models/permission';

@Injectable({
  providedIn: 'root'
})
export class PermissionService {

  private url = 'http://localhost:8080/permissions';

  constructor(private http : HttpClient) { }

  getPermissionsByUserId(userId: number): Observable<Permission[]> {
    return this.http.get<Permission[]>(`${this.url}/${userId}`);
  }

  postPermission(permission : Permission) {
    return this.http.post<Permission>(this.url, permission);
  }
}
