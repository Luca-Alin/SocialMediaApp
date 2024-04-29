import {type MessageDTO} from "./MessageDTO";
import {type UserDTO} from "../../user-service/model/UserDTO";

export interface Conversation {
    friend: UserDTO;
    friendIsTyping: number;
    messages: MessageDTO[];
}
