<script setup lang="ts">
import userService from "../services/user-service/UserService";
import {onMounted, type Ref, ref} from "vue";
import type {UserDTO} from "../services/user-service/model/UserDTO";
import router from "../router";
import postService from "../services/post-service/PostService";
import {storeToRefs} from "pinia";
import {useUserInfoStore} from "../stores/UserInfoStore";

const USER: Ref<UserDTO | null> = ref(null);
const firstName = ref("");
const lastName = ref("");
const bio = ref("");
const email = ref("");

function patchChanges() {
  const patchedUser : UserDTO = {
    bio: null,
    email: null,
    firstName: null,
    lastName: null,
    profileImage: null,
    uuid: null
  }
  if (email.value != USER!.value!.email!) patchedUser.email = email.value;
  if (firstName.value != USER!.value!.firstName!) patchedUser.firstName = firstName.value;
  if (lastName.value != USER!.value!.lastName!) patchedUser.lastName = lastName.value;
  if (bio.value != USER!.value!.bio!) patchedUser.bio = bio.value;

  userService.patchUser(patchedUser)
      .then((res) => {
        USER.value = res.data;
        reset();
      })
      .catch((err) => {
        console.log(err);
      })
}

function reset() {
  firstName.value = USER.value!.firstName!;
  lastName.value = USER.value!.lastName!;
  email.value = USER.value!.email!;
  bio.value = USER.value!.bio!;
}

const userInfo = useUserInfoStore();
const {authenticatedUser, accessToken, refreshToken} = storeToRefs(userInfo);

onMounted(() => {
  if (accessToken.value == null) {
    router.push("/login");
    return;
  }

  userService.findUserDetails()
      .then(res => {
        USER.value = res.data;
        reset();
      });
});
</script>

<template>
  <div v-if="USER" class="d-flex flex-column">
    <div>
      First Name: <input type="text" v-model="firstName">
    </div>
    <div>
      Last Name: <input type="text" v-model="lastName">
    </div>
    <div>
      Email: <input type="email" v-model="email">
    </div>
    <div>
      Bio: <input type="text" v-model="bio">
    </div>

    <button @click="patchChanges()" class="btn btn-warning">Save Changes</button>
    <button @click="reset()" class="btn btn-primary">Reset Changes</button>
  </div>


</template>

<style scoped>

</style>