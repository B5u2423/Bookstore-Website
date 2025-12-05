import api from './api-config'

const CATEGORY_ENDPOINT = {
  GET_ALL: '/api/v1/categories/all',
  GET_ALL_PAGINATED: '/api/v1/categories',
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
}
