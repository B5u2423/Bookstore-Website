import { useAdminAuthStore } from '@/stores/admin-auth-store'
import api, { adminApi, customerApi } from './api-config'
import { useAuthStore } from '@/stores/auth-store'

export function loginUser(loginData) {
  return api.post('/auth/login', {
    email: loginData.email,
    password: loginData.password,
  })
}

export function loginAdmin(loginData) {
  return api.post('/auth/admin/login', {
    email: loginData.email,
    password: loginData.password,
  })
}

export function registerUser(registrationData) {
  // Prevent ADMIN users from registering through regular endpoint
  if (registrationData.userType === 'ADMIN') {
    return Promise.reject({
      response: {
        data: 'Tài khoản ADMIN không thể đăng ký qua trang này. Vui lòng sử dụng trang đăng nhập dành cho quản trị viên.',
      },
    })
  }

  return api.post('/auth/register', {
    userType: registrationData.userType || 'CUSTOMER',
    name: registrationData.name,
    email: registrationData.email,
    password: registrationData.password,
  })
}

export function logoutUser(token) {
  return api.delete('/auth/logout', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}

export function getCurrentUser() {
  return api.get('/customers/account')
}

export const AuthService = {
  async resetPassword(body) {
    try {
      const res = await api.post('/auth/reset-password', body)
      return res.data
    } catch (error) {
      console.error('Error reset password', error)
    }
  },
  async refreshJwtToken(refreshToken) {
    try {
      const res = await api.post('/auth/refresh', { refreshToken })
      return res.data
    } catch {
      console.error('Error referesh token', error)
      throw error
    }
  },
}

// admin req interceptors
adminApi.interceptors.request.use(
  (config) => {
    const adminAuthStore = useAdminAuthStore()
    const token = adminAuthStore.accessToken

    // exclude refresh endpoint
    const fullUrl = `${config.baseURL ?? ''}${config.url ?? ''}`
    const isRefreshEndpoint = fullUrl.endsWith('/auth/refresh')
    if (token && !isRefreshEndpoint) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

// admin resp interceptor
adminApi.interceptors.response.use(
  (response) => response,
  async (error) => {
    const adminAuthStore = useAdminAuthStore()
    const originalRequest = error.config

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      try {
        const refreshToken = adminAuthStore.refreshToken
        if (refreshToken) {
          const response = await AuthService.refreshJwtToken(refreshToken)

          adminAuthStore.accessToken = response.token
          adminAuthStore.refreshToken = response.refresh

          originalRequest.headers.Authorization = `Bearer ${response.token}`
          return adminApi(originalRequest)
        }
      } catch (refreshError) {
        adminAuthStore.accessToken = ''
        adminAuthStore.refreshToken = ''
        window.location.href = '/admin/login'
      }
    }

    return Promise.reject(error)
  },
)

// customer req interceptors
customerApi.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore()
    const token = authStore.accessToken

    // exclude refresh endpoint
    const fullUrl = `${config.baseURL ?? ''}${config.url ?? ''}`
    const isRefreshEndpoint = fullUrl.endsWith('/auth/refresh')
    if (token && !isRefreshEndpoint) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

// customer resp interceptor
customerApi.interceptors.response.use(
  (response) => response,
  async (error) => {
    const authStore = useAuthStore()
    const originalRequest = error.config

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      try {
        const refreshToken = authStore.refreshToken
        if (refreshToken) {
          const response = await AuthService.refreshJwtToken(refreshToken)

          authStore.accessToken = response.token
          authStore.refreshToken = response.refresh

          originalRequest.headers.Authorization = `Bearer ${response.token}`
          return customerApi(originalRequest)
        }
      } catch (refreshError) {
        authStore.accessToken = ''
        authStore.refreshToken = ''
        window.location.href = '/login'
      }
    }

    return Promise.reject(error)
  },
)
