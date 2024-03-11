import {LoginRequest} from "./model/LoginRequest";
import axios, {AxiosResponse} from "axios";
import {RegisterRequest} from "./model/RegisterRequest";

class AuthenticationService {
    url = "/auth";

    public login(loginRequest: LoginRequest): Promise<AxiosResponse<AuthenticationService>> {
        return axios.post(`${this.url}/login`, loginRequest);
    }

    public register(registerRequest: RegisterRequest): Promise<AxiosResponse<AuthenticationService>> {
        return axios.post(`${this.url}/register`, registerRequest);
    }

    public random(): Promise<AxiosResponse<AuthenticationService>> {
        return axios.get(`${this.url}/random`);
    }
}

const authenticationService = new AuthenticationService();
export default authenticationService;