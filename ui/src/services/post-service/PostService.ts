import axios, {AxiosResponse} from "axios";
import {PostDTO} from "./model/PostDTO";
import type {PostReactionType} from "src/services/post-service/model/PostReactionType";
import type {PostReaction} from "../services/post-service/model/PostReaction";

class PostService {
    url = "/posts";

    public findAllByUserAndFriends() {
        return axios.get<PostDTO[]>(`${this.url}/`);
    }

    public findByUserId(id: string) {
        return axios.get<PostDTO[]>(`${this.url}/${id}`);
    }

    public createPost(post: PostDTO) {
        const imgs : {image: string}[] = post.images?.map((img) => img);
        return axios.post<PostDTO>(`${this.url}/`, {
            images: [
                {
                    image: post.images[0]
                }
            ]
        });
    }

    public addPostReaction(post : PostDTO, postReactionType: PostReactionType) : Promise<AxiosResponse<PostReaction[]>> {
        return axios.post<PostReaction[]>(
            `${this.url}/reaction/${post.uuid}`, `"${postReactionType}"`, {
                headers: {
                    "Content-Type": "application/json"
                }
            });
    }
}

const postService = new PostService();
export default postService;