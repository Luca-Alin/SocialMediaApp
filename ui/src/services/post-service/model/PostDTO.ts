import {CommentDTO} from "../../comment-service/model/CommentDTO";
import {UserDTO} from "../../user-service/model/UserDTO";
import type {PostReactionType} from "src/services/post-service/model/PostReactionType";
import type {PostReactionDTO} from "src/services/post-service/model/PostReactionDTO";

export interface PostDTO {
    uuid: string | null;
    content: string | null;
    createdAt: Date | null; // Assuming LocalDateTime maps to Date in TypeScript
    user: UserDTO | null;
    images: string[] | null; // Assuming ArrayBuffer represents byte array in TypeScript
    comments: CommentDTO[] | null;
    postReactions: PostReactionDTO[] | null;
}
