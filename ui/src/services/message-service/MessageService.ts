import axios from "axios";
import type {Conversation} from "../services/chat-service/model/Conversation";

class MessageService {
    url = "/message";
    public findAllConversations() {
        return axios.get<Conversation[]>(`${this.url}/`);
    }
}

const messageService = new MessageService();
export default messageService;
