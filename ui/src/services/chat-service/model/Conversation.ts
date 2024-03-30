import {MessageDTO} from "./MessageDTO";
import {UserDTO} from "../../user-service/model/UserDTO";

export interface Conversation {
    friend: UserDTO;
    friendIsTyping: number;
    messages: MessageDTO[];
}
