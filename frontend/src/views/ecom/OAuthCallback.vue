<script setup>
import { useAuthStore } from '@/stores/auth-store'
import { onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()

function loadProfile() {
  const token = route.query.token
  const refresh = route.query.ref
  if (token && refresh) {
    authStore.handleOauthCallback(token, refresh)
    router.push({ name: 'profile' })
  }
  {
    router.push({ name: 'oauth-failed' })
  }
}

onMounted(() => {
  loadProfile()
})
</script>

<template>Redirecting</template>
