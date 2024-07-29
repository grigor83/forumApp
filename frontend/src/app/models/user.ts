import { Permission } from "./permission";
import { Room } from "./room";

export class User {
    id!: number;
    username: string | null;
    password: string | null;
    email: string | null;
    role : string | null;
    verified : boolean;
    banned : boolean;
    code : number;
    permissions : Permission[];

    constructor(username:string | null, password:string | null, email : string | null, role : string | null) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.verified = false;
        this.banned = false;
        this.code = 0;
        this.permissions = [];
    }
}
