import api, { adminApi } from './api-config'

export const AdminService = {
  async getAllCustomersPaginated(token) {
    try {
      const res = await adminApi.get('/admin/get-customers')
      return res.data
    } catch (error) {
      console.error('Error fetching customers')
    }
  },
  async getAllStaffPaginated(token) {
    try {
      const res = await adminApi.get('/admin/get-staff')
      return res.data
    } catch (error) {
      console.error('Error fetching staff')
    }
  },
  async getDashboardAnalytics(params = {}) {
    try {
      const res = await adminApi.get('/admin/analytics', {
        params: params,
      })
      return res.data
    } catch (error) {
      console.error('Error fetching dashboard metrics', error)
    }
  },
}
