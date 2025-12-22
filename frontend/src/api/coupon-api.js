import api from './api-config'

export const CouponService = {
  async getAllCouponsPaginated(token, params = {}) {
    try {
      const res = await api.get('/api/v1/coupons', {
        params: params,
        headers: { Authorization: `Bearer ${token}` },
      })
      return res.data
    } catch (error) {
      console.error('Error fetch paginated coupons', error)
      throw error
    }
  },
  async updateCoupon(body, token) {
    try {
      const res = api.put('/api/v1/coupons/update', body, {
        headers: { Authorization: `Bearer ${token}` },
      })
      return res.data
    } catch (error) {
      console.error('Error updating coupon')
      throw error
    }
  },

  async addNewCoupon(body, id, token) {
    try {
      const res = api.post('/api/v1/coupons/add', body, {
        params: { id },
        headers: { Authorization: `Bearer ${token}` },
      })
      return res.data
    } catch (error) {
      console.error('Error add coupon')
      throw error
    }
  },

  async deleteCoupon(id, token) {
    try {
      const res = api.delete('/api/v1/coupons/delete', {
        params: { id },
        headers: { Authorization: `Bearer ${token}` },
      })
      return res.data
    } catch (error) {
      console.error('Error delete coupon')
      throw error
    }
  },
}
