import { adminApi, customerApi } from './api-config'

export const CouponService = {
  async getAllCouponsPaginated(params = {}) {
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

  async updateCoupon(id, body) {
    return await adminApi.put('/coupons/update', body, {
      params: { id },
    })
  },

  async addNewCoupon(body) {
    return await adminApi.post('/coupons/add', body)
  },

  async deleteCoupon(id) {
    return adminApi.delete('/coupons/delete', {
      params: { id },
    })
  },

  async applyCoupon(body) {
    return await customerApi.post('/coupons/apply', body)
  },

  async getAvailableCoupons(params = {}) {
    return await customerApi.get('/coupons/available', { params: params })
  },
}
