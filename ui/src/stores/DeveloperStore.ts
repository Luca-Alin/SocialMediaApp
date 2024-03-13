import {defineStore} from "pinia";

export const useDeveloperStore = defineStore("Developer", {
    state: () => ({
        developerMode: localStorage.getItem("developerMode") ?? false
    }),
    actions: {
        setDeveloperMode() {
            this.developerMode = !this.developerMode;
            localStorage.setItem("developerMode", `${this.developerMode}`)

            const head = document.querySelector("head");

            for (let i = 0; i < head!.children.length; i++) {
                const element = head!.children.item(i);
                if (element!.id == "developer-css") {
                    console.log(this.developerMode);
                    if (this.developerMode == true) {
                        element!.setAttribute("href", "src/assets/developer-stylesheet.css");
                    } else {
                        element!.setAttribute("href", "");
                    }
                }
            }
        }
    }
});