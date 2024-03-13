<script setup lang="ts">
import {createPinia, storeToRefs} from "pinia";
import {createApp, onMounted} from "vue";

import App from "../App.vue";
import {useUserInfoStore} from "../stores/UserInfoStore";
import chatService from "../services/chat-service/ChatService";
import {useDeveloperStore} from "../stores/DeveloperStore";

const pinia = createPinia();
const app = createApp(App);
app.use(pinia);
const userInfo = useUserInfoStore();

const {user} = storeToRefs(userInfo);

function logout() {
  userInfo.logoutUser();
  chatService.disconnect();
}

const developer = useDeveloperStore();
const {developerMode} = storeToRefs(developer);

function enableDeveloperMode() {
  developer.setDeveloperMode();
}

onMounted(() => {

});
</script>

<template>
  <!-- Navbar if user is connected -->
  <nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">Social Media App</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
              aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">

          <li v-if="user" class="nav-item">
            <router-link to="/posts" class="nav-link active" aria-current="page" href="#">Home</router-link>
          </li>

          <li v-if="user" class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown"
               aria-expanded="false">
              {{ user?.firstName }} {{ user?.lastName }}
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <li>
                <button class="dropdown-item" @click="logout">Logout</button>
              </li>
              <li>
                <router-link class="dropdown-item" to="/settings">Settings</router-link>
              </li>
            </ul>
          </li>


          <li class="nav-item">
            <button class="nav-link active" aria-current="page" @click="enableDeveloperMode()">
              <span v-if="developerMode">
                Disable developer mode
              </span>
              <span v-else>
                Enable developer mode
              </span>
            </button>
          </li>

        </ul>
        <form class="d-flex">
          <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
          <button class="btn btn-outline-success" type="submit">Search</button>
        </form>
      </div>
    </div>
  </nav>

  <!-- Navbar if user is not connected -->
</template>

<style scoped>

</style>