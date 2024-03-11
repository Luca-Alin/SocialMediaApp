import {UserDTO} from "../../user-service/model/UserDTO";

export interface CommentDTO {
    id: number;
    content: string;
    user: UserDTO;
    createdAt: Date; // Assuming LocalDateTime maps to Date in TypeScript
}
