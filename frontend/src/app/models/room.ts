import { Comment } from "./comment";

export class Room {
    id! : number;
    name : string;
    comments : Comment[];

    constructor(name : string) {
        this.name = name;
        this.comments = [];
    }
}
