<script setup lang="ts">
import {useRoute} from "vue-router";
import {onMounted, type Ref, ref} from "vue";
import type {UserDTO} from "../services/user-service/model/UserDTO";
import userService from "../services/user-service/UserService";

const route = useRoute();
const userId = route.params["id"];
const user : Ref<UserDTO | null> = ref(null);
const isLoading = ref(false);

onMounted(() => {
  isLoading.value = true;
  userService.findUserById(userId as string)
      .then((res) => {
        user.value = res.data;
      })
      .catch((err) => {
        user.value = null;
        console.log(err);
      })
      .finally(() => {
        isLoading.value = false;
      })

})
</script>

<template>
  <div v-if="isLoading">
    Loading screen
  </div>
  <div v-else-if="user == null">
    User not found
  </div>
  <div v-else>
    <div>
      {{ user.firstName }} {{ user.lastName }}'s Profile
    </div>
    <div>
      {{ user.bio }}
    </div>
  </div>
</template>

<style scoped>

</style>