import axios, {AxiosResponse} from "axios";
import {PostDTO} from "./model/PostDTO";
import type {PostReactionType} from "src/services/post-service/model/PostReactionType";
import type {PostReaction} from "../services/post-service/model/PostReaction";

class PostService {
    url: string = "/posts";

    public findAllByUserAndFriends(pageNumber: number, pageSize: number): Promise<AxiosResponse<PostDTO[]>> {
        return axios.get<PostDTO[]>(`${this.url}/?pageNumber=${pageNumber}&pageSize=${pageSize}`);
    }

    public findByUserId(id: string) {
        return axios.get<PostDTO[]>(`${this.url}/${id}`);
    }

    public createPost(post: PostDTO) {
        const mappedImages = post.images.map(img => {
            return {
                image: img
            };
        });
        return axios.post<PostDTO>(`${this.url}/`, {
            content: post.content,
            images: mappedImages
        });
    }

    public addPostReaction(post: PostDTO, postReactionType: PostReactionType): Promise<AxiosResponse<PostReaction[]>> {
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