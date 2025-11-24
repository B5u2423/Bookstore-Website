import AdminLayout from '@/layouts/AdminLayout.vue'
import EComLayout from '@/layouts/EComLayout.vue'
import { resolveDirective } from 'vue'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  //   { name: 'landing', path: '/', component: LandingView },
  //   { name: 'user-login', path: '/login', component: () => import('@/views/UserLoginView.vue') },
  //   { name: 'user-register', path: '/register', component: () => import('@/views/UserRegisterView.vue') },
  //   { name: 'admin-login', path: '/admin/login', alias: '/admin',  component: () => import('@/views/AdminLoginView.vue') },
  //   { name: 'admin-dashboard', path: '/admin/dashboard', component: () => import('@/views/AdminDashboardView.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
  //   { name: 'admin-add-book', path: '/admin/books/add',  component: () => import('@/views/AdminAddBookView.vue'), meta: { requiresAuth: true, requiresAdmin: true } },
  //  // TODO: convert title to kebab-case
  //   { name: 'product-detail', path: '/b/:title', component: () => import('@/views/ProductDetailView.vue') },
  //   { name: 'cart', path: '/cart', component: () => import('@/views/CartView.vue') },
  //   { name: 'deals', path: '/deals', component: () => import('@/views/DealsView.vue') },
  //   { name: 'category', path: '/books', component: () => import('@/views/CategoryVue.vue') }
  {
    path: '/',
    component: EComLayout,
    children: [
      { path: '', component: () => import('@/views/ecom/Landing.vue') },
      { path: 'shop', name: 'shop', component: () => import('@/views/ecom/Shop.vue') },
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
