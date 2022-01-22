import { createRouter, createWebHistory } from "vue-router"

const routers = [
  { path: "/", redirect: "/home" },
  {
    path: "/home",
    component: () => import('@/views/home/index'),
  },
  {
    path: '/login',
    component: () => import('@/views/auth/login'),
    hidden: true
  },
  {
    path: '/logout',
    component: () => import('@/views/auth/logout'),
    hidden: true
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: routers
})

export default router;