import {type UserDTO} from "../../user-service/model/UserDTO";

export interface CommentDTO {
    id: number | null;
    content: string | null;
    user: UserDTO | null;
    createdAt: Date | null;
}
