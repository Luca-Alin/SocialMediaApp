import axios from "axios";
import {UserDTO} from "./model/UserDTO";

class UserService {
    url = "/users";

    public getUserById(id : string) {
        return axios.get<UserDTO>(`${this.url}/${id}`);
    }

    public findUserDetails() {
        return axios.get<UserDTO>(`${this.url}/user-by-jwt`);
    }
}

const userService = new UserService();
export default userService;