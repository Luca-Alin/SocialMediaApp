import {defineStore} from "pinia";
import type {PostDTO} from "../services/post-service/model/PostDTO";

export const usePostsStore = defineStore("Posts", {
    state: () => ({
        posts: [] as (PostDTO | null)[]
    }),
    actions: {
        deletePosts() {
            this.posts.length = 0;
        },
        addPost(postDTO: PostDTO) {
            this.posts.push(postDTO);
            this.posts.sort((a, b) => {
                //sort post by newest created to the oldest
                const aTime = new Date(a.createdAt).getTime();
                const bTime = new Date(b.createdAt).getTime();
                return bTime - aTime;
            });
        },
        addPosts(postDTOs: PostDTO[]) {
            postDTOs.forEach(post => this.addPost(post));
        },
        deletePost(postDTO: PostDTO) {
            this.posts = this.posts.filter(post => post.uuid !== postDTO.uuid);
        }
    }
});