import {UserDTO} from "../user-service/model/UserDTO";
import axios, {AxiosResponse} from "axios";
import {FriendshipStatus} from "./model/FriendshipStatus";

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