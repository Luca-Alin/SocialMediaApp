import {CommentDTO} from "../../comment-service/model/CommentDTO";
import {UserDTO} from "../../user-service/model/UserDTO";
import type {PostReactionDTO} from "src/services/post-service/model/PostReactionDTO";

export interface PostDTO {
    uuid: string;
    content: string;
    createdAt: Date;
    user: UserDTO;
    images: string[];
    comments: CommentDTO[];
    postReactions: PostReactionDTO[];
}
