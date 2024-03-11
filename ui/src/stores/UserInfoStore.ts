import {defineStore} from "pinia";
import type {Ref} from "vue";
import {ref} from "vue";
import type {UserDTO} from "@/services/user-service/model/UserDTO";

export const useUserInfoStore = defineStore("UserInfo", {

    state: () => ({
        user: ref(JSON.parse(sessionStorage.getItem("user"))),
        accessToken: ref(sessionStorage.getItem("accessToken")),
        refreshToken: ref(sessionStorage.getItem("refreshToken"))
    }),
    actions: {
        getUser(): Ref<UserDTO | null> {
            return _user;
        },

        setUser(user: UserDTO): void {
            sessionStorage.setItem("user", JSON.stringify(user));
            this.user = user;
        },

        getAccessToken(): Ref<string | null> {
            return accessToken;
        },

        setAccessToken(accessToken: string): void {
            sessionStorage.setItem("accessToken", accessToken);
            this.accessToken = accessToken;
        },

        getRefreshToken(): Ref<string | null> {
            return refreshToken;
        },

        setRefreshToken(refreshToken: string): void {
            sessionStorage.setItem("refreshToken", refreshToken);
            this.refreshToken = refreshToken;
        },

        logoutUser() {
            this.user.value = null;
            this.accessToken.value = null;
            this.refreshToken.value = null;

            sessionStorage.removeItem("user");
            sessionStorage.removeItem("accessToken");
            sessionStorage.removeItem("refreshToken");
        }
    }
});