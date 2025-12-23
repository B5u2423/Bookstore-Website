import api, { adminApi } from './api-config'

export const CouponService = {
  async getAllCouponsPaginated(token, params = {}) {
    try {
      const res = await adminApi.get('/coupons', {
        params: params,
      })
      return res.data
    } catch (error) {
      console.error('Error fetch paginated coupons', error)
      throw error
    }
  },
  async updateCoupon(id, body, token) {
    try {
      const res = await adminApi.put('/coupons/update', body, {
        params: { id },
      })
      return res.data
    } catch (error) {
      console.error('Error updating coupon')
      throw error
    }
  },

  async addNewCoupon(body, token) {
    try {
      const res = await adminApi.post('/coupons/add', body)
      return res.data
    } catch (error) {
      console.error('Error add coupon')
      throw error
    }
  },

  async deleteCoupon(id, token) {
    try {
      const res = adminApi.delete('/coupons/delete', {
        params: { id },
      })
      return res.data
    } catch (error) {
      console.error('Error delete coupon')
      throw error
    }
  },
}
