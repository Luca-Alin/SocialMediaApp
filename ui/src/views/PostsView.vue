<script setup lang="ts">
import postService from "../services/post-service/PostService";
import {createApp, onMounted, ref, watch} from "vue";
import PostComponent from "../components/PostComponent.vue";
import router from "../router";
import {createPinia, storeToRefs} from "pinia";
import {useUserInfoStore} from "../stores/UserInfoStore";

const userInfo = useUserInfoStore();
const {user, accessToken, refreshToken} = storeToRefs(userInfo);

const posts = ref([]);

// TODO - reduce the boilerplatness of this code
watch(accessToken, (oldVal, newVal) => {
  if (accessToken.value == null)
    router.push("/login");
})
onMounted(() => {
  if (accessToken.value == null) {
    router.push("/login");
    return;
  }

  postService.findAllByUserAndFriends()
      .then((res) => {
        posts.value = res.data;
      })
      .catch((err) => {
        alert(err);
      })
      .finally(() => {
      });
});
</script>

<template>
  <h1>Posts</h1>

  <div v-for="post in posts">
    <PostComponent :propPost="post"/>
  </div>
</template>

<style scoped>

</style>