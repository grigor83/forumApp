import { Room } from "./room";
import { User } from "./user";

export class Permission {
    id!: number;
    post: boolean;
    edit: boolean;
    delete: boolean;
    room : Room | null;
    user : User | null;

    constructor(post:boolean, edit:boolean, deleteComment:boolean, room : Room, user : User) {
        this.post = post;
        this.edit = edit;
        this.delete = deleteComment;
        this.room = room;
        this.user = user;
    }
}
