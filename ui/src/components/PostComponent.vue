<script setup lang="ts">
import type {PostDTO} from "../services/post-service/model/PostDTO";
import {computed, onMounted, type Ref, ref, watch} from "vue";
import PostCommentSection from "./PostCommentSection.vue";
import {usePostsStore} from "../stores/PostsStore";
import {storeToRefs} from "pinia";
import {PostReactionType} from "../services/post-service/model/PostReactionType";
import {formatDate} from "../services/format-date-service/FormatDateService";
import {useUserInfoStore} from "../stores/UserInfoStore";
import postService from "../services/post-service/PostService";

const userInfo = useUserInfoStore();
const {authenticatedUser} = storeToRefs(userInfo);

const postsStore = usePostsStore();
const {posts} = storeToRefs(postsStore);

const props = defineProps({
  postId: String
});


const post: Ref<PostDTO | null> = ref(null);
const bootstrapId: Ref<string | null> = ref(null);
let imagesIndex: Ref<number[]> = ref([]);

onMounted(() => {
  post.value = posts.value.find(p => p !== null && p.uuid === props.postId) as PostDTO;
  bootstrapId.value = `bootstrapId-${post.value?.uuid}`;

  // if there is a single image, there will be no indicators for the carousel
  const imagesCount = post.value.images.length;
  if (imagesCount && imagesCount > 1)
    for (let i = 1; i <= imagesCount; i++)
      imagesIndex.value.push(i);
});

watch(posts, _ => {
  post.value = posts.value.find(p => p?.uuid === props.postId) as PostDTO;
}, {
  deep: true
});

function numberOfPostReactionTypes(postRT: PostReactionType) {
  return post.value?.postReactions?.filter(prt => prt.postReactionType == postRT).length;
}

function addPostReaction(postReactionType: PostReactionType) {
  if (post.value === null) return;

  postService.addPostReaction(post.value, postReactionType)
      .then((res) => post.value!.postReactions = res.data)
      .catch((err) => console.error(err))
      .finally();
}

function isThisMyReaction(postRT: PostReactionType): boolean {
  return post.value?.postReactions?.filter(prt => {
    return prt.userId == authenticatedUser.value.uuid && prt.postReactionType == postRT;
  }).length == 1;
}

function deletePost() {
  postService.deletePost(post.value?.uuid!)
      .then(_ => {
        postsStore.deletePost(post.value!);
      })
      .catch(_ => {
      })
      .finally(() => {
      });
}

const showLessContent: Ref<boolean> = ref(true);
const maxWordsCount = 100;

const resizableContent = computed(() => {
  if (showLessContent.value)
    return post.value?.content.slice(0, maxWordsCount) + "...";
  return post.value?.content;
});

function toggleContent() {
  showLessContent.value = !showLessContent.value;
}

</script>

