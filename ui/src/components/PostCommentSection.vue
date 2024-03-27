<script setup lang="ts">
import {onMounted, ref, type Ref} from "vue";
import type {CommentDTO} from "../services/comment-service/model/CommentDTO";
import {storeToRefs} from "pinia";
import type {PostDTO} from "../services/post-service/model/PostDTO";
import PostComment from "../components/PostComment.vue";
import {usePostsStore} from "../stores/PostsStore";
import commentService from "../services/comment-service/CommentService";

const postsStore = usePostsStore();
const {posts} = storeToRefs(postsStore);


const props = defineProps({
  propComments: Object,
  postId: String
});

const comments: Ref<CommentDTO[] | null> = ref(null);
const post: Ref<PostDTO | null> = ref(null);
onMounted(() => {
  post.value = posts.value
      .find(p => p !== null && p.uuid === props.postId) as PostDTO;
  comments.value = post.value.comments;
});

const newComment = ref("");

function addNewComment() {
  commentService.addComment({
    content: newComment.value,
    user: null,
    createdAt: null,
    id: null
  }, post.value!)
      .then(res => {
        post.value?.comments?.push(res.data);
      })
      .catch(err => {
        console.log(err);
      })
      .finally(() => {
      });
  newComment.value = "";
}
</script>

<template>
  <div class="accordion accordion-flush" id="accordionFlushExample">
    <div class="accordion-item">
      <h2 class="accordion-header">
        <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                data-bs-target="#flush-collapseOne" aria-expanded="false" aria-controls="flush-collapseOne">
          {{ comments?.length ?? 0 }} comments
        </button>
      </h2>
      <div id="flush-collapseOne" class="accordion-collapse collapse" data-bs-parent="#accordionFlushExample">
        <div class="accordion-body">
          <div class="d-flex">
            <input type="text" v-model="newComment">
            <button @click="addNewComment();">Submit Comment</button>
          </div>
          <div class="d-flex flex-column-reverse">
            <div v-for="comment in comments">
              <PostComment :comment-prop="comment"/>
            </div>
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>