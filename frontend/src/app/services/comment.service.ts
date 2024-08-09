import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Comment } from '../models/comment';
import { Observable } from 'rxjs/internal/Observable';

@Injectable({
  providedIn: 'root'
})
export class CommentService {

  private url = 'https://localhost:8443/comments';

  constructor(private http : HttpClient) { }

  postComment(comment : Comment) {
    return this.http.post<Comment>(this.url, {
      id: comment.id,
      content: comment.content,
      username: comment.username,
      roomId: comment.roomId
    });
  }

  updateComment(comment: Comment): Observable<Comment> {
    return this.http.put<any>(`${this.url}`, {
      id: comment.id,
      content: comment.content,
      username: comment.username,
      roomId: 0
    });    
  }

  deleteComment(id : number): Observable<string> {
    return this.http.delete<string>(`${this.url}/${id}`);
  }

  getCommentsByRoomId(roomId: number | undefined): Observable<Comment[]> {
    return this.http.get<Comment[]>(`${this.url}/${roomId}`);
  }
}
