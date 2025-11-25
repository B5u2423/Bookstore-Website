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
   <v-app-bar density="default" class="px-16"
    > <v-app-bar-title class="display-inline cursor-pointer"
      > <v-responsive max-width="333" max-height="39"
        > <v-img
          src="https://theme.hstatic.net/200000845405/1001223012/14/logo.png?v=471"
          class="cursor-pointer"
          @click="goToHomePage"
        ></v-img
        > </v-responsive
      > </v-app-bar-title
    > <v-text-field
      max-width="666"
      placeholder="Search"
      variant="outlined"
      rounded
      density="comfortable"
      color="deep-purple accent-4"
      class="custom-search"
      clearable
      prepend-inner-icon="mdi-magnify"
      hide-details
    /> <template v-slot:append
      > <v-btn @click="goToRegister">Đăng ký</v-btn> <v-btn @click="goToLogin">Đăng nhập</v-btn>
      <v-btn @click="goToCart" icon="mdi-cart"></v-btn> <v-btn icon="mdi-bell"></v-btn> </template
    > </v-app-bar
  >
</template>

