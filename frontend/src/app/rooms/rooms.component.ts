import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../header/header.component";
import { SidenavComponent } from "../sidenav/sidenav.component";
import { DatePipe, NgFor, NgIf } from '@angular/common';
import { RoomService } from '../services/room.service';
import { Room } from '../models/room';
import { CommentComponent } from "../comment/comment.component";
import { Comment } from '../models/comment';
import { CommentService } from '../services/comment.service';
import { Permission } from '../models/permission';
import { UserService } from '../services/user.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-rooms',
  standalone: true,
  imports: [HeaderComponent, SidenavComponent, NgIf, NgFor, CommentComponent, FormsModule],
  providers: [DatePipe],
  templateUrl: './rooms.component.html',
  styleUrl: './rooms.component.css'
})

export class RoomsComponent implements OnInit {

  selectedTab = 0;
  scienceRoom!: Room;
  cultureRoom!: Room;
  sportRoom!: Room;
  musicRoom!: Room;
  permissions : Permission[] | undefined;
  displayPostModal : boolean = false;
  displayEditModal : boolean = false;
  content!: string | null;
  selectedComment: Comment = new Comment(null, null, null, null);
  userPermission!: Permission | null;

  constructor(private roomService : RoomService, private commentService : CommentService,
              private userService : UserService, private datePipe : DatePipe) {}

  ngOnInit(): void {
    this.roomService.getRooms().subscribe(response => {
      response.forEach(room => {
        switch(room.name){
          case 'Nauka' : this.scienceRoom = room;
                          break;
          case 'Kultura' : this.cultureRoom = room;
                          break;
          case 'Sport' : this.sportRoom = room;
                          break;
          case 'Muzika' : this.musicRoom = room;
                          break;
        }
      })
    });

    this.permissions = this.userService.activeUser?.permissions;
  }

  selectTab(index: number) {
    this.selectedTab = index;
  }

  postComment() {
    if (this.userService.activeUser?.role === 'admin'){
      this.displayPostModal = true;
      return;
    }

    let permission = this.getPermissionForRoom();
    if (!permission?.post){
      alert("Nemate dozvolu da objavite komentar!")
      return;
    }

    this.displayPostModal = true;
  }

  closePostModal() {
    let room!: Room;
    switch(this.selectedTab){
      case 0: room = this.scienceRoom;
              break;
      case 1: room = this.cultureRoom;
              break;
      case 2: room = this.sportRoom;
              break;
      case 3: room = this.musicRoom;
    }

    if (this.content != null){
      if (this.userService.activeUser !== null){
        const date = this.datePipe.transform(new Date(), 'dd.MM.yyyy. HH:mm');
        const comment = new Comment(date, this.content, this.userService.activeUser, room);
        this.content = null;
        this.commentService.postComment(comment).subscribe(response => {
          room.comments.push(response);
        });
      }
    }
    this.displayPostModal = false;
  }

  editComment(comment : Comment) {
    if (this.userService.activeUser?.role === 'admin'){
      this.selectedComment = comment;
      this.displayEditModal = true;
      return;
    }

    let permission = this.getPermissionForRoom();
    if (!permission?.edit && !permission?.delete){
      alert("Nemate dozvolu da editujete/obrišete komentar!")
      return;
    }

    this.selectedComment = comment;
    this.userPermission = permission;
    this.displayEditModal = true;
  }

  closeEditModal() {
    this.selectedComment = new Comment(null,null,null,null);
    this.userPermission = null;
    this.displayEditModal = false;
  }

  updateComment(){
    if (this.userService.activeUser?.role === 'admin' || 
                        this.userPermission?.edit){
      this.commentService.updateComment(this.selectedComment).subscribe(response => {
        this.closeEditModal();
      });
    }
    else {
      alert("Nemate dozvolu da editujete komentar!")
      this.closeEditModal();
      return;
    }    
  }

  deleteComment(){
    if (this.userService.activeUser?.role === 'admin' || 
          this.userPermission?.delete){
      this.commentService.deleteComment(this.selectedComment.id)
          .subscribe(response => {
              this.commentService.getCommentsByRoomId(this.selectedComment.room?.id)
                  .subscribe(response=> {
                    if (this.scienceRoom.id === this.selectedComment.room?.id)
                      this.scienceRoom.comments = response;
                    else if (this.cultureRoom.id === this.selectedComment.room?.id)
                      this.cultureRoom.comments = response;
                    else if (this.sportRoom.id === this.selectedComment.room?.id)
                      this.sportRoom.comments = response;
                    else
                      this.musicRoom.comments = response;
                    
                    this.closeEditModal();
                  });
    });
  }
  else {
    alert("Nemate dozvolu da obrišete komentar!")
    this.closeEditModal();
    return;
  }    
  }

  getPermissionForRoom(){
    let permission;

    if (this.selectedTab === 0){
      permission = this.permissions?.find(perm => perm.room?.id === this.scienceRoom.id);
    }
    else if (this.selectedTab === 1){
      permission = this.permissions?.find(perm => perm.room?.id === this.cultureRoom.id);
    }
    else if (this.selectedTab === 2){
      permission = this.permissions?.find(perm => perm.room?.id === this.sportRoom.id);
    }
    else {
      permission = this.permissions?.find(perm => perm.room?.id === this.musicRoom.id);
    }

    return permission;
  }

}
