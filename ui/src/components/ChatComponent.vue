<script setup lang="ts">
import {storeToRefs} from "pinia";
import {onMounted, type Ref, ref, watch} from "vue";
import {useUserInfoStore} from "../stores/UserInfoStore";
import router from "../router";
import chatService from "../services/chat-service/ChatService";
import messageService from "../services/message-service/MessageService";
import {useMessagesStore} from "../stores/MessagesStore";
import type {UserDTO} from "../services/user-service/model/UserDTO";
import type {Conversation} from "../services/chat-service/model/Conversation";
import type {MessageRequest} from "../services/chat-service/model/MessageRequest";

const userInfo = useUserInfoStore();
const {user, accessToken, refreshToken} = storeToRefs(userInfo);


const socketIsConnected = ref(chatService.webSocketIsConnected);

const messageStore = useMessagesStore();
const {conversations} = storeToRefs(messageStore);

const selectedUser: Ref<UserDTO | null> = ref(null);
const selectedUserConversation: Ref<Conversation | null> = ref(null);


const props = defineProps({
  chatIsMaximised: Boolean
});
const emit = defineEmits(["maximize-request"]);

// TODO - reduce the boilerplatness of this code
function selectUserForConversation(user: UserDTO) {
  selectedUser.value = user;
  selectedUserConversation.value = conversations.value.find(cnv => cnv.friend.uuid == user.uuid)!;
}

const currentMessage = ref("");

function sendMessage() {
  const message: MessageRequest = {
    receiverId: selectedUser!.value!.uuid!,
    content: currentMessage.value
  };
  chatService.sendMessage(message);

  currentMessage.value = "";
}


watch(accessToken, () => {
  if (accessToken.value == null)
    router.push("/login");
});
onMounted(async () => {
  try {
    messageService.findAllConversations()
        .then(res => {
          console.log(res.data);
          messageStore.conversations = res.data;
          chatService.connect();
        })
        .catch(err => {
          console.log(err);
        });
  } catch (err) {
    console.log(err);
  }

  if (accessToken.value == null) {
    await router.push("/login");
    return;
  }
});

function handleEnter(event: KeyboardEvent) {
  if (event.shiftKey) {
    return;
  } else {
    sendMessage();
  }
}

watch(selectedUserConversation, (oldVal, newVal) => {
  if (selectedUserConversation.value == null)
    return;
  console.log("yes-1");

  const container = document.getElementById("messageContainer");
  if (container == null)
    return;
  console.log("yes-2");

  container.scrollTop = container.scrollHeight + 50;
  console.log("yes-3");
}, {
  deep: true
});
</script>

<template>
  <div class="w-100 border border-secondary bg-light overflow-hidden d-flex flex-column rounded-top-4 mt-1">

    <!-- Chat Top Bar (that can also open and close the chat) -->
    <div class="d-flex justify-content-center align-content-center">
      <button
          class="btn btn-primary text-light w-100 h-100 rounded-0"
          v-if="socketIsConnected"
          @click="emit('maximize-request', 'maximize'); chatIsMaximised = !chatIsMaximised;">
        Chat {{ (false) ? `() unread messages` : `` }}
      </button>
      <p v-else>Chat is offline</p>
    </div>

    <div v-if="chatIsMaximised"
         class="h-100 w-100 d-flex overflow-hidden"
    >

      <!-- Chat Panel -->
      <div v-if="selectedUser" class="d-flex flex-fill flex-column overflow-y-hidden">
        <div class="d-flex">
          <header class="d-flex bg-primary w-100">
            <!---->
            <!-- Leave Chat Button -->
            <button type="button" class="btn btn-primary rounded-0" @click="selectedUser = null">
              <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                   class="bi bi-arrow-left" viewBox="0 0 16 16">
                <path fill-rule="evenodd"
                      d="M15 8a.5.5 0 0 0-.5-.5H2.707l3.147-3.146a.5.5 0 1 0-.708-.708l-4 4a.5.5 0 0 0 0 .708l4 4a.5.5 0 0 0 .708-.708L2.707 8.5H14.5A.5.5 0 0 0 15 8"></path>
              </svg>
            </button>

            <!-- Friend Name and Picture -->
            <button class="btn btn-primary rounded-0 w-100">
              <img class="rounded-circle"
                   :src="`data:image/png;base64,${selectedUser?.profileImage}`"
                   width="40" height="40" alt="">
              {{ selectedUser?.firstName }} {{ selectedUser?.lastName }}
            </button>
          </header>
        </div>

        <div class="flex-fill overflow-y-scroll" id="messageContainer">
          <div v-for="message in selectedUserConversation?.messages">
            <div v-if="message.senderId == user?.uuid" class="d-flex flex-row-reverse">
              <img class="rounded-circle"
                   :src="`data:image/png;base64,${user?.profileImage}`"
                   width="40" height="40" alt="">
              <div class="d-flex flex-column rounded-2 bg-primary m-2 p-1 text-light">
                <div>
                  {{ message.content }}
                </div>
                <div class="d-flex flex-row-reverse">
                  <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="white" class="p-0 m-0 border-0">
                    <path d="M12.736 3.97a.733.733 0 0 1 1.047 0c.286.289.29.756.01 1.05L7.88 12.01a.733.733 0 0 1-1.065.02L3.217 8.384a.757.757 0 0 1 0-1.06.733.733 0 0 1 1.047 0l3.052 3.093 5.4-6.425z"/>
                  </svg>
                  <svg xmlns="http://www.w3.org/2000/svg" width="12" height="12" fill="white" class="p-0 m-0 border-0" style="margin-right: -6px;">
                    <path d="M12.736 3.97a.733.733 0 0 1 1.047 0c.286.289.29.756.01 1.05L7.88 12.01a.733.733 0 0 1-1.065.02L3.217 8.384a.757.757 0 0 1 0-1.06.733.733 0 0 1 1.047 0l3.052 3.093 5.4-6.425z"/>
                  </svg>
                </div>
              </div>
            </div>
            <!---->
            <div v-else class="d-flex">
              <img class="rounded-circle"
                   :src="`data:image/png;base64,${selectedUser?.profileImage}`"
                   width="40" height="40" alt="">
              <div class="rounded-2 bg-white m-2 p-1 text-dark border border-black border-1">
                {{ message.content }}
              </div>
            </div>
          </div>
        </div>
        <!---->
        <!-- Form for sending messages -->
        <div class="d-flex p-1">
          <form class="flex-fill d-flex" @submit.prevent>
            <textarea class="flex-fill rounded-2 m-1 custom-scrollbar" style="resize: none" v-model="currentMessage"
                      @keyup.enter.prevent="handleEnter">
            </textarea>
            <button class="btn btn-primary m-1 rounded-circle" @click="sendMessage()">
              <svg xmlns="http://www.w3.org/2000/svg" width="25" height="25" fill="currentColor" class="bi bi-send"
                   viewBox="0 0 16 16">
                <path
                    d="M15.854.146a.5.5 0 0 1 .11.54l-5.819 14.547a.75.75 0 0 1-1.329.124l-3.178-4.995L.643 7.184a.75.75 0 0 1 .124-1.33L15.314.037a.5.5 0 0 1 .54.11ZM6.636 10.07l2.761 4.338L14.13 2.576zm6.787-8.201L1.591 6.602l4.339 2.76z"/>
              </svg>
            </button>
          </form>
        </div>
      </div>
      <!---->
      <!-- All Conversations Panel -->
      <div v-else
           class="overflow-y-auto h-100 w-100 custom-scrollbar">
        <div v-for="conversation in conversations">
          <!---->
          <!-- Individual Conversation -->
          <div class="conversation-item btn d-flex align-content-center justify-content-between w-100 rounded-0"
               @click="selectUserForConversation(conversation.friend)">
            <!---->
            <div class="d-flex justify-content-center align-content-center align-items-center">
              <img class="rounded-circle me-3 d-block"
                   :src="`data:image/png;base64,${conversation.friend.profileImage}`"
                   width="40" height="40" alt="">
            </div>
            <!---->
            <div class="d-flex flex-fill flex-column align-items-start">
              <div class="fs-6">
                {{ conversation.friend.firstName }} {{ conversation.friend.lastName }}
              </div>
              <div class="d-flex justify-content-start">
                {{ messageStore.findLastMessageWithUser(conversation.friend) ?? "" }}
              </div>
            </div>
            <!---->
            <div>
              {{ messageStore.findDateOfLastMessageWithUser(conversation.friend) ?? "" }}
            </div>
          </div>
          <!---->
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.conversation-item {
  border-bottom: 0.1em solid grey;
}

.conversation-item:hover {
  background: rgb(225, 225, 225);
}

.custom-scrollbar::-webkit-scrollbar {
  width: 0.45em;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: lightgray;
  outline: 1px solid slategrey;
  border-radius: 20px;
}
</style>