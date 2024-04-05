import type {PostDTO} from "@/services/post-service/model/PostDTO";

export interface GroupPost {
    id : number;
    post: PostDTO;
}