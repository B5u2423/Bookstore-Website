import api from './api-config'

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('accessToken')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true

      try {
        const refreshToken = localStorage.getItem('refreshToken')
        if (refreshToken) {
          const response = await refreshJwtToken(refreshToken)
          const { token, refresh } = response.data

          localStorage.setItem('accessToken', token)
          localStorage.setItem('refreshToken', refresh)

          originalRequest.headers.Authorization = `Bearer ${token}`
          return api(originalRequest)
        }
      } catch (refreshError) {
        localStorage.removeItem('accessToken')
        localStorage.removeItem('refreshToken')
        localStorage.removeItem('user')
        window.location.href = '/login'
      }
    }

    return Promise.reject(error)
  },
)

export function loginUser(loginData) {
  return api.post('/api/v1/auth/login', {
    email: loginData.email,
    password: loginData.password,
  })
}

export function loginAdmin(loginData) {
  return api.post('/api/v1/auth/admin/login', {
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

  return api.post('/api/v1/auth/register', {
    userType: registrationData.userType || 'CUSTOMER',
    firstName: registrationData.firstName,
    lastName: registrationData.lastName,
    email: registrationData.email,
    password: registrationData.password,
  })
}

export function refreshJwtToken(refreshToken) {
  return api.get('/api/v1/auth/refresh', {
    data: { refreshToken },
  })
}

export function logoutUser(token) {
  return api.delete('/api/v1/auth/logout', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}

export function getCurrentUser() {
  return api.get('/api/v1/customers/account')
}

export const AuthService = {
  async resetPassword(body) {
    try {
      const res = await api.post('/api/v1/auth/reset-password', body)
      return res.data
    } catch (error) {
      console.error('Error reset password', error)
    }
  },
}
