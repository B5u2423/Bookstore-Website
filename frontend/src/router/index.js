import LandingView from '@/views/LandingView.vue'
import { createRouter, createWebHistory } from 'vue-router'

const  routes = [
 { name: 'landing', path: '/', component: LandingView }, 
 { name: 'user-login', path: '/login', component: () => import('@/views/UserLoginView.vue') }, 
 { name: 'user-register', path: '/register', component: () => import('@/views/UserRegisterView.vue') }, 
 { name: 'admin-login', path: '/admin/login', alias: '/admin', component: () => import('@/views/AdminLoginView.vue') }, 
 { name: 'product-detail', path: '/b/:id', component: () => import('@/views/ProductDetailView.vue') }, 
 { name: 'cart', path: '/cart', component: () => import('@/views/CartView.vue') }, 
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
