import axios, {type AxiosResponse} from "axios";
import type {Conversation} from "@/services/chat-service/model/Conversation";
import type {UserDTO} from "@/services/user-service/model/UserDTO";

class MessageService {
    url = "/message";
    public findAllConversations() {
        return axios.get<Conversation[]>(`${this.url}/`);
    }

    public readConversation(sender : UserDTO) : Promise<AxiosResponse<void>> {
        return axios.post<void>(`${this.url}/read`, sender);
    }
}

const messageService = new MessageService();
export default messageService;
