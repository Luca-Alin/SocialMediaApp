import axios from "axios";
import type {UserDTO} from "./model/UserDTO";

class UserService {
    url = "/users";

    public findUserById(id : string) {
        return axios.get<UserDTO>(`${this.url}/public/${id}`);
    }

    public findUserDetails() {
        return axios.get<UserDTO>(`${this.url}/user-by-jwt`);
    }

    public patchUser(userDTO: UserDTO) {
        return axios.put<UserDTO>(`${this.url}/`, userDTO);
    }

    public searchUsers(query : string) {
        return axios.get<UserDTO[]>(`${this.url}/search?query=${query}`);
    }
}

const userService = new UserService();
export default userService;