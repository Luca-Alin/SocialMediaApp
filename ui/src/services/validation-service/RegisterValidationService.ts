import type {RegisterRequest} from "src/services/authentication-service/model/RegisterRequest";

function validateRegisterRequest(registerRequest: RegisterRequest): Map<string, string> | null {
    const emailRegex: RegExp = /^(([^<>()[\].,;:\s@"]+(\.[^<>()[\].,;:\s@"]+)*)|(".+"))@(([^<>()[\].,;:\s@"]+\.)+[^<>()[\].,;:\s@"]{2,})$/i;
    const errorMessages : Map<string, string> = new Map();

    const firstName = registerRequest.firstName;
    const lastName = registerRequest.lastName;
    const email = registerRequest.email;
    const password = registerRequest.password;

    if (firstName.length == 0) errorMessages.set("firstName", "Invalid first name");
    if (lastName.length == 0) errorMessages.set("lastName", "Invalid last name");
    if (!emailRegex.test(email)) errorMessages.set("email", "Invalid email");
    if (password.length == 0) errorMessages.set("password", "Invalid password");

    return errorMessages.size == 0
        ? null
        : errorMessages;
}

export {validateRegisterRequest};