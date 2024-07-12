import { createRouter, createWebHashHistory, RouteRecordRaw } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import HelloView from '../views/HelloView.vue'
import LoginCard from '../components/LoginCard.vue'
import RegisterCard from '../components/RegisterCard.vue'
import ProfileView from '../views/ProfileView.vue'
import ExerciseView from '../views/ExerciseView.vue'
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
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
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
    path: "/exercises",
    component: ExerciseView,
  },
  {
    path: "/userboard",
    component: UserBoard
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
