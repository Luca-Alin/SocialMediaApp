import {defineStore} from "pinia";
import type {Conversation} from "../services/chat-service/model/Conversation";
import type {MessageDTO} from "../services/chat-service/model/MessageDTO";
import {useUserInfoStore} from "../stores/UserInfoStore";
import userService from "../services/user-service/UserService";
import type {UserDTO} from "src/services/user-service/model/UserDTO";
import {formatDate} from "../services/format-date-service/FormatDateService";
import {ref} from "vue";

export const useMessagesStore = defineStore("Messages", {
    state: () => ({
        userInfoService: useUserInfoStore(),
        conversations: [] as Conversation[],
        count: 0
    }),
    actions: {
        addMessage(messageDTO: MessageDTO): void {
            console.log(messageDTO);

            const currentUserId = this.userInfoService.authenticatedUser?.uuid;

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
                this.conversations.find(conv => conv.friend.uuid == senderId)!
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

            this.conversations.sort((a, b) => {
                const aTime = new Date(a.messages[a.messages.length - 1].dateSent);
                const bTime = new Date(b.messages[b.messages.length - 1].dateSent);
                return bTime - aTime;
            });
        },
        findLastMessageWithUser(userDTO: UserDTO): string | null {
            const messages = this.conversations
                .find(conv => conv.friend.uuid == userDTO.uuid)!
                .messages;
            if (messages.length == 0) {
                return null;
            }

            const message = messages[messages.length - 1].content;
            if (message.length > 20) {
                return `${message.substring(0, 20)}...`;
            }

            return message;
        },
        findDateOfLastMessageWithUser(userDTO: UserDTO): string | null {
            const messages = this.conversations
                .find(conv => conv.friend.uuid == userDTO.uuid)!
                .messages;

            if (messages.length == 0) {
                return null;
            }

            const message = messages[messages.length - 1];
            return formatDate(message.dateSent);
        },
        findNumberOfUnreadMessages(userDTO: UserDTO): number {
            const authenticatedUser: UserDTO = this.userInfoService.authenticatedUser;
            return this.conversations
                .find(cnv => cnv.friend.uuid === userDTO.uuid)
                ?.messages
                .filter(msg => msg.receiverId === authenticatedUser.uuid)
                .filter(msg => !msg.messageWasRead)
                .length;
        },
        findTotalNumberOfUnreadMessages(): number {
            const authenticatedUser: UserDTO = this.userInfoService.authenticatedUser;

            return this.conversations
                .flatMap(cnv => cnv.messages)
                .filter(msg => msg.receiverId === authenticatedUser.uuid)
                .filter(msg => !msg.messageWasRead)
                .length;
        },
        setUserTyping(userId: string): void {
            const conversation = this.conversations.find(cnv => cnv.friend.uuid === userId);
            if (conversation) {
                conversation.friendIsTyping = Date.now();
            }
        }
    },
    getters: {
        getNumberOne : (state) => state.count * 2
    }
});