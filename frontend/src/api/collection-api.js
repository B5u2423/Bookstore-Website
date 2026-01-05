import api, { adminApi } from './api-config'

export const CollectionService = {
  async getAllCollections() {
    try {
      const res = await adminApi.get('/collections/get-all')
      return res.data
    } catch (error) {
      console.error('Error fetching all collections', error)
      throw error
    }
  },
  async getAllCollectionsPaginated(token, params = {}) {
    try {
      const res = await adminApi.get('/collections', {
        params: params,
      })
      return res.data
    } catch (error) {
      console.error('Error fetching paginated collections', error)
      throw error
    }
  },
  async updateCollection(id, body, token) {
    try {
      const res = await adminApi.put('/collections/update', body, {
        params: { id },
      })
      return res.data
    } catch (error) {
      console.error('Error updating collection')
      throw error
    }
  },

  async addNewCollection(body, token) {
    try {
      const res = await adminApi.post('/collections/add', body)
      return res.data
    } catch (error) {
      console.error('Error add collection')
      throw error
    }
  },

  async deleteCollection(id, token) {
    try {
      const res = adminApi.delete('/collections/delete', {
        params: { id },
      })
      return res.data
    } catch (error) {
      console.error('Error delete collection')
      throw error
    }
  },
}
