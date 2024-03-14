import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.min.js";

import {createApp} from "vue";
import {createPinia} from "pinia";

import App from "./App.vue";
import router from "./router";

import axios from "axios";
import {useUserInfoStore} from "../src/stores/UserInfoStore";


const app = createApp(App);

app.use(createPinia());
app.use(router);

app.mount("#app");

axios.defaults.baseURL = import.meta.env.VITE_API_URL;
axios.interceptors.request.use(
    (config) => {
        const userInfoStore = useUserInfoStore();

        if (config.url?.indexOf("/public") != -1 || config.url?.indexOf("/auth") != -1)
            return config;

        const accessToken = userInfoStore.accessToken;
        if (!accessToken) {
            router.push("/login").finally();
        }

        config.headers["Authorization"] = `Bearer ${accessToken}`;
        return config;
    },
    (error) => Promise.reject(error)
);

axios.interceptors.response.use(
    (response) => response,
    (error) => Promise.reject(error)
);
