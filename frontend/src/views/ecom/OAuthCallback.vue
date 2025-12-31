<script setup>
import router from '@/router'
import { useAuthStore } from '@/stores/auth-store'
import { onMounted } from 'vue'

const authStore = useAuthStore()
function getCookie(name) {
  return document.cookie
    .split('; ')
    .find((c) => c.startsWith(name + '='))
    ?.split('=')[1]
}

function extract() {
  debugger
  const access = getCookie('access_token')
  const refresh = getCookie('refresh_token')

  authStore.accessToken = access
  authStore.refreshToken = refresh

  document.cookie = 'access_token=; Path=/; Max-Age=0'
  document.cookie = 'refresh_token=; Path=/; Max-Age=0'

  router.push('/profile')
}

onMounted(() => {
  extract()
})
</script>

<template>Redirecting</template>
