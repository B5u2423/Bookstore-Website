import api from './api-config'

const CATEGORY_ENDPOINT = {
  GET_ALL: '/api/v1/categories/all',
  GET_ALL_PAGINATED: '/api/v1/categories',
  GET_CANDIDATES: '/api/v1/categories/candidates',
  PUT_UPDATE: '/api/v1/categories/update',
  POST_ADD: '/api/v1/categories/add',
}

export const CategoryService = {
  async fetchAllCategories() {
    try {
      const res = await api.get(CATEGORY_ENDPOINT.GET_ALL)
      return res.data
    } catch (error) {
      console.error('Error fetching categories')
      throw error
    }
  },

  async fetchAllCategoriesPaginated(params = {}) {
    try {
      const res = await api.get(CATEGORY_ENDPOINT.GET_ALL_PAGINATED, {
        params: params,
      })
      return res.data
    } catch (error) {
      console.error('Error fetching categories paginated')
      throw error
    }
  },

  async fetchChildCandidates(id) {
    try {
      const res = await api.get(CATEGORY_ENDPOINT.GET_CANDIDATES, {
        params: { id },
      })
      return res.data
    } catch (error) {
      console.error('Error fetching candidate children categories')
      throw error
    }
  },

  async updateCategory(body, token) {
    try {
      const res = await api.put(CATEGORY_ENDPOINT.PUT_UPDATE, body, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      return res.data
    } catch (error) {
      console.error('Error update category')
      throw error
    }
  },

  async addCategory(body, token) {
    try {
      const res = await api.post(CATEGORY_ENDPOINT.POST_ADD, body, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      })
      return res.data
    } catch (error) {
      console.error('Error adding category')
      throw error
    }
  },
}
