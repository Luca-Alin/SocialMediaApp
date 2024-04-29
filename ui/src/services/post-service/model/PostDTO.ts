import type {PostReactionDTO} from "src/services/post-service/model/PostReactionDTO";
import type {UserDTO} from "@/services/user-service/model/UserDTO";
import type {CommentDTO} from "@/services/comment-service/model/CommentDTO";

export interface PostDTO {
    uuid: string;
    content: string;
    createdAt: Date;
    user: UserDTO;
    images: string[];
    comments: CommentDTO[];
    postReactions: PostReactionDTO[];
}
