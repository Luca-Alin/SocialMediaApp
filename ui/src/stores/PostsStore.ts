import {defineStore} from "pinia";
import type {PostDTO} from "../services/post-service/model/PostDTO";

export const usePostsStore = defineStore("Posts", {
    state: () => ({
        posts: new Map() as Map<string, PostDTO | null>,
        index: 0
    }),
    actions: {
        addPost(postDTO: PostDTO) {
            this.posts.set(postDTO.uuid, postDTO);
        },
        addPosts(postDTOs: PostDTO[]) {
            postDTOs.forEach(post => this.addPost(post));
        },
        addFakePosts(count: number) {
            for (let i = 0; i < count; i++) this.posts.set(`fake${this.index++}`, null);
        },
        removeAllFakePosts() {
            const entriesToRemove: string[] = [];
            this.posts.forEach((value, key) => {
                if (key.startsWith("fake")) {
                    entriesToRemove.push(key);
                }
            });
            entriesToRemove.forEach(key => {
                this.posts.delete(key);
            });
        }
    }
});