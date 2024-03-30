import {type Ref, ref, watch} from "vue";
import type {MessageDTO} from "@/services/chat-service/model/MessageDTO";
import type {MessageRequest} from "@/services/chat-service/model/MessageRequest";
import {useUserInfoStore} from "@/stores/UserInfoStore";
import {useMessagesStore} from "@/stores/MessagesStore";
import type {UserDTO} from "@/services/user-service/model/UserDTO";


export class ChatService {
    wsUrl = import.meta.env["VITE_WS_URL"];
    public ws: WebSocket = null!;
    public webSocketIsConnected: Ref<boolean> = ref<boolean>(false);
    public retryingToConnectChat: Ref<number> = ref(0);
    private interval: NodeJS.Timeout = null!;
    private readonly userInfo;
    private readonly messageStore;

    constructor() {
        this.userInfo = useUserInfoStore();
        this.messageStore = useMessagesStore();
    }

    public async connect() {
        if (this.ws != null) throw Error("WebSocket already connected").message;

        const connectionUrl: string = `${this.wsUrl}?username=${this.userInfo.accessToken}`;
        this.ws = new WebSocket(connectionUrl);
        if (this.ws == null) {
            await this.connect();
        }


        this.ws.onopen = this.onopen.bind(this);
        this.ws.onclose = this.onclose.bind(this);
        this.ws.onmessage = this.onmessage.bind(this);
    }

    public disconnect() {
        this.ws?.close();
        this.ws = null!;
    }

    public sendMessage(receiverId: string, currentMessage: string) {
        if (this.ws == null) throw new Error("Message could not be sent");

        const messageRequest: MessageRequest = {
            content: currentMessage,
            receiverId: selectedUser
        };

        const parsedMessage: string = JSON.stringify(messageRequest);
        this.ws.send(parsedMessage);
    }

    public sendTypingNotification(userDTO: UserDTO) {
        if (this.ws == null) throw new Error("Message could not be sent");
        const messageRequest: MessageRequest = {
            content: null!,
            receiverId: userDTO.uuid
        };
        const parsedMessage: string = JSON.stringify(messageRequest);
        this.ws.send(parsedMessage);
    }

    private onopen() {
        this.webSocketIsConnected.value = true;
    }

    private onclose() {
        this.webSocketIsConnected.value = false;
        this.ws = null!;
    }

    private onmessage(message: MessageEvent<string>) {
        let parsedMessage: MessageDTO = JSON.parse(message.data);
        if (parsedMessage.content == null) {
            this.messageStore.setUserTyping(parsedMessage.senderId);
            return;
        }

        this.messageStore.addMessage(parsedMessage as MessageDTO);
    }

}

export default ChatService;