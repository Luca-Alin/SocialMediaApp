<script setup lang="ts">

import App from "../App.vue";
import {createPinia, storeToRefs} from "pinia";
import {useUserInfoStore} from "../stores/UserInfoStore";
import {useDeveloperStore} from "../stores/DeveloperStore";

import {createApp} from "vue";
import chatService from "../services/chat-service/ChatService";

const pinia = createPinia();
const app = createApp(App);
app.use(pinia);


const userInfoStore = useUserInfoStore();
const {user} = storeToRefs(userInfoStore);

const developerStore = useDeveloperStore();
const {developerMode} = storeToRefs(developerStore);

function logout() {
  userInfoStore.logoutUser();
  chatService.disconnect();

}

function enableDeveloperMode() {
  developerStore.setDeveloperMode();
}


</script>

<template>

  <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
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
            <!--suppress TypeScriptUnresolvedReference -->
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown"
               aria-expanded="false">
              {{ user!.firstName }} {{ user!.lastName }}
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

        <div class="d-flex">
          <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
          <button class="btn btn-outline-light" type="submit">Search</button>
        </div>

      </div>

    </div>
  </nav>

</template>

<style scoped>

</style>