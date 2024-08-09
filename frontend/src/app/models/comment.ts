import { Room } from "./room";
import { User } from "./user";

export class Comment {
    id! : number;
    date : string;
    content : string | null;
    userId : number;
    username : string | null;
    roomId : number;

    constructor (date : string | null, content : string | null, userId : number, username : string | null, roomId : number){
        this.id = 0;
        this.date = date ?? '';
        this.content = content;
        this.userId = userId;
        this.username = username;
        this.roomId = roomId;
    }
}
