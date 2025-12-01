<script setup>
import AdminNavDrawer from '@/components/admin/AdminNavDrawer.vue'

const today = new Date()
const formattedDate = new Intl.DateTimeFormat('vi', {
  weekday: 'long',
  day: '2-digit',
  month: '2-digit',
  year: 'numeric',
}).format(today)
</script>

<template>

  <AdminNavDrawer />

  <v-app-bar :title="formattedDate"></v-app-bar>

  <v-main>

    <router-view></router-view>

  </v-main>

</template>

