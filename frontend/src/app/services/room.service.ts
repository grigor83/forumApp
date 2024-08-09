import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Room } from '../models/room';

@Injectable({
  providedIn: 'root'
})

export class RoomService {
  private url = 'https://localhost:8443/rooms';

  constructor(private http: HttpClient) { }

  getRooms(): Observable<Room[]> {
    return this.http.get<Room[]>(this.url);
  }

}
