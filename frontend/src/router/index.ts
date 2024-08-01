import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";
import HomeView from "../views/HomeView.vue";
import AboutView from "../views/AboutView.vue";
import LoginCard from "../components/LoginCard.vue";
import RegisterCard from "../components/RegisterCard.vue";
import ProfileView from "../views/ProfileView.vue";
import ActivityTypeView from "../views/ActivityTypeView.vue";
import ActivityGroupView from "../views/ActivityGroupView.vue";
import SessionView from "../views/SessionView.vue";
import CreateSessionView from "../views/CreateSessionView.vue";
import EditSessionView from "../views/EditSessionView.vue";
import DashboardView from "../views/DashboardView.vue";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "home",
    component: HomeView,
  },
  {
    path:'/:pathName(.*)',
    name: 'NotFound',
    component: HomeView
  },
  {
    path: "/about",
    name: "about",
    component: AboutView,
  },
  {
    path: "/login",
    component: LoginCard,
  },
  {
    path: "/register",
    component: RegisterCard,
  },
  {
    path: "/profile",
    component: ProfileView,
  },
  {
    path: "/dashboard",
    component: DashboardView,
  },
  {
    path: "/activity",
    redirect: "/activity/types",
    children: [
      {
        path: "sessions",
        component: SessionView,
      },
      {
        path: "sessions/create",
        component: CreateSessionView,
      },
      {
        path: "sessions/edit/:id",
        component: EditSessionView,
      },
      {
        path: "types",
        component: ActivityTypeView,
      },
      {
        path: "groups",
        component: ActivityGroupView,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const publicPages = ["/", "/login", "/register", "/home", "/hello", "/about"];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = localStorage.getItem("user");

  if (authRequired && !loggedIn) {
    next("/login");
  } else {
    next();
  }
});

export default router;
