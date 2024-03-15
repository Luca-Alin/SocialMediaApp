<script setup lang="ts">
import ChatComponent from "./components/ChatComponent.vue";
import NavbarComponent from "./components/NavbarComponent.vue";
import {useUserInfoStore} from "./stores/UserInfoStore";
import {storeToRefs} from "pinia";
import {ref, type Ref} from "vue";

const userStore = useUserInfoStore();
const {user} = storeToRefs(userStore);

const chatWindowIsOpen: Ref<boolean> = ref(false);
</script>

<template>
  <div id="page-content" class="d-flex flex-column">

    <header>
      <NavbarComponent/>
    </header>

    <main class="d-flex flex-row flex-fill justify-content-center overflow-hidden">

      <!-- Content - posts, settings, user profiles etc. -->
      <div :class="(chatWindowIsOpen) ?
                    'd-none d-sm-none d-md-flex' :
                    'd-flex'">
        <div style="max-width: 768px">
          <RouterView/>
        </div>
      </div>


      <!-- Chat Window Button  -->
      <div v-if="!chatWindowIsOpen" id="open-chat-button" class="d-md-none">
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

      <!-- Chat Window -->
      <div v-if="user"
           :class="(chatWindowIsOpen) ?
             'd-flex d-sm-flex flex-grow-1' :
             'd-none d-sm-none d-md-flex'"
           class="overflow-hidden"
      >

        <div class="d-flex flex-column flex-grow-1 overflow-hidden">
          <ChatComponent
              @maximize-request="() => chatWindowIsOpen = !chatWindowIsOpen"
              :chat-is-maximised="chatWindowIsOpen"
              id="actual-chat-component"
              :class="(chatWindowIsOpen) ?
                      'overflow-hidden h-100' :
                      ''"
          />
        </div>

      </div>

    </main>


  </div>
</template>

<style scoped>
#page-content {
  height: 100vh;
  min-height: 100vh;
  max-height: 100vh;
  overflow: hidden;
}

@media (min-width: 768px) {
  #actual-chat-component {
    position: fixed;
    right: 0;
    bottom: 0;
    z-index: 9999;
    max-height: 50vh;
  }
}

#open-chat-button {
  position: fixed;
  right: 1.5em;
  bottom: 1.5em;
  z-index: 9999;
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
</style>
