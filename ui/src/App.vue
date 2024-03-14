<script setup lang="ts">
import ChatComponent from "./components/ChatComponent.vue";
import NavbarComponent from "./components/NavbarComponent.vue";
import {useUserInfoStore} from "./stores/UserInfoStore";
import {storeToRefs} from "pinia";

const userStore = useUserInfoStore();
const {user} = storeToRefs(userStore);
</script>

<template>
  <div id="main" class="d-flex flex-column vh-100 min-vh-100">

    <header>
      <NavbarComponent/>
    </header>

    <main class="overflow-y-scroll">

      <div class="d-flex flex-row">
        <div class="container d-flex justify-content-center">
          <div>
            <RouterView/>
          </div>
        </div>

        <div v-if="user" class="container" style="max-height: 500px">
          <ChatComponent style="max-height: 500px; max-width: 300px; position: absolute; right: 3em; bottom: 0"/>
        </div>
      </div>
    </main>


  </div>
</template>

<style scoped>
main::-webkit-scrollbar {
  width: 0.4em;
}

main::-webkit-scrollbar-track {
  background: transparent;
}

main::-webkit-scrollbar-thumb {
  padding-right: 5px;
  background-color: lightgray;
  outline: 1px solid slategrey;
  border-radius: 20px;
}
</style>
