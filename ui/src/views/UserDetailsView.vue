<script setup lang="ts">
import {useRoute} from "vue-router";
import {onMounted, type Ref, ref} from "vue";
import type {UserDTO} from "../services/user-service/model/UserDTO";
import userService from "../services/user-service/UserService";
import postService from "../services/post-service/PostService";
import PostComponent from "../components/PostComponent.vue";
import {usePostsStore} from "../stores/PostsStore";
import {storeToRefs} from "pinia";
import FakePost from "../components/FakePost.vue";
import friendshipService from "../services/friendship-service/FriendshipService";
import {type FriendshipStatus} from "../services/friendship-service/model/FriendshipStatus";
import {useUserInfoStore} from "../stores/UserInfoStore";

const userStore = useUserInfoStore();
const {authenticatedUser} = storeToRefs(userStore);


const route = useRoute();
const userId = route.params["id"];
const userProfileDetails: Ref<UserDTO | null> = ref(null);
const isLoading = ref(false);

const postsStore = usePostsStore();
const {posts} = storeToRefs(postsStore);


const friendshipStatus: Ref<FriendshipStatus | null> = ref(null);

function friendshipAction() {
  switch (friendshipStatus.value) {
    case null:
      break;
    case("ACCEPTED"):
      console.log("unfriend");
      break;
    case("SENT"):
      console.log("unfriend");
      break;
    case("NONE"):
      console.log("send friendship request");
      break;
  }
}

async function findFriendshipStatus() {
  friendshipStatus.value = ((await friendshipService.friendshipStatus(userProfileDetails.value!)).data);
}

onMounted(() => {
  isLoading.value = true;
  userService.findUserById(userId as string)
      .then((res) => {
        userProfileDetails.value = res.data;
        findFriendshipStatus();
      })
      .then(_ => {
        postService.findByUserId(userProfileDetails.value?.uuid!)
            .then((res) => {
              postsStore.deletePosts();
              postsStore.addPosts(res.data);
            });
      })
      .catch((err) => {
        userProfileDetails.value = null;
        console.log(err);
      })
      .finally(() => {
        isLoading.value = false;
      });


});
</script>

<template>
  <div v-if="isLoading">
    Loading screen
  </div>
  <div v-else-if="userProfileDetails == null">
    User not found
  </div>
  <div v-else class="d-flex flex-column">
    <div style="min-width: 500px">
      <!-- Profile Image and Friendship Button -->
      <div class="float-start d-flex flex-column me-2 mb-2">

        <img :src="`data:img/png;base64,${userProfileDetails.profileImage}`" alt="profile image"
             class="rounded-circle border border-dark border-5"
             width="200" height="200">

        <div v-if="authenticatedUser?.uuid !== userProfileDetails?.uuid">
          <button class="btn btn-primary m-2" :disabled="!friendshipStatus" @click="friendshipAction">
            <span v-if="friendshipStatus">{{ friendshipStatus }}</span>
            <span v-else class="spinner-border" role="status"></span>
          </button>
        </div>
      </div>
      <!-- Name and Bio -->
      <div>
        <div class="fs-1">
          {{ userProfileDetails?.firstName }} {{ userProfileDetails?.lastName }}
        </div>
        <div>
          {{ userProfileDetails?.bio }}
        </div>
      </div>
    </div>

    <!-- Posts Made By User -->
    <div class="d-flex flex-column" id="content">
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

  </div>
</template>

<style scoped>

</style>