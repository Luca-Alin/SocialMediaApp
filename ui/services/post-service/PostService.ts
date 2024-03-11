import axios from "axios";
import {PostDTO} from "./model/PostDTO";

class PostService {
    url = "/posts";

    public findAllByUserAndFriends() {
        return axios.get<PostDTO[]>(`${this.url}/`);
    }

    public findByUserId(id : string)  {
        return axios.get<PostDTO[]>(`${this.url}/${id}`);
    }

    public createPost(post: PostDTO) {
        return axios.post<PostDTO>(`${this.url}/`, post);
    }
}

const postService = new PostService();
export default postService;