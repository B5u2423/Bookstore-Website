import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { loginUser, logoutUser } from '@/api/auth-api'

export const useAdminAuthStore = defineStore(
  'admin-auth',
  () => {
    const accessToken = ref('')
    const refreshToken = ref('')
    const error = ref(null)
    const isLoading = ref(false)
    const isAuthenticated = computed(() => !!accessToken.value)

    function $reset() {
      accessToken.value = ''
      refreshToken.value = ''
      isLoading.value = false
      error.value = null
    }

    async function adminLogout() {
      isLoading.value = true

      try {
        await logoutUser()
      } catch (err) {
        console.warn('Logout failed on server:', err)
      } finally {
        $reset()
      }
    }

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
      adminLogin,
      adminLogout,
    }
  },
  {
    persist: {
      storage: localStorage,
      paths: ['accessToken'],
    },
  },
)
