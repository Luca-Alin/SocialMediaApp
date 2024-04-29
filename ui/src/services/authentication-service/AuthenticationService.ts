import {type LoginRequest} from "./model/LoginRequest";
import axios, {type AxiosResponse} from "axios";
import {type RegisterRequest} from "./model/RegisterRequest";
import type {AuthenticationResponse} from "src/services/authentication-service/model/AuthenticationResponse";

class AuthenticationService {
    url = "/auth";

    public login(loginRequest: LoginRequest) {
        return axios.post<AuthenticationResponse>(`${this.url}/login`, loginRequest);
    }

    public register(registerRequest: RegisterRequest) {
        return axios.post<AuthenticationResponse>(`${this.url}/register`, registerRequest);
    }

    public random() {
        return axios.get<AuthenticationResponse>(`${this.url}/random`);
    }

    public refresh(refreshToken: string): Promise<AxiosResponse<AuthenticationResponse>> {
        return axios.post<AuthenticationResponse>(`${this.url}/refresh-token`, null, {
            headers: {
                Authorization: `Bearer ${refreshToken}`
            }
        });
    }
}

const authenticationService = new AuthenticationService();
export default authenticationService;