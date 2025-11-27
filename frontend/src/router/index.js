import StandardLayout from '@/layouts/StandardLayout.vue'
import EComLayout from '@/layouts/EComLayout.vue'
import SideBar from '@/components/ecom/SideBar.vue'
import DashboardLayout from '@/layouts/DashboardLayout.vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: EComLayout,
    children: [
      {
        path: '',
        components: {
          sidebar: SideBar,
          default: () => import('@/views/ecom/Landing.vue'),
        },
      },
      {
        path: 'shop',
        name: 'shop',
        components: {
          default: () => import('@/views/ecom/Shop.vue'),
        },
      },
      {
        path: 'cart',
        name: 'cart',
        components: {
          default: () => import('@/views/ecom/Cart.vue'),
        },
      },
      {
        path: 'register',
        name: 'register',
        components: {
          default: () => import('@/views/ecom/Register.vue'),
        },
      },
      {
        path: 'login',
        name: 'login',
        components: {
          default: () => import('@/views/ecom/CustomerLogin.vue'),
        },
      },
    ],
  },
  {
    path: '/admin-login',
    component: StandardLayout,
    children: [
      {
        path: '',
        name: 'admin-login',
        component: () => import('@/views/admin/AdminLogin.vue'),
      },
    ],
  },
  {
    path: '/admin',
    component: DashboardLayout,
    children: [
      { path: 'login', redirect: { name: 'admin-login' } },
      {
        path: 'dashboard',
        name: 'admin-dashboard',
        component: () => import('@/views/admin/AdminDashboard.vue'),
      },
    ],
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
