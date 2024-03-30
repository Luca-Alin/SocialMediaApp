export interface MessageDTO {
    id: number;
    senderId: string;
    receiverId: string;
    content: string;
    dateSent: Date; // Assuming LocalDateTime maps to Date in TypeScript
    messageWasRead: boolean;
}
