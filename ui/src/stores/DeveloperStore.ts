import {defineStore} from "pinia";
import developerService from "../services/developer-mode-service/DeveloperService";

export const useDeveloperStore = defineStore("Developer", {
    state: () => ({
        developerMode: developerService.fetchDeveloperModeValueFromLocalStorage()
    }),
    actions: {
        setDeveloperMode() {
            this.developerMode = !this.developerMode;
            localStorage.setItem("developerMode", `${this.developerMode}`)

            developerService.setDeveloperMode(this.developerMode);
        },
    }
});