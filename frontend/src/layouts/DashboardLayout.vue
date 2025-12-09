<script setup>
import AdminNavDrawer from '@/components/admin/AdminNavDrawer.vue'
import { useAdminAuthStore } from '@/stores/admin-auth-store'
import { useRouter } from 'vuetify/lib/composables/router'

const adminAuthStore = useAdminAuthStore()
const router = useRouter()

const today = new Date()
const formattedDate = new Intl.DateTimeFormat('vi', {
  weekday: 'long',
  day: '2-digit',
  month: '2-digit',
  year: 'numeric',
}).format(today)

async function handleLogout() {
  await adminAuthStore.adminLogout()
  router.push({ name: 'admin-login' })
}
</script>

<template>

  <AdminNavDrawer />

  <v-app-bar>

    <v-app-bar-title class="ml-10">{{ formattedDate }}</v-app-bar-title>

    <v-btn
      class="mr-4"
      variant="elevated"
      color="primary"
      @click="handleLogout"
    >
       Đăng xuất
    </v-btn>

  </v-app-bar>

  <v-main>

    <router-view></router-view>

  </v-main>

</template>

