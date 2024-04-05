import type {GroupUser} from "@/services/group-service/model/GroupUser";
import type {WhoCanCreatePosts} from "@/services/group-service/model/enums/WhoCanCreatePosts";
import type {WhoCanSeePosts} from "@/services/group-service/model/enums/WhoCanSeePosts";
import type {HowToJoin} from "@/services/group-service/model/enums/HowToJoin";
import type {GroupPost} from "@/services/group-service/model/GroupPost";

export interface Group {
    id: string;
    groupName: string;
    groupDescription: string;
    groupImage: string;
    whoCanCreatePosts: WhoCanCreatePosts;
    whoCanSeePosts: WhoCanSeePosts;
    howToJoin: HowToJoin;
    groupUsers: GroupUser[];
    groupPosts: GroupPost[];
    bannedUsers: GroupUser[];
}