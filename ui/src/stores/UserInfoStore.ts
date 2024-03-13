import {defineStore} from "pinia";
import type {Ref} from "vue";
import {ref} from "vue";
import type {UserDTO} from "@/services/user-service/model/UserDTO";

export const useUserInfoStore = defineStore("UserInfo", {

    state: () => ({
        user: JSON.parse(sessionStorage.getItem("user")) as UserDTO,
        accessToken: sessionStorage.getItem("accessToken") as string | null,
        refreshToken: sessionStorage.getItem("refreshToken") as string | null
    }),
    actions: {
        setUser(user: UserDTO): void {
            sessionStorage.setItem("user", JSON.stringify(user));
            this.user = user;
        },
        setAccessToken(accessToken: string): void {
            sessionStorage.setItem("accessToken", accessToken);
            this.accessToken = accessToken;
        },
        setRefreshToken(refreshToken: string): void {
            sessionStorage.setItem("refreshToken", refreshToken);
            this.refreshToken = refreshToken;
        },
        logoutUser() {
            this.user = null;
            this.accessToken = null;
            this.refreshToken = null;

            sessionStorage.removeItem("user");
            sessionStorage.removeItem("accessToken");
            sessionStorage.removeItem("refreshToken");
        }
    }
});