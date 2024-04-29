import {FriendshipStatus} from "./model/FriendshipStatus";
import type {UserDTO} from "@/services/user-service/model/UserDTO";
import axios, {type AxiosResponse} from "axios";

class FriendshipService {
    url = "/friendships";
    public sendFriendshipRequests(user: UserDTO) {
        return axios.post<FriendshipStatus>(`${this.url}/send`, user);
    }

    public acceptFriendshipRequests(user: UserDTO) {
        return axios.post<FriendshipStatus>(`${this.url}/accept`, user);
    }

    public friendshipStatus(user: UserDTO) : Promise<AxiosResponse<FriendshipStatus>> {
        return axios.post<FriendshipStatus>(`${this.url}/friendship-status`, user);
    }
}

const friendshipService = new FriendshipService();
export default friendshipService;