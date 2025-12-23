import api from './api-config'

export const AdminService  = {
  async getAllCustomersPaginated(token) {
    try {
      const res = await api.get('/api/v1/admin/get-customers', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
      return res.data
    } catch (error) {
      console.error("Error fetching customers")
    }
  },
  async getAllStaffPaginated(token) {
    try {
      const res = await api.get('/api/v1/admin/get-staff', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      })
      return res.data
    } catch (error) {
      console.error("Error fetching staff")
    }
  },
}