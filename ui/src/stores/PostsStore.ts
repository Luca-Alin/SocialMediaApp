import {defineStore} from "pinia";
import type {PostDTO} from "../services/post-service/model/PostDTO";

export const usePostsStore = defineStore("Posts", {
    state: () => ({
        posts: [] as (PostDTO | null)[],
        index: 0
    }),
    actions: {
        deletePosts() {
          this.posts.length = 0;
        },
        addPost(postDTO: PostDTO) {
            console.log("adding post", this.posts.length);
            this.posts.push(postDTO);
            this.posts.sort((b, a) => {
                return new Date(a.createdAt).getTime() - new Date(b.createdAt).getTime()
            });
            console.log(this.posts.length);
        },
        addPosts(postDTOs: PostDTO[]) {
            postDTOs.forEach(post => this.addPost(post));
        },
        addFakePosts(count: number) {
            for (let i = 0; i < count; i++) this.posts.push(null);
        },
        removeAllFakePosts() {
            this.posts = this.posts.filter(p => p !== null);
        }
    }
});