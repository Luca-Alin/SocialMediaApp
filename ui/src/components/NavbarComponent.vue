<script setup lang="ts">

import App from "../App.vue";
import {createPinia, storeToRefs} from "pinia";
import {useUserInfoStore} from "../stores/UserInfoStore";
import {useDeveloperStore} from "../stores/DeveloperStore";

import {createApp, onMounted, type Ref, ref, watch} from "vue";
import chatService from "../services/chat-service/ChatService";
import developerService from "../services/developer-mode-service/DeveloperService";
import router from "../router";

const pinia = createPinia();
const app = createApp(App);
app.use(pinia);


const userInfoStore = useUserInfoStore();
const {authenticatedUser, accessToken, refreshToken} = storeToRefs(userInfoStore);

const developerStore = useDeveloperStore();
const {developerMode} = storeToRefs(developerStore);

async function logout() {
  userInfoStore.logoutUser();
  chatService.disconnect();


  await router.push("/login");
}

function enableDeveloperMode() {
  developerStore.setDeveloperMode();
}

const accessTokenExpTime: Ref<string | null> = ref(null);
const refreshTokenExpTime: Ref<string | null> = ref(null);

onMounted(() => {
  if (developerMode.value) {
    setInterval(() => {
      accessTokenExpTime.value = `${developerService.getTokenExpirationTime(accessToken.value)}s`;
      refreshTokenExpTime.value = `${developerService.getTokenExpirationTime(refreshToken.value)}s`;
    }, 1000);
  } else {
    accessTokenExpTime.value = null;
    refreshTokenExpTime.value = null;
  }
});
watch((developerMode), () => {
  let timer = null;
  if (developerMode.value) {
    timer = setInterval(() => {
      accessTokenExpTime.value = developerService.getTokenExpirationTime(accessToken.value);
      refreshTokenExpTime.value = developerService.getTokenExpirationTime(refreshToken.value);
    }, 1000);
  } else {
    accessTokenExpTime.value = null;
    refreshTokenExpTime.value = null;
    timer = null;
  }
});
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

          <li v-if="authenticatedUser" class="nav-item">
            <router-link to="/posts" class="nav-link active" aria-current="page" href="#">Home</router-link>
          </li>

          <li v-if="authenticatedUser" class="nav-item dropdown">
            <!--suppress TypeScriptUnresolvedReference -->
            <a class="nav-link dropdown-toggle active" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown"
               aria-expanded="false">
              {{ authenticatedUser!.firstName }} {{ authenticatedUser!.lastName }}
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

          <li v-if="developerMode" class="nav-item">
            <div class="nav-link active d-flex flex-column" aria-current="page">
              <span>
                {{ `Access Token: ${accessTokenExpTime}` }}
              </span>
              <span>
                {{ `Refresh Token: ${refreshTokenExpTime}` }}
              </span>
            </div>
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