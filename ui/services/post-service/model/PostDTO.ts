import {CommentDTO} from "../../comment-service/model/CommentDTO";
import {UserDTO} from "../../user-service/model/UserDTO";

export interface PostDTO {
    uuid: string;
    content: string;
    createdAt: Date; // Assuming LocalDateTime maps to Date in TypeScript
    user: UserDTO;
    images: ArrayBuffer[]; // Assuming ArrayBuffer represents byte array in TypeScript
    comments: CommentDTO[];
}
