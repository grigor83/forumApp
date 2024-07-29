import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Comment } from '../models/comment';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private url = 'http://localhost:8080/comments';

  constructor(private http : HttpClient) { }

  getCommentsByRoomId(roomId: number | undefined): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.url}/${roomId}`);
  }

  postComment(comment : Comment) {
    return this.http.post<Comment>(this.url, comment);
  }

  updateComment(comment: Comment): Observable<Comment> {
    return this.http.put<Comment>(`${this.url}/${comment.id}`, comment);    
  }

  deleteComment(id : number): Observable<string> {
    return this.http.delete<string>(`${this.url}/${id}`);
  }
}
