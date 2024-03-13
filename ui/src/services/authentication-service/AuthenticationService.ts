import {LoginRequest} from "./model/LoginRequest";
import axios, {AxiosResponse} from "axios";
import {RegisterRequest} from "./model/RegisterRequest";
import type {AuthenticationResponse} from "@/services/authentication-service/model/AuthenticationResponse";

class AuthenticationService {
    url = "/auth";

    public login(loginRequest: LoginRequest) {
        return axios.post<AuthenticationResponse>(`${this.url}/login`, loginRequest);
    }

    public register(registerRequest: RegisterRequest) {
        return axios.post<AuthenticationResponse>(`${this.url}/register`, registerRequest);
    }

    public random(){
        return axios.get<AuthenticationResponse>(`${this.url}/random`);
    }
}

const authenticationService = new AuthenticationService();
export default authenticationService;