<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth-store'

const router = useRouter()
const authStore = useAuthStore()

const goToHomePage = () => {
  router.push({ name: 'landing' })
}

const goToCart = () => {
  router.push({ name: 'cart' })
}

async function handleLogout() {
  await authStore.logout()
  router.push('/')
}

const buttons = [
  { name: 'Tất cả danh mục', path: '/' },
  { name: 'Tất cả sách', path: '/books' },
  { name: 'Khuyến mãi', path: '/deals' },
]
</script>

<template>
  <v-app-bar density="compact" class="px-16">
      <v-img
        class="cursor-pointer"
        @click="goToHomePage"
        src="https://cdn.vuetifyjs.com/docs/images/brand-kit/v-logo-circle.svg"
        alt="HomePage"
        max-width="120"
      ></v-img>
      <v-spacer></v-spacer>
      <v-toolbar-items>
      </v-toolbar-items>
  </v-app-bar>
</template>

