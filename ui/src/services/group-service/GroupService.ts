import axios, {AxiosResponse} from "axios";
import type {Group} from "@/services/group-service/model/Group";
import type {GroupJoinStatus} from "@/services/group-service/model/enums/GroupJoinStatus";

class GroupService {
    url = "/group";

    createGroup(group: Group) {
        return axios.post<void>(`${this.url}/`, group);
    }

    findAllGroups() : Promise<AxiosResponse<Group[]>> {
        return axios.get<Group[]>(`${this.url}/all`);
    }

    findGroupById(id : string) : Promise<AxiosResponse<Group>> {
        return axios.get<Group>(`${this.url}/${id}`);
    }

    findGroupJoinStatus(id : string) : Promise<AxiosResponse<GroupJoinStatus>> {
        return axios.get<GroupJoinStatus>(`${this.url}/group-join-status/${id}`);
    }
}

const groupService = new GroupService();
export default groupService;