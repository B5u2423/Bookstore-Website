import api from "./api-config";

export const CollectionService = {
  async getAllCollectionsPaginated(token, params = {}) {
    try {
      const res = await api.get('/api/v1/collections', {
        params: params,
        headers: { Authorization: `Bearer ${token}` },
      })
      return res.data
    } catch (error) {
      console.error('Error fetch paginated collections', error)
      throw error
    }
  },
  async updateCollection(id, body, token) {
    try {
      const res = await api.put('/api/v1/collections/update', body, {
        headers: { Authorization: `Bearer ${token}` },
          params: {id}
      })
      return res.data
    } catch (error) {
      console.error('Error updating collection')
      throw error
    }
  },

  async addNewCollection(body, token) {
    try {
      const res = await api.post('/api/v1/collections/add', body, {
        headers: { Authorization: `Bearer ${token}` },
      })
      return res.data
    } catch (error) {
      console.error('Error add collection')
      throw error
    }
  },

  async deleteCollection(id, token) {
    try {
      const res = api.delete('/api/v1/collections/delete', {
        params: { id },
        headers: { Authorization: `Bearer ${token}` },
      })
      return res.data
    } catch (error) {
      console.error('Error delete collection')
      throw error
    }
  },
}