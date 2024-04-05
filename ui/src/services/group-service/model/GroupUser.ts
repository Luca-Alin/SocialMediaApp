import type {GroupRank} from "@/services/group-service/model/enums/GroupRank";
import type {UserDTO} from "@/services/user-service/model/UserDTO";

export interface GroupUser {
    id : number;
    groupRank: GroupRank;
    user: UserDTO;
}