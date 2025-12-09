import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { loginUser, registerUser, logoutUser, getCurrentUser } from '@/api/auth-api'

export const useAuthStore = defineStore(
  'auth',
  () => {
    const accessToken = ref('')
    const refreshToken = ref('')
    const isLoading = ref(false)
    const error = ref(null)
    const isAuthenticated = computed(() => !!accessToken.value)

    function $reset() {
      accessToken.value = ''
      refreshToken.value = ''
      isLoading.value = false
      error.value = null
    }

    async function login(credentials) {
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

    async function register(registrationData) {
      isLoading.value = true
      error.value = null

      try {
        const response = await registerUser(registrationData)
        const { createdUser } = response.data

        return { success: true, data: createdUser }
      } catch (err) {
        error.value = err.response?.data || 'Đăng ký thất bại'
        return { success: false, error: error.value }
      } finally {
        isLoading.value = false
      }
    }

    async function logout() {
      isLoading.value = true

      try {
        await logoutUser(accessToken.value)
      } catch (err) {
        console.warn('Logout failed on server:', err)
      } finally {
        $reset()
      }
    }

    return {
      accessToken,
      refreshToken,
      isLoading,
      error,
      isAuthenticated,
      login,
      register,
      logout,
    }
  },
  {
    persist: {
      storage: sessionStorage,
      paths: ['accessToken'],
    },
  },
)
