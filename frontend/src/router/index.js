import AdminLayout from '@/layouts/AdminLayout.vue'
import EComLayout from '@/layouts/EComLayout.vue'
import { resolveDirective } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: EComLayout,
    children: [
      { path: '', component: () => import('@/views/ecom/Landing.vue') },
      { path: 'shop', name: 'shop', component: () => import('@/views/ecom/Shop.vue') },
      { path: 'cart', name: 'cart', component: () => import('@/views/ecom/Cart.vue') },
      { path: 'register', name: 'register', component: () => import('@/views/ecom/Register.vue') },
      { path: 'login', name: 'login', component: () => import('@/views/ecom/CustomerLogin.vue') },
    ],
  },
  {
    path: '/admin',
    component: AdminLayout,
    children: [
      { path: '', redirect: { name: 'admin-login' } },
      {
        path: 'login',
        name: 'admin-login',
        component: () => import('@/views/admin/AdminLogin.vue'),
      },
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
