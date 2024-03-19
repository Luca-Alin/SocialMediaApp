import {useUserInfoStore} from "@/stores/UserInfoStore";
import {Ref, ref} from "vue";
import type {MessageRequest} from "@/services/chat-service/model/MessageRequest";
import type {MessageDTO} from "src/services/chat-service/model/MessageDTO";
import {useMessagesStore} from "/src/stores/MessagesStore";

export class ChatService {
    wsUrl = import.meta.env.VITE_WS_URL;
    public ws: WebSocket | null = null;
    public webSocketIsConnected: Ref<boolean> = ref<boolean>(false);

    constructor() {
    }

    public async connect() {
        if (this.ws != null) throw Error("WebSocket already connected").message;


        const userInfo = useUserInfoStore();
        if (userInfo.accessToken == null) throw new Error("Access token is undefined").message;


        const connectionUrl = `${this.wsUrl}?username=${userInfo.accessToken}`;
        this.ws = new WebSocket(connectionUrl);
        if (this.ws == null) {
            setTimeout(() => {
                this.connect();
            }, 1500);
            throw new Error("WebSocket couldn't connect to the server").message;
        }


        this.ws.onopen = this.onopen.bind(this);
        this.ws.onclose = this.onclose.bind(this);
        this.ws.onmessage = this.onmessage.bind(this);
    }

    private onmessage(message: MessageEvent<MessageDTO>) {
        const messageStore = useMessagesStore();
        const messageDTO: MessageDTO = JSON.parse(message.data);
        messageStore.addMessage(messageDTO);


    }
    public disconnect() {
        this.ws?.close();
        this.ws = null;
    }

    public sendMessage(messageRequest: MessageRequest) {
        if (this.ws == null) throw new Error(messageRequest);

        const parsedMessage = JSON.stringify(messageRequest);
        this.ws.send(parsedMessage);
    }

    private onopen() {
        this.webSocketIsConnected.value = true;
        console.log("WebSocket connected", this.webSocketIsConnected);
    }

    private onclose() {
        this.webSocketIsConnected.value = false;
        this.ws = null;
        console.log("WebSocket disconnected", this.webSocketIsConnected);
    }

}

const chatService = new ChatService();
export default chatService;