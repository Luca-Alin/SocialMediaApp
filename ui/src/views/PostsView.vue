<script setup lang="ts">
import postService from "../services/post-service/PostService";
import {onMounted, type Ref, ref, watch} from "vue";
import PostComponent from "../components/PostComponent.vue";
import router from "../router";
import FakePost from "../components/FakePost.vue";

import {storeToRefs} from "pinia";
import {useUserInfoStore} from "../stores/UserInfoStore";
import {usePostsStore} from "../stores/PostsStore";
import PostCreationComponent from "../components/PostCreationComponent.vue";

const userInfo = useUserInfoStore();
const {user, accessToken, refreshToken} = storeToRefs(userInfo);

const postsStore = usePostsStore();
const {posts} = storeToRefs(postsStore);

let page = 0;
let count = 5;

const lastPostsId : Ref<String | null> = ref(null);
const allPostsAreLoaded = ref(false);
let countOfInitialPosts = -1;
function loadPosts() {
  postService.findAllByUserAndFriends(page, count)
      .then((res) => {
        if (res.data.length == 0) {
          allPostsAreLoaded.value = true;
          return;
        }

        if (countOfInitialPosts == -1) countOfInitialPosts = res.data.length;

        console.log(res.data);
        postsStore.addPosts(res.data);

        lastPostsId.value = `bootstrapId-${res.data[res.data.length - 1].uuid}`;
        page++;

      })
      .catch((err) => {
        console.log(err);
      })
      .finally(() => {
      });
}

onMounted(() => {
  postsStore.deletePosts();

  if (accessToken.value == null) {
    router.push("login");
    return;
  }

  watch(accessToken, _ => {
    if (accessToken.value == null)
      router.push("login");
  });

  loadPosts();

  const options = {
    root: null,
    rootMargin: "0px",
    threshold: 0.5
  };
  const observer = new IntersectionObserver(handleIntersection, options);
  function handleIntersection(entries, observer) {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        if (posts.value.length >= countOfInitialPosts) {
          loadPosts();
        }
      }
    });
  }
  const endOfPage = document.getElementById("end-of-page");
  observer.observe(endOfPage);
});





/*

    var container = document.getElementById("container");
    var globalIndex = 1;

    createDivs();
    function createDivs() {
        for (var i = 0; i < 3; i++) {
            var div = document.createElement("div");
            div.innerText = `div${globalIndex}`;
            div.id = `div${globalIndex++}`;
            div.classList.add("d-flex", "justify-content-center", "align-items-center", "border", "border-1", "border-dark");
            div.style.minHeight = "600px";
            container.append(div);

            if (i === 2) {
                var newDiv = document.getElementById(`div${globalIndex - 1}`);
                observer.observe(newDiv);
            }
        }
    }
*/

</script>

<template>

  <div class="d-flex flex-column" id="content">
    <div class="d-flex justify-content-center">
      <div class="w-100 ps-5 pe-5">
        <PostCreationComponent></PostCreationComponent>
      </div>
    </div>
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
  </div>

</template>

<style scoped>

</style>