import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import HelloView from '../views/HelloView.vue'
import LoginCard from '../components/LoginCard.vue'
import RegisterCard from '../components/RegisterCard.vue'
import ProfileView from '../views/ProfileView.vue'
import ActivityTypeView from '../views/ActivityTypeView.vue'
import ActivityGroupView from '../views/ActivityGroupView.vue'
import SessionView from '../views/SessionView.vue'
import CreateSessionView from '../views/CreateSessionView.vue'
import UserBoard from '../components/UserBoard.vue'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path: '/hello',
    name: 'hello',
    component: HelloView
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
    path: "/userboard",
    component: UserBoard
  },
    {
    path: '/activity',
    redirect: '/activity/types',
    children: [
      {
        path: 'sessions',
        component: SessionView,
      },
      {
        path: 'sessions/create',
        component: CreateSessionView
      },
      {
        path: 'types',
        component: ActivityTypeView
      },
      {
        path: 'groups',
        component: ActivityGroupView
      }
    ]
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const publicPages = ['/', '/login', '/register', '/home', '/hello', '/about'];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = localStorage.getItem('user');

  if (authRequired && !loggedIn) {
    next('/login');
  } else {
    next();
  }
});

export default router
