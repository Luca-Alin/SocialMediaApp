<script setup lang="ts">
import ChatComponent from "./components/ChatComponent.vue";
import NavbarComponent from "./components/NavbarComponent.vue";
import {useUserInfoStore} from "./stores/UserInfoStore";
import {storeToRefs} from "pinia";
import {ref, type Ref, watch} from "vue";
import GroupComponent from "@/components/GroupComponent.vue";
import router from "@/router";

const userStore = useUserInfoStore();
const {authenticatedUser} = storeToRefs(userStore);

const chatWindowIsOpen: Ref<boolean> = ref(false);

watch(authenticatedUser, _ => {
  if (!authenticatedUser)
    router.push("/login");
})
</script>

<template>

  <div id="page-content" class="d-flex flex-column">

    <header>
      <NavbarComponent/>
    </header>

    <main class="d-flex flex-fill overflow-auto w-100 h-100">

      <div v-if="authenticatedUser" class="d-flex flex-column p-2">
        <GroupComponent/>
      </div>

      <!-- Content - posts, settings, user profiles etc. -->
      <div class="d-flex flex-fill justify-content-center">
        <div style="max-height: 300px; max-width: 768px;">
          <RouterView class="flex-fill d-flex justify-content-center"/>
        </div>
      </div>


    </main>

  </div>

  <!-- Floating Stuff -->
  <!-- Chat -->
  <ChatComponent v-if="authenticatedUser"
                 :chat-is-maximised="chatWindowIsOpen"
                 @maximize-request="() => chatWindowIsOpen = !chatWindowIsOpen"
                 :class="chatWindowIsOpen ? 'actual-chat' : 'dsmnone actual-chat'"/>

  <!-- chat window button  -->
  <div v-if="authenticatedUser && !chatWindowIsOpen" id="open-chat-button" class="d-md-none">
    <button
        type="button"
        class="btn btn-primary rounded-5 pt-2 pb-2"
        @click="chatWindowIsOpen = !chatWindowIsOpen"
    >
      <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-chat"
           viewBox="0 0 16 16">
        <path
            d="M2.678 11.894a1 1 0 0 1 .287.801 11 11 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8 8 0 0 0 8 14c3.996 0 7-2.807 7-6s-3.004-6-7-6-7 2.808-7 6c0 1.468.617 2.83 1.678 3.894m-.493 3.905a22 22 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a10 10 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9 9 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105"></path>
      </svg>
    </button>
  </div>
</template>

<style scoped>
#page-content {
  background: rgb(233, 230, 230);
  height: 100vh;
  min-height: 100vh;
  max-height: 100vh;
  overflow: hidden;
}

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

.chat-windows-space {
  min-width: 20%;
}

@media (max-width: 767px) {
  .dsmnone {
    display: none !important;
  }

  .actual-chat {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
  }

  .chat-windows-space {
    min-width: 0;
    width: 0;
    max-width: 0;
  }

  #open-chat-button {
    position: absolute;
    right: 0.75em;
    bottom: 0.75em;
    z-index: 9999;
  }

}


/* MD */
@media (min-width: 768px) {
  .chat-windows-space {
    min-width: 21em;
    width: 21em;
    max-width: 21em;
  }

  /* Used in ternary operator <=> False Unused Warning || DON'T DELETE */
  .actual-chat {
    position: fixed;
    right: 1em;
    bottom: 0;
    width: 20em;
    min-width: 20em;
    max-width: 20em;
    max-height: 70vh;
    z-index: 99999;
  }
}


</style>
