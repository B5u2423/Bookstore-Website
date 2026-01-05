import api, { customerApi } from './api-config'

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
      const res = await customerApi.post('/customers/add-address', body)
      return res.data
    } catch (error) {
      console.error('Error create new address for user')
    }
  },

  async getCustomerAccount(token) {
    try {
      const res = await customerApi.get('/customers/account')
      return res.data
    } catch (error) {}
  },

  async deleteAddress(id) {
    try {
      const res = await customerApi.delete('customers/remove-address', {
        params: { id },
      })
    } catch (error) {
      console.error('Error deleting address', error)
    }
  },
}
