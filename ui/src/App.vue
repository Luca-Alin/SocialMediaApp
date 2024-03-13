<script setup lang="ts">
import ChatComponent from "./components/ChatComponent.vue";
import NavbarComponent from "./components/NavbarComponent.vue";
import {useUserInfoStore} from "./stores/UserInfoStore";
import {storeToRefs} from "pinia";

const userStore = useUserInfoStore();
const {user} = storeToRefs(userStore);
</script>

<template>
  <div class="container d-flex flex-column">

    <div>
      <NavbarComponent/>
    </div>

    <div>
      <div class="d-flex flex-row">
        <div class="container">
          <RouterView/>
        </div>

        <div v-if="user" class="container" style="max-height: 500px">
          <ChatComponent style="max-height: 500px; max-width: 300px; position: absolute; right: 0; bottom: 0"/>
        </div>
      </div>
    </div>


  </div>
</template>

<style scoped>
</style>
