<script setup lang="ts">
import {onMounted, watch} from "vue";
import router from "../router";

import {storeToRefs} from "pinia";
import {useUserInfoStore} from "../stores/UserInfoStore";
import PostCreationComponent from "../components/PostCreationComponent.vue";
import PostsDisplay from "../components/PostsDisplay.vue";
import {usePostsStore} from "@/stores/PostsStore";

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

const postStore = usePostsStore();
const {posts} = storeToRefs(postStore);
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