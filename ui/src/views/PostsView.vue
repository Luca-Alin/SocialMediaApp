<script setup lang="ts">
import postService from "../services/post-service/PostService";
import {onMounted, watch} from "vue";
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


// TODO - reduce the boilerplatness of this code
function loadPosts(page : number, count: number) {
  postsStore.addFakePosts(5);
  postService.findAllByUserAndFriends()
      .then((res) => {
        postsStore.removeAllFakePosts();
        postsStore.addPosts(res.data);
      })
      .catch((err) => {
        alert(err);
      })
      .finally(() => {
      });
}

onMounted(() => {

  if (accessToken.value == null) {
    router.push("login");
    return;
  }

  watch(accessToken, _ => {
    if (accessToken.value == null)
      router.push("login");
  });

  loadPosts(0, 0);
});
</script>

<template>
<!--  <h1>Posts</h1>-->

<!--  <PostCreationComponent/>-->

  <div v-for="post in posts.values()" class="d-flex flex-row mt-2 mb-2">

    <div v-if="post === null" class="w-100 d-flex justify-content-center">
      <FakePost/>
    </div>
    <div v-else class="w-100 d-flex justify-content-center">
      <PostComponent :post-id="post.uuid"/>
    </div>

  </div>
</template>

<style scoped>

</style>