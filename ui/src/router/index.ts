import {createRouter, createWebHistory} from "vue-router";
import NotFoundView from "@/views/NotFoundView.vue";
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import PostsView from "@/views/PostsView.vue";
import SettingsView from "@/views/SettingsView.vue";
import UserDetailsView from "@/views/UserDetailsView.vue";
import {useUserInfoStore} from "@/stores/UserInfoStore";
import CreateGroupView from "@/views/CreateGroupView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: "/login",
            component: LoginView
        },
        {
            path: "/register",
            component: RegisterView
        },
        {
            path: "/posts",
            component: PostsView
        },
        {
            path: "/settings",
            component: SettingsView
        },
        {
            path: "/user/:id",
            name: "User",
            component: UserDetailsView
        },
        {
            path: "/create-group",
            name: "CreateGroup",
            component: CreateGroupView
        },
        {
            path: "/",
            redirect: "/posts"
        },
        {
            path: "/:pathMatch(.*)*",
            name: "NotFound",
            component: NotFoundView
        }
    ]
});

router.beforeEach((to) => {
    const store = useUserInfoStore();
});

export default router;
