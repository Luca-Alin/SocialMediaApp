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
import PostsDisplay from "../components/PostsDisplay.vue";

const userInfo = useUserInfoStore();
const {accessToken} = storeToRefs(userInfo);

onMounted(() => {
  if (accessToken.value == null) {
    router.push("login");
    return;
  }

  watch(accessToken, _ => {
    if (accessToken.value == null)
      router.push("login");
  });
});
</script>

<template>

  <div class="d-flex flex-column" id="content">
    <div class="d-flex justify-content-center">
      <div class="w-100 ps-5 pe-5">
        <PostCreationComponent></PostCreationComponent>
      </div>
    </div>

    <PostsDisplay/>

  </div>

</template>

<style scoped>

</style>