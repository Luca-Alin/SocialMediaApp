import "bootstrap/dist/css/bootstrap.min.css";

import {createApp} from "vue";
import {createPinia} from "pinia";

import App from "./App.vue";
import router from "./router";

import axios from "axios";


axios.defaults.baseURL = import.meta.env.VITE_API_URL;
export const axiosInstance = axios.create();
axiosInstance.interceptors.request.use(
    (config) => {
        if (config.url?.indexOf("/public"))
            return config;

        const accessToken = localStorage.getItem("accessToken");
        if (!accessToken) {
            router.push("").finally();
        }

        config.headers["Authorization"] = `Bearer ${accessToken}`;
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

axiosInstance.interceptors.response.use(
    (response) => {
        return response;
    },
    (error) => {
        return Promise.reject(error);
    }
);


const app = createApp(App);

app.use(createPinia());
app.use(router);

app.mount("#app");
