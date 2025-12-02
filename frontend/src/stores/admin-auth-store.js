import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { loginUser } from '@/api/auth-api'

export const useAdminAuthStore = defineStore(
  'admin-auth',
  () => {
    const accessToken = ref('')
    const refreshToken = ref('')
    const error = ref(null)
    const isLoading = ref(false)
    const isAuthenticated = computed(() => !!accessToken.value)

    async function adminLogin(credentials) {
      isLoading.value = true
      error.value = null

      try {
        const response = await loginUser(credentials)
        const { token, refresh } = response.data

        accessToken.value = token
        refreshToken.value = refresh

        return { success: true, data: response.data }
      } catch (err) {
        error.value = err.response?.data || 'Đăng nhập thất bại'
        return { success: false, error: error.value }
      } finally {
        isLoading.value = false
      }
    }

    return {
      accessToken,
      refreshToken,
      isLoading,
      error,
      isAuthenticated,
      adminLogin
    }
  },
  {
    persist: {
      storage: localStorage,
      paths: ['accessToken'],
    },
  },
)
