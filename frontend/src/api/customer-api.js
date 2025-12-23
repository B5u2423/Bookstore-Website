import api from './api-config'

const USER_ENDPOINTS = {
  UPDATE_PROFILE: '/customers/profile',
}

export const UserService = {
  async updateUserProfile(token, body) {
    try {
      const res = api.put(USER_ENDPOINTS.UPDATE_PROFILE, body, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
    } catch (error) {
      console.error('Error updating user profile', error)
      throw error
    }
  },
}

export const CustomerService = {
  async setAddress(body, token) {
    try {
      const res = await api.post('/customers/add-address', body, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      return res.data
    } catch (error) {
      console.error('Error create new address for user')
    }
  },

  async getCustomerAccount(token) {
    try {
      const res = await api.get('/customers/account', {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      return res.data
    } catch (error) {}
  },
}
