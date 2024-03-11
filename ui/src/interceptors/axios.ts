import axios from "axios";
import router from "@/router";

export const axiosInstance = axios.create({
    baseURL: import.meta.env.VITE_API_URL
});

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