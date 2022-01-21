import { createRouter, createWebHistory } from "vue-router"
const routerHistory = createWebHistory()
const routers = [
  { path: "/", redirect: "/home" },
  {
    path: "/home",
    component: () => import('@/views/home/index'),
  },
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/free',
    component: () => import('@/views/free/index'),
    hidden: true
  }
]

const router = createRouter({
  history: routerHistory,
  routes: routers
})

export default router;