import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.min.js";

import {createApp} from "vue";
import {createPinia} from "pinia";

import App from "./App.vue";
import router from "./router";

import axios from "axios";
import authenticationService from "../src/services/authentication-service/AuthenticationService";
import {useUserInfoStore} from "../src/stores/UserInfoStore";

axios.defaults.baseURL = import.meta.env.VITE_API_URL;
axios.interceptors.request.use(
    (config) => {
        console.log("axios.interceptors.request.config");

        if (config.url?.indexOf("/public") != -1 || config.url?.indexOf("/auth") != -1)
            return config;

        const accessToken = sessionStorage.getItem("access_token");
        if (!accessToken) {
            router.push("").finally();
        }

        config.headers["Authorization"] = `Bearer ${accessToken}`;
        return config;
    },
    (error) => {
        console.log("axios.interceptors.request.error");
        return Promise.reject(error);
    }
);

axios.interceptors.response.use(
    (response) => {
        console.log("axios.interceptors.response.response");
        return response;
    },
    async (error) => {
        console.log("axios.interceptors.response.error", error);
        const userStore = useUserInfoStore();

        const errorUrl: string = error.config.url;
        if (errorUrl.indexOf("/refresh-token") != -1) {
            await router.push("/login");
            userStore.logoutUser();
            return Promise.reject(error);
        }

        const prevRequest = error.config;
        if (error.response.status === 401 && !prevRequest.sent) {
            prevRequest.sent = true;

            const response = await authenticationService.refresh(userStore.refreshToken);
            userStore.setAccessToken(response.data.accessToken);
            userStore.setRefreshToken(response.data.refreshToken);
            prevRequest.headers["Authorization"] = `Bearer ${userStore.accessToken}`;

            return axios(prevRequest);
        }

        return Promise.reject(error);
    }
);


const app = createApp(App);

app.use(createPinia());
app.use(router);

app.mount("#app");

