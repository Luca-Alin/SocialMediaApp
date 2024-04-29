function validateEmailAndPassword(email : string, password: string) : string | null {
    const emailRegex : RegExp =  /^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i;;

    if (emailRegex.test(email) && password.length > 0) {
        return null;
    }

    console.log(email.length);
    console.log(password.length == 0);

    let errorMessage = "";

    if (password.length == 0) {
        errorMessage += "Invalid password\n";
    }

    if (!emailRegex.test(email)) {
        errorMessage += "Invalid email\n";

    }

    return errorMessage;
}

export {validateEmailAndPassword};