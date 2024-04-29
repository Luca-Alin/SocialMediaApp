<script lang="ts" setup>

import {useRoute} from "vue-router";
import {type Ref, ref, watch} from "vue";
import userService from "@/services/user-service/UserService";
import type {UserDTO} from "@/services/user-service/model/UserDTO";

const route = useRoute();
const searchQuery: Ref<string> = ref(route.params["id"] as string);

const result: Ref<UserDTO[]> = ref(null!);

const isLoading = ref(false);

search();

function search() {
  isLoading.value = true;
  userService.searchUsers(searchQuery.value)
      .then(res => {
        result.value = res.data;
      })
      .catch(err => console.log(err))
      .finally(() => isLoading.value = false);
}

watch(route, () => {
  searchQuery.value = route.params["id"] as string;
  search();
});
</script>

<template>
  <div v-if="isLoading">
    <div class="clearfix">
      <div class="spinner-border float-right" role="status">
      </div>
    </div>
  </div>
  <div v-else-if="result" class="d-flex flex-column">
    <div class="fs-1">
      Users:
    </div>
    <div v-for="user in result" :key="user.uuid">
      <RouterLink :to="`/user/${user.uuid}`">
        <img :src="`data:image/png;base64,${user.profileImage}`" alt="" width="50" height="50">
        {{ `${user.firstName} ${user.lastName}` }}
      </RouterLink>
    </div>
  </div>
</template>

<style scoped>

</style>