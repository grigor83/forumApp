import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Room } from '../models/room';

@Injectable({
  providedIn: 'root'
})

export class RoomService {
  private url = 'http://localhost:8080/rooms';

  constructor(private http: HttpClient) { }

  getRooms(): Observable<Room[]> {
    return this.http.get<Room[]>(this.url);
  }

  getRoomById(id: number): Observable<Room> {
    return this.http.get<Room>(`${this.url}/${id}`);
  }
}
