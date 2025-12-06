import api from './api-config'

const USER_ENDPOINTS = {
  UPDATE_PROFILE: '/api/v1/customers/profile'
}

export const UserService = {
  async updateUserProfile(token, body) {
    try {
      const res = api.put(
        USER_ENDPOINTS.UPDATE_PROFILE,
        body,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      )
    } catch (error) {
      console.error('Error updateing user profile', error)
      throw error
    }

  }
}

export function getCustomerAccount(token) {
  return api.get('/api/v1/customers/account', {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  })
}
