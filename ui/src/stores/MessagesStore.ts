import {defineStore} from "pinia";
import type {Conversation} from "../services/chat-service/model/Conversation";
import type {MessageDTO} from "../services/chat-service/model/MessageDTO";
import {useUserInfoStore} from "../stores/UserInfoStore";
import userService from "../services/user-service/UserService";
import type {UserDTO} from "src/services/user-service/model/UserDTO";

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
        },
        findLastMessageWithUser(userDTO: UserDTO): string | null {
            const messages = this.conversations
                .find(conv => conv.friend.uuid == userDTO.uuid)!
                .messages;
            if (messages.length == 0) {
                return null;
            }

            const message = messages[messages.length - 1].content;
            if (message.length > 30) {
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
            return this.formatDate(message.dateSent);
        },
        parseISOString(dateString: string): Date {
            return new Date(dateString);
        },
        formatDate(date: Date): string {
            date = this.parseISOString(date);

            const today = new Date();
            const currentDay = today.getDate();
            const currentMonth = today.getMonth();
            const currentYear = today.getFullYear();

            const inputDay = date.getDate();
            const inputMonth = date.getMonth();
            const inputYear = date.getFullYear();

            if (inputYear === currentYear && inputMonth === currentMonth && inputDay === currentDay) {
                // Date is today
                const hours = date.getHours().toString().padStart(2, "0");
                const minutes = date.getMinutes().toString().padStart(2, "0");
                return `${hours}:${minutes}`;
            } else if (inputYear === currentYear && inputMonth === currentMonth && inputDay >= currentDay - 7) {
                // Date is from this week
                const daysOfWeek = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
                return daysOfWeek[date.getDay()].slice(0, 3);
            } else if (inputYear === currentYear && inputMonth === currentMonth && inputDay < currentDay - 7) {
                // Date is from the previous week
                const dayOfMonth = inputDay.toString();
                const monthName = new Intl.DateTimeFormat("en-US", {month: "long"}).format(date);
                return `${dayOfMonth} ${monthName}`;
            } else {
                // For other cases (e.g., dates from a month ago, last year, etc.), you can define your own logic here
                return date.toDateString(); // Default format for other cases
            }
        }
    }
});