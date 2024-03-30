<script setup lang="ts">

import PostComponent from "../components/PostComponent.vue";
import FakePost from "../components/FakePost.vue";
import {storeToRefs} from "pinia";
import {usePostsStore} from "../stores/PostsStore";
import {computed, onMounted, ref, type Ref} from "vue";
import postService from "../services/post-service/PostService";


const postsStore = usePostsStore();
const {posts} = storeToRefs(postsStore);

let page = 0;
let count = 5;

const initialPostsAreLoaded: Ref<boolean> = ref(false);

const allPostsAreLoaded: Ref<boolean> = ref(false);


const computedPosts = computed(() => {
  return posts.value.sort((a, b) => {
    const aTime = new Date(a.createdAt).getTime();
    const bTime = new Date(b.createdAt).getTime();
    return bTime - aTime;
  });
});

function loadPosts() {
  postService.findAllByUserAndFriends(page, count)
      .then((res) => {
        initialPostsAreLoaded.value = true;
        page++;
        if (res.data.length == 0) {
          allPostsAreLoaded.value = true;
          return;
        } else {
          postsStore.addPosts(res.data);
        }
      })
      .catch(_ => {
      })
      .finally(() => {
      });
}

onMounted(() => {
  postsStore.deletePosts();

  loadPosts();

  const options = {
    root: null,
    rootMargin: "0px",
    threshold: 0.5
  };
  const observer = new IntersectionObserver(handleIntersection, options);

  function handleIntersection(entries, observer) {
    entries.forEach(entry => {
      if (initialPostsAreLoaded.value && entry.isIntersecting) {
        loadPosts();
      }
    });
  }

  const endOfPage = document.getElementById("end-of-page");
  observer.observe(endOfPage);
});


</script>

<template>
  <div v-for="post in posts" class="d-flex flex-row mt-2 mb-2">
    <div v-if="post === null" class="w-100 d-flex justify-content-center">
      <FakePost/>
    </div>
    <div v-else class="w-100 d-flex justify-content-center">
      <PostComponent :post-id="post.uuid"/>
    </div>
  </div>
  <div id="end-of-page" class="d-flex justify-content-center fs-1">
    <div v-if="allPostsAreLoaded">
      You've seen all posts
    </div>
    <div v-else>
      <div class="spinner-border text-secondary" role="status">
        <span class="sr-only"></span>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>