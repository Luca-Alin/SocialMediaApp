<script lang="ts" setup>
import App from "../App.vue";
import {createPinia, storeToRefs} from "pinia";
import {useUserInfoStore} from "@/stores/UserInfoStore";
import {useDeveloperStore} from "@/stores/DeveloperStore";

import {createApp, onMounted, type Ref, ref, watch} from "vue";
import developerService from "../services/developer-mode-service/DeveloperService";
import router from "../router";

const pinia = createPinia();
const app = createApp(App);
app.use(pinia);


const userInfoStore = useUserInfoStore();
const {authenticatedUser, accessToken, refreshToken} = storeToRefs(userInfoStore);

const developerStore = useDeveloperStore();
const {developerMode} = storeToRefs(developerStore);

const searchQuery = ref("");

function searchUsers() {
  router.push({name: "SearchView", params: {id: searchQuery.value}});
}

async function logout() {
  userInfoStore.logoutUser();

  // chatService.disconnect();


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


window.addEventListener("keydown", function (e) {
  if (e.keyCode === 75 && e.ctrlKey) {
    e.preventDefault();
    document.getElementById("search-bar")?.focus();
  }
});
</script>

<template>

  <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">Social Media App</a>
      <button aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"
              class="navbar-toggler"
              data-bs-target="#navbarSupportedContent" data-bs-toggle="collapse" type="button">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div id="navbarSupportedContent" class="collapse navbar-collapse">

        <ul class="navbar-nav me-auto mb-2 mb-lg-0">

          <li v-if="authenticatedUser" class="nav-item">
            <router-link aria-current="page" class="nav-link active" href="#" to="/posts">Home</router-link>
          </li>

          <li v-if="authenticatedUser" class="nav-item dropdown">
            <!--suppress TypeScriptUnresolvedReference -->
            <a id="navbarDropdown" aria-expanded="false" class="nav-link dropdown-toggle active"
               data-bs-toggle="dropdown"
               href="#"
               role="button">
              {{ authenticatedUser!.firstName }} {{ authenticatedUser!.lastName }}
            </a>
            <ul aria-labelledby="navbarDropdown" class="dropdown-menu">
              <li>
                <router-link class="dropdown-item" :to="`/user/${authenticatedUser.uuid}`">My Profile</router-link>
              </li>
              <li>
                <button class="dropdown-item" @click="logout">Logout</button>
              </li>
              <li>
                <router-link class="dropdown-item" to="/settings">Settings</router-link>
              </li>
            </ul>
          </li>


          <li class="nav-item">
            <button aria-current="page" class="nav-link active" @click="enableDeveloperMode()">
              <span v-if="developerMode">
                Disable developer mode
              </span>
              <span v-else>
                Enable developer mode
              </span>
            </button>
          </li>

          <li v-if="developerMode" class="nav-item">
            <div aria-current="page" class="nav-link active d-flex flex-column">
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
          <input id="search-bar" v-model="searchQuery" aria-label="Search"
                 class="form-control me-2" placeholder="Ctrl+K" type="search"
                 @keyup.enter.prevent="searchUsers">
          <button class="btn btn-outline-light" type="submit" @click="searchUsers">Search</button>
        </div>

      </div>

    </div>
  </nav>

</template>

<style scoped>

</style>