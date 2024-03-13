export interface UserDTO {
    uuid: string | null;
    firstName: string | null;
    lastName: string | null;
    bio: string | null;
    email: string | null;
    profileImage: ArrayBuffer | null;
}