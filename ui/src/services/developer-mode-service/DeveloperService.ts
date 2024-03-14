class DeveloperService {

    fetchDeveloperModeValueFromLocalStorage()  {
        const value = localStorage.getItem("developerMode") == "true" ? true : false ?? false;
        this.setDeveloperMode(value);
        return value;
    }

    setDeveloperMode(value : boolean) {
        const indexHeadElement = document.querySelector("head");
        for (let i = 0; i < indexHeadElement!.children.length; i++) {
            const element = indexHeadElement!.children.item(i);
            if (element!.id == "developer-css") {
                if (value) {
                    element!.setAttribute("href", "src/assets/developer-stylesheet.css");
                } else {
                    element!.setAttribute("href", "");
                }
            }
        }
    }

    getTokenExpirationTime(token: string | null): number | null {
        if (token == null)
            return null;
        const tokenPayload = atob(token.split(".")[1]);
        const decodedPayload = JSON.parse(tokenPayload);
        const currentTime = Math.floor(Date.now() / 1000);
        return decodedPayload.exp - currentTime;
    }
}

const developerService = new DeveloperService();
export default developerService;