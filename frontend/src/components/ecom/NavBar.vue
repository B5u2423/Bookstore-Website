<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth-store'

const router = useRouter()
const authStore = useAuthStore()

const goToLogin = () => {
  router.push({ name: 'login' })
}

const goToRegister = () => {
  router.push({ name: 'register' })
}

const goToCart = () => {
  router.push({ name: 'cart' })
}

const goToHomePage = () => {
  router.push('/')
}

async function handleLogout() {
  await authStore.logout()
  router.push('/')
}
</script>

<template>
  <v-app-bar density="default" class="px-16">
    <v-toolbar-title @click="goToHomePage">Bookshop</v-toolbar-title>
      <v-toolbar-items>
        <v-btn @click="goToRegister">Đăng ký</v-btn>
        <v-btn @click="goToLogin">Đăng nhập</v-btn>
        <v-btn @click="goToCart" icon="mdi-cart"></v-btn>
        <v-btn icon="mdi-bell"></v-btn>
      </v-toolbar-items>
  </v-app-bar>
</template>