<template>
  <div v-if="post" class="flex-fill border border-1 bg-primary rounded-3 bg-white content">
    <div class="d-flex m-2">
      <div class="ms-2 d-flex justify-content-center align-items-center align-content-center">
        <router-link :to="{
              name: `User`,
              params: {id: `${post?.user?.uuid}`}}">
          <img :src="`data:image/png;base64,${post?.user?.profileImage}`"
               class="rounded-circle"
               width="40" height="40" alt=""/>
        </router-link>
      </div>
      <div class="ms-2 d-flex flex-column flex-fill">
        <div>
          <router-link :to="{
              name: `User`,
              params: {id: `${post?.user?.uuid}`}}">
            {{ post?.user?.firstName }} {{ post?.user?.lastName }}
          </router-link>
        </div>
        <div>
          {{ `${(post?.createdAt != null) ? formatDate(post!.createdAt!) : ""}` }}
        </div>
      </div>

      <!-- Delete Post Button -->
      <div v-if="post.user.uuid == authenticatedUser.uuid" class="me-1" @click="deletePost()">
        <button class="btn btn-danger">
          <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash"
               viewBox="0 0 16 16">
            <path
                d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1
                0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z"/>
            <path
                d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1
                1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0
                1-1V4.059L11.882 4zM2.5 3h11V2h-11z"/>
          </svg>
        </button>
      </div>
    </div>

    <div class="ms-2 me-2 overflow-hidden">
      <div v-if="post">
        <div v-if="post && post.content.length < maxWordsCount">
          {{ post.content }}
        </div>
        <div v-else>
          <div v-if="showLessContent">
            {{ resizableContent }}
            <a href="#" @click="toggleContent" class="btn btn-link">Show More</a>
          </div>
          <div v-else>
            {{ resizableContent }}
            <a href="#" @click="toggleContent" class="btn btn-link">Show Less</a>
          </div>
        </div>
      </div>
    </div>

    <!-- This ensures the post will use a normal width without images -->
    <div v-if="post.images.length === 0">
      <img src="" alt="" width="800" height="1" class="d-block w-100">
    </div>
    <!-- Post Images Carousel -->
    <div :id="bootstrapId!" class="carousel carousel slide">
      <div class="carousel-indicators">
        <button v-for="i in imagesIndex"
                type="button" :data-bs-target="`#${bootstrapId}`" :data-bs-slide-to="i - 1"
                :class="i == 1 ? 'active' : ''"
                aria-current="true"
                :aria-label="`Slide ${i}`">
        </button>
      </div>
      <div class="carousel-inner">
        <div v-if="post?.images && post.images.length > 0" class="carousel-item active"
             data-bs-interval="10000">
          <img :src="`data:image/png;base64,${post?.images[0]}`"
               class="bd-placeholder-img bd-placeholder-img-lg d-block w-100"
               width="800" height="450" alt="">
        </div>
        <div v-if="post?.images && post!.images!.length > 1" v-for="image in post?.images">
          <div v-if="image != post.images[0]" class="carousel-item" data-bs-interval="2000">
            <img :src="`data:image/png;base64,${image}`"
                 class="bd-placeholder-img bd-placeholder-img-lg d-block w-100"
                 width="800" height="450" alt="">
          </div>
        </div>
      </div>
      <button v-if="post?.images && post.images.length > 1" class="carousel-control-prev" type="button"
              :data-bs-target="`#${bootstrapId}`" data-bs-slide="prev">
        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Previous</span>
      </button>
      <button v-if="post?.images && post.images.length > 1" class="carousel-control-next" type="button"
              :data-bs-target="`#${bootstrapId}`" data-bs-slide="next">
        <span class="carousel-control-next-icon" aria-hidden="true"></span>
        <span class="visually-hidden">Next</span>
      </button>
    </div>

    <!-- Other Post Data -->
    <div v-if="post" class="d-flex">
      <button v-for="reaction in Object.keys(PostReactionType)"
              class="post-reaction-btn btn flex-fill"
              @click="addPostReaction(reaction)">
        <span
            :class="isThisMyReaction(reaction) ? 'border border-primary border-1 rounded-circle pt-2 pb-2 ps-1 pe-2' : ''">
          {{ numberOfPostReactionTypes(reaction) }}
          {{ PostReactionType[reaction] }}
        </span>
      </button>
    </div>

    <PostCommentSection :post-id="props.postId!"/>
  </div>
</template>

<style scoped>
.post-reaction-btn:active {
  font-size: 20px;
}

.content {
  min-width: 400px;
  width: auto;
  max-width: 600px;
}

.no-image-placeholder {
  width: 100%; /* Set the width to fill the container */
  height: 300px; /* Set the desired height for the placeholder */
  background-color: transparent; /* Set the background color to transparent */
  border: 1px dashed #ccc; /* Optional: Add a dashed border for visual clarity */
  box-sizing: border-box; /* Ensure the border is included in the width */
}
</style>


