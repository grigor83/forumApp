import { Room } from "./room";
import { User } from "./user";

export class Comment {
    id! : number;
    date : string;
    content : string | null;
    user : User | null;
    room : Room | null;

    constructor (date : string | null, content : string | null, user : User | null, room : Room | null){
        this.date = date ?? '';
        this.content = content;
        this.user = user;
        this.room = room;
    }
}
