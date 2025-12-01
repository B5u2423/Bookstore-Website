import StandardLayout from '@/layouts/StandardLayout.vue'
import EComLayout from '@/layouts/EComLayout.vue'
import EComNoSidebar from '@/layouts/EComNoSidebar.vue'
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
        name: 'landing',
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
      {
        path: 'profile',
        component: () => import('@/views/ecom/Profile.vue'),
        children: [
          { path: '', name: 'profile-root', redirect: { name: 'user-info' } },
          {
            path: 'info',
            name: 'user-info',
            component: () => import('@/views/ecom/ProfileUserInfo.vue'),
          },
          {
            path: 'vouchers',
            name: 'vouchers',
            component: () => import('@/views/ecom/ProfileVouchers.vue'),
          },
          {
            path: 'order-history',
            name: 'history',
            component: () => import('@/views/ecom/ProfileOrderHistory.vue'),
          },
        ],
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
            path: 'authors',
            name: 'i-authors',
            component: () => import('@/views/admin/Author.vue'),
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
            path: 'publishers',
            name: 'i-publishers',
            component: () => import('@/views/admin/Publisher.vue'),
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
            component: () => import('@/views/admin/Book.vue'),
          },
          {
            path: 'staffs',
            name: 'man-staffs',
            component: () => import('@/views/admin/Book.vue'),
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
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

export default router
