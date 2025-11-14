import LandingView from '@/views/LandingView.vue'
import { createRouter, createWebHistory } from 'vue-router'

const  routes = [
  { name: 'landing', path: '/', component: LandingView }, 
  { name: 'user-login', path: '/login', component: () => import('@/views/UserLoginView.vue') }, 
  { name: 'user-register', path: '/register', component: () => import('@/views/UserRegisterView.vue') }, 
  { path: '/admin/login', alias: '/admin', name: 'admin-login', component: () => import('@/views/AdminLoginView.vue') },
  { path: '/admin/dashboard', name: 'admin-dashboard', component: () => import('@/views/AdminDashboardView.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
  { path: '/admin/books/add', name: 'admin-add-book', component: () => import('@/views/AdminAddBookView.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
 // TODO: convert title to kebab-case
  { name: 'product-detail', path: '/b/:title', component: () => import('@/views/ProductDetailView.vue') }, 
  { name: 'cart', path: '/cart', component: () => import('@/views/CartView.vue') }, 
  { name: 'deals', path: '/deals', component: () => import('@/views/DealsView.vue') }, 
  { name: 'all-books', path: '/books', component: () => import('@/views/AllBooksCategoriesView.vue') }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

export default router
