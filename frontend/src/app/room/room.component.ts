import { DatePipe, NgFor, NgIf } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { CommentComponent } from "../comment/comment.component";
import { Room } from '../models/room';
import { UserService } from '../services/user.service';
import { CommentService } from '../services/comment.service';
import { FormsModule } from '@angular/forms';
import { Comment } from '../models/comment';
import { Permission } from '../models/permission';
import { RoomService } from '../services/room.service';

@Component({
  selector: 'app-room',
  standalone: true,
  imports: [FormsModule, NgFor, NgIf, CommentComponent],
  providers: [DatePipe],
  templateUrl: './room.component.html',
  styleUrl: './room.component.css'
})
export class RoomComponent implements OnInit {

  selectedTab = 0;
  scienceRoom!: Room;
  cultureRoom!: Room;
  sportRoom!: Room;
  musicRoom!: Room;
  permissions : Permission[] | undefined;
  displayModal : boolean = false;
  content!: string | null;

  constructor(private userService : UserService, private commentService : CommentService,
              private datePipe : DatePipe, private roomService : RoomService) { }

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

  postComment() {
    let permission = this.getPermissionForRoom();
    if (!permission?.post){
      alert("Nemate dozvolu da objavite komentar!")
      return;
    }

    this.displayModal = true;
  }

  closeModal() {
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
    this.displayModal = false;
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

  selectTab(index: number) {
    this.selectedTab = index;
  }

}
