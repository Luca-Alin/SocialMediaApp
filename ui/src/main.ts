import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.min.js";

import {createApp} from "vue";
import {createPinia} from "pinia";

import App from "./App.vue";
import router from "./router";

import axios from "axios";
axios.defaults.baseURL = import.meta.env.VITE_API_URL;
axios.interceptors.request.use(
    (config) => {
        console.log("axios request");

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
        return Promise.reject(error);
    }
);

axios.interceptors.response.use(
    (response) => {
        console.log("axios response");
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

