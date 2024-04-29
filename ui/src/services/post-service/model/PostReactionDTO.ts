import type {PostReactionType} from "@/services/post-service/model/PostReactionType";

export interface PostReactionDTO {
    userId : string;
    postReactionType: PostReactionType;
}