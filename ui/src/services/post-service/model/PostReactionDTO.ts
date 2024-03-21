import type {PostReactionType} from "src/services/post-service/model/PostReactionType";

export class PostReactionDTO {
    userId : string;
    postReactionType: PostReactionType;
}