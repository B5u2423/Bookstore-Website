<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth-store'

const router = useRouter()
const authStore = useAuthStore()

const goToHomePage = () => {
  router.push('/')
}

async function handleLogout() {
  await authStore.logout()
  router.push('/')
}
</script>

<template>

  <v-app-bar
    density="default"
    class="px-16"
  >

    <v-app-bar-title class="display-inline">

      <v-responsive
        max-width="333"
        max-height="39"
      >

        <v-img
          src="https://theme.hstatic.net/200000845405/1001223012/14/logo.png?v=471"
          class="cursor-pointer"
          @click="goToHomePage"
        ></v-img>

      </v-responsive>

    </v-app-bar-title>

    <v-text-field
      max-width="666"
      placeholder="Search"
      variant="outlined"
      rounded
      density="comfortable"
      class="custom-search"
      clearable
      prepend-inner-icon="mdi-magnify"
      hide-details
    />

    <template v-slot:append>

      <v-btn :to="{ name: 'register' }">Đăng ký</v-btn>

      <v-btn :to="{ name: 'login' }">Đăng nhập</v-btn>

      <v-btn
        :to="{ name: 'cart' }"
        icon="mdi-cart"
      ></v-btn>

      <v-btn icon="mdi-bell"></v-btn>

    </template>

  </v-app-bar>

  <v-app-bar
    class="bg-blue px-16"
    scroll-behavior="fully-hide"
    scroll-threshold="30"
    height="50"
  >

    <div class="ml-16">

      <v-btn>Sách </v-btn>

      <v-btn>Nổi bật</v-btn>

      <v-btn>Khuyến mãi</v-btn>

    </div>

    <v-spacer></v-spacer>

    <div class="mr-16 d-inline-flex align-center justify-center">

      <v-icon
        class="mx-1"
        icon="mdi-phone"
      ></v-icon>

      <h3>Hotline: 0923550726</h3>

    </div>

  </v-app-bar>

</template>

