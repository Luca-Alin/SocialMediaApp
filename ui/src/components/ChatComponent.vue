<script setup lang="ts">
import {createPinia, storeToRefs} from "pinia";
import {createApp, onMounted, type Ref, ref, watch} from "vue";
import App from "../App.vue";
import {useUserInfoStore} from "../stores/UserInfoStore";
import router from "../router";
import chatService from "../services/chat-service/ChatService";
import messageService from "../services/message-service/MessageService";
import {useMessagesStore} from "../stores/MessagesStore";

const pinia = createPinia();
const app = createApp(App);
app.use(pinia);
const userInfo = useUserInfoStore();
const {user, accessToken, refreshToken} = storeToRefs(userInfo);

const chatIsMaximised = ref(false);

const socketIsConnected = ref(chatService.webSocketIsConnected);

const messageStore = useMessagesStore();
const {conversations} = storeToRefs(messageStore);

// TODO - reduce the boilerplatness of this code
watch(accessToken, () => {
  if (accessToken.value == null)
    router.push("/login");
});
onMounted(() => {

  try {
    chatService.connect();
    messageService.findAllConversations()
        .then(res => {
          messageStore.conversations = res.data;
        })
        .catch(err => {
          console.log(err)
        })
  }
  catch (err) {
    console.log(err);
  }

  if (accessToken.value == null) {
    router.push("/login");
    return;
  }
});

</script>

<template>
  <div class="border border-primary border-2 overflow-y-scroll">
    <div class="d-flex justify-content-center align-content-center">
      <button
          class="btn btn-primary w-100 h-100 rounded-0"
          v-if="socketIsConnected" @click="chatIsMaximised = !chatIsMaximised"
      >Chat is live</button>
      <p v-else>Chat is offline</p>
    </div>

    <div  class="container" v-if="chatIsMaximised">
      <div v-for="conversation in conversations">
        <div>{{ conversation.friend.firstName }} {{ conversation.friend.lastName }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>