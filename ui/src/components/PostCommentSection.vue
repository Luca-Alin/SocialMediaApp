<script setup lang="ts">
import {onMounted, ref, type Ref, watch} from "vue";
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
const commentSectionId : Ref<string | null> = ref(null);

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

onMounted(() => {
  commentSectionId.value = props.postId + "?comment-section";
});

watch(posts, _ => {
  post.value = posts.value.find(p => p?.uuid === props.postId) as PostDTO;
  comments.value = post.value?.comments;
}, {
  deep: true
});
</script>

<template>
  <div class="accordion accordion-flush" :id="`${commentSectionId!}a`">
    <div class="accordion-item">
      <h2 class="accordion-header">
        <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse"
                :data-bs-target="`#${commentSectionId!}b`" aria-expanded="false" :aria-controls="`${commentSectionId!}b`">
          {{ comments?.length ?? 0 }} comments
        </button>
      </h2>
      <div :id="`${commentSectionId!}b`" class="accordion-collapse collapse" :data-bs-parent="`#${commentSectionId!}a`">
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