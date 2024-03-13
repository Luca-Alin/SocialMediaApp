import {defineStore} from "pinia";
import type {Conversation} from "../services/chat-service/model/Conversation";
import type {MessageDTO} from "../services/chat-service/model/MessageDTO";
import {useUserInfoStore} from "../stores/UserInfoStore";
import userService from "../services/user-service/UserService";

export const useMessagesStore = defineStore("Messages", {
    state: () => ({
        userInfoService: useUserInfoStore(),
        conversations: [] as Conversation[]
    }),
    actions: {
        addMessage(messageDTO: MessageDTO) {
            const currentUserId = this.userInfoService.user?.uuid;

            let senderId: string;
            if (currentUserId == messageDTO.senderId) {
                senderId = messageDTO.receiverId;
            } else {
                senderId = messageDTO.senderId;
            }

            const thereIsAlreadyAConversationWithThisUser = this.conversations
                .filter(conv => conv.friend.uuid == senderId)
                .length > 0;

            if (thereIsAlreadyAConversationWithThisUser) {
                this.conversations.filter(conv => conv.friend.uuid == senderId)[0]
                    .messages
                    .push(messageDTO);
            } else {
                userService.findUserById(senderId)
                    .then((res) => {
                        const senderUserDTO = res.data;
                        this.conversations.push({
                            friend: senderUserDTO,
                            messages: [messageDTO]
                        });
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            }
        }
    }
});