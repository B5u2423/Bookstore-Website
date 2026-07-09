import SideBar from '@/components/ecom/SideBar.vue'
import DashboardLayout from '@/layouts/DashboardLayout.vue'
import EComLayout from '@/layouts/EComLayout.vue'
import EComNoSidebar from '@/layouts/EComNoSidebar.vue'
import StandardLayout from '@/layouts/StandardLayout.vue'
import { useAdminAuthStore } from '@/stores/admin-auth-store'
import { useAuthStore } from '@/stores/auth-store'
import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    component: EComLayout,
    children: [
      {
        path: '',
        name: 'landing',
        components: {
          sidebar: SideBar,
          default: () => import('@/views/ecom/Landing.vue'),
        },
      },
      {
        path: 'register',
        name: 'register',
        components: {
          default: () => import('@/views/ecom/Register.vue'),
        },
        meta: { entrypoint: true },
      },
      {
        path: 'login',
        name: 'login',
        components: {
          default: () => import('@/views/ecom/CustomerLogin.vue'),
        },
        meta: { entrypoint: true },
      },
      {
        path: 'reset',
        name: 'reset',
        components: {
          default: () => import('@/views/ecom/ResetPassword.vue'),
        },
        meta: { entrypoint: true },
      },
      {
        path: 'profile',
        component: () => import('@/views/ecom/Profile.vue'),
        meta: { requiresAuth: true },
        children: [
          {
            path: '',
            name: 'profile-root',
            redirect: { name: 'user-info' },
          },
          {
            path: 'info',
            name: 'user-info',
            component: () => import('@/views/ecom/ProfileUserInfo.vue'),
          },
          {
            path: 'order-history',
            name: 'history',
            component: () => import('@/views/ecom/ProfileOrderHistory.vue'),
          },
        ],
      },
      {
        path: 'categories/:slug',
        name: 'category-page',
        component: () => import('@/views/ecom/CategoryShowcase.vue'),
      },
      {
        path: 'collections/:slug',
        name: 'collection-page',
        component: () => import('@/views/ecom/CollectionShowcase.vue'),
      },
      {
        path: 'oa2/callback',
        name: 'oauth2-callback',
        component: () => import('@/views/ecom/OAuthCallback.vue'),
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
    meta: { requiresAdminAuth: true },
    children: [
      { path: 'login', redirect: { name: 'admin-login' } },
      {
        path: 'dashboard',
        name: 'admin-dashboard',
        component: () => import('@/views/admin/AdminDashboard.vue'),
      },
      {
        path: 'inventory',
        name: 'inventory-root',
        redirect: { name: 'admin-dashboard' },
        children: [
          {
            path: 'books',
            name: 'i-books',
            component: () => import('@/views/admin/Book.vue'),
          },
          {
            path: 'collections',
            name: 'i-collections',
            component: () => import('@/views/admin/Collection.vue'),
          },
          {
            path: 'categories',
            name: 'i-categories',
            component: () => import('@/views/admin/Category.vue'),
          },
          {
            path: 'orders',
            name: 'i-orders',
            component: () => import('@/views/admin/Order.vue'),
          },
          {
            path: 'coupons',
            name: 'i-coupons',
            component: () => import('@/views/admin/Coupon.vue'),
          },
        ],
      },
      {
        path: 'manage',
        name: 'manage-root',
        redirect: { name: 'admin-dashboard' },
        children: [
          {
            path: 'customers',
            name: 'man-customers',
            component: () => import('@/views/admin/Customer.vue'),
          },
          {
            path: 'staffs',
            name: 'man-staffs',
            component: () => import('@/views/admin/Staff.vue'),
          },
        ],
      },
    ],
  },
  {
    path: '/books',
    component: EComNoSidebar,
    children: [
      { path: '', name: 'book-root', redirect: { name: 'landing' } },
      {
        path: ':slug/pid/:id',
        name: 'book-detail',
        component: () => import('@/views/ecom/BookDetail.vue'),
      },
    ],
  },
  {
    path: '/cart',
    component: EComNoSidebar,
    children: [
      { path: '', name: 'cart', component: () => import('@/views/ecom/Cart.vue') },
      {
        path: 'payment-callback',
        name: 'callback',
        component: () => import('@/views/ecom/PaymentCallback.vue'),
      },
      { path: 'checkout', name: 'checkout', component: () => import('@/views/ecom/Checkout.vue') },
    ],
  },
  {
    path: '/oauth-failed',
    component: EComNoSidebar,
    children: [
      { path: '', name: 'oauth-failed', component: () => import('@/views/ecom/OAuthFailed.vue') },
    ],
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/',
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return {top: 0, behavior: 'smooth'}
    }
  }
})

router.beforeEach((to) => {
  if (!to.meta.requiresAuth && !to.meta.requiresAdminAuth && !to.meta.entrypoint) {
    return true
  }
  const auth = useAuthStore()
  const adminAuth = useAdminAuthStore()

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return { path: 'login' }
  }

  // if already logged in, no route to register or login page
  if (auth.isAuthenticated && to.meta.entrypoint) {
    return { path: 'user-info' }
  }

  if (to.meta.requiresAdminAuth && !adminAuth.isAuthenticated) {
    return { path: 'admin-login' }
  }

  return true
})

export default router
