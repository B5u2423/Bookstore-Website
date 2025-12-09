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

/**
 * User login
 * @param {Object} loginData - Login credentials
 * @param {string} loginData.email - User email
 * @param {string} loginData.password - User password
 * @returns {Promise} API response with user data and tokens
 */
export function loginUser(loginData) {
  return api.post('/api/v1/auth/login', {
    email: loginData.email,
    password: loginData.password,
  })
}

/**
 * Admin login
 * @param {Object} loginData - Admin login credentials
 * @param {string} loginData.email - Admin email
 * @param {string} loginData.password - Admin password
 * @returns {Promise} API response with admin user data and tokens
 */
export function loginAdmin(loginData) {
  return api.post('/api/v1/auth/admin/login', {
    email: loginData.email,
    password: loginData.password,
  })
}

/**
 * User registration
 * @param {Object} registrationData - Registration data
 * @param {string} registrationData.firstName - User first name
 * @param {string} registrationData.lastName - User last name
 * @param {string} registrationData.email - User email
 * @param {string} registrationData.password - User password
 * @param {string} registrationData.userType - User type (CUSTOMER, STAFF only - ADMIN not allowed)
 * @returns {Promise} API response with created user data
 */
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

/**
 * Refresh JWT access token
 * @param {string} refreshToken - Refresh token
 * @returns {Promise} API response with new tokens
 */
export function refreshJwtToken(refreshToken) {
  return api.get('/api/v1/auth/refresh', {
    data: { refreshToken },
  })
}

/**
 * User logout
 * @returns {Promise} API response
 */
export function logoutUser(token) {
  return api.delete('/api/v1/auth/logout', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}

/**
 * Get current user profile (if authenticated)
 * @returns {Promise} API response with user data
 */
export function getCurrentUser() {
  return api.get('/api/v1/customers/account')
}

export default api
