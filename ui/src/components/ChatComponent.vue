<script setup lang="ts">
import {storeToRefs} from "pinia";
import {onMounted, type Ref, ref, watch} from "vue";
import {useUserInfoStore} from "../stores/UserInfoStore";
import router from "../router";
import chatService from "../services/chat-service/ChatService";
import messageService from "../services/message-service/MessageService";
import {useMessagesStore} from "../stores/MessagesStore";
import type {UserDTO} from "../services/user-service/model/UserDTO";

const userInfo = useUserInfoStore();
const {user, accessToken, refreshToken} = storeToRefs(userInfo);


const socketIsConnected = ref(chatService.webSocketIsConnected);

const messageStore = useMessagesStore();
const {conversations} = storeToRefs(messageStore);

const selectedUser: Ref<UserDTO | null> = ref(null);

const props = defineProps({
  chatIsMaximised: Boolean
});
const emit = defineEmits(["maximize-request"]);

// TODO - reduce the boilerplatness of this code
watch(accessToken, () => {
  if (accessToken.value == null)
    router.push("/login");
});
onMounted(() => {
  try {
    messageService.findAllConversations()
        .then(res => {
          console.log(res.data);
          messageStore.conversations = res.data;
        })
        .catch(err => {
          console.log(err);
        });
    chatService.connect();
  } catch (err) {
    console.log(err);
  }

  if (accessToken.value == null) {
    router.push("/login");
    return;
  }
});
</script>

<template>
  <div class="border border-primary bg-light overflow-hidden">

    <!-- Chat Top Bar (that can also open and close the chat) -->
    <div class="d-flex justify-content-center align-content-center">
      <button
          class="btn btn-outline-primary w-100 h-100 rounded-0"
          v-if="socketIsConnected"
          @click="/*chatIsMaximised = !chatIsMaximised;*/
          emit('maximize-request', 'maximize');
          chatIsMaximised = !chatIsMaximised
          "
      >Chat is live
      </button>
      <p v-else>Chat is offline</p>
    </div>

    <div v-if="chatIsMaximised"
        class="overflow-hidden h-100 w-100"
    >

      <!-- Chat Panel -->
      <div v-if="selectedUser">
        <div class="d-flex">
          <header class="d-flex bg-primary w-100">
            <button type="button" class="btn btn-primary" @click="selectedUser = null">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                   class="bi bi-arrow-left" viewBox="0 0 16 16">
                <path fill-rule="evenodd"
                      d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8"></path>
              </svg>
            </button>
            <button class="btn btn-primary w-100">
              {{ selectedUser?.firstName }} {{ selectedUser?.lastName }}
            </button>
          </header>
        </div>

        <div class="d-flex">
          <input type="text">
          <button @click="">Send</button>
        </div>
      </div>

      <!-- All Conversations Panel -->
      <div v-else
           class="overflow-y-scroll h-100 w-100"
      >
        <div
            v-for="conversation in conversations"
        >
          <button class="btn" @click="selectedUser = conversation.friend">
            {{ conversation.friend.firstName }} {{ conversation.friend.lastName }}
          </button>
        </div>
      </div>


    </div>
  </div>
</template>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 0.4em;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  padding-right: 5px;
  background-color: lightgray;
  outline: 1px solid slategrey;
  border-radius: 20px;
}
</style>