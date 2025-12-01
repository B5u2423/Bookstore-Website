import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAdminAuthStore = defineStore('admin-auth', () => {
  const isLoggedIn = ref(false)
})
