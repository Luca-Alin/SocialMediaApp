import type {PostDTO} from "src/services/post-service/model/PostDTO";
import type {CommentDTO} from "src/services/comment-service/model/CommentDTO";
import axios, {AxiosResponse} from "axios";

class CommentService {
    private url = "/comments";
    public addComment(commentDTO: CommentDTO, postDTO: PostDTO) : Promise<AxiosResponse<CommentDTO>> {
        return axios.post<CommentDTO>(`${this.url}/add`, {
            content: commentDTO.content,
            post: {
                uuid: postDTO.uuid
            }
        });
    }

}

const commentService = new CommentService();
export default commentService;