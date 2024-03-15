import {defineStore} from "pinia";
import type {UserDTO} from "@/services/user-service/model/UserDTO";

export const _ACCESS_TOKEN = "access_token";
export const _REFRESH_TOKEN = "refresh_token";
export const _USER_INFO = "user_info";
export const _REMEMBER_ME = "remember_me";

export type UserInfoState = {
    user: UserDTO | null;
    accessToken: string | null;
    refreshToken: string | null;
    rememberMe: boolean;
}
export const useUserInfoStore = defineStore("UserInfo", {
    state: () => ({
        user: JSON.parse(
            sessionStorage.getItem(_USER_INFO) ?? localStorage.getItem(_USER_INFO) ?? null
        ),
        accessToken: sessionStorage.getItem(_ACCESS_TOKEN) ?? localStorage.getItem(_ACCESS_TOKEN) ?? null,
        refreshToken: sessionStorage.getItem(_REFRESH_TOKEN) ?? localStorage.getItem(_REFRESH_TOKEN) ?? null,
        rememberMe:  localStorage.getItem(_REMEMBER_ME) === "true"
    } as UserInfoState),
    actions: {
        setUser(user: UserDTO): void {
            const stringify = JSON.stringify(user);
            sessionStorage.setItem(_USER_INFO, stringify);
            if (this.rememberMe) {
                localStorage.setItem(_USER_INFO, stringify);
            }
            this.user = user;
        },
        setAccessToken(accessToken: string): void {
            this.accessToken = accessToken;
            sessionStorage.setItem(_ACCESS_TOKEN, accessToken);
            if (this.rememberMe) {
                localStorage.setItem(_ACCESS_TOKEN, accessToken);
            }
        },
        setRefreshToken(refreshToken: string): void {
            this.refreshToken = refreshToken;
            sessionStorage.setItem(_REFRESH_TOKEN, refreshToken);
            if (this.rememberMe) {
                localStorage.setItem(_REFRESH_TOKEN, refreshToken);
            }
        },
        toggleRememberMe() {
            this.rememberMe = !this.rememberMe;
            localStorage.setItem(_REMEMBER_ME, `${this.rememberMe}`);
        },
        logoutUser() {
            this.user = null;
            this.accessToken = null;
            this.refreshToken = null;

            sessionStorage.removeItem(_USER_INFO);
            sessionStorage.removeItem(_ACCESS_TOKEN);
            sessionStorage.removeItem(_REFRESH_TOKEN);

            localStorage.removeItem(_USER_INFO);
            localStorage.removeItem(_ACCESS_TOKEN);
            localStorage.removeItem(_REFRESH_TOKEN);
            localStorage.removeItem(_REMEMBER_ME);
        }
    }
});