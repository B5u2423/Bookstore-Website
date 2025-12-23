import api, { adminApi } from './api-config'

const CATEGORY_ENDPOINT = {
  GET_ALL: '/categories/all',
  GET_PARENTS: '/categories/parents',
  GET_CHILDREN: '/categories/children',
  GET_ALL_PAGINATED: '/categories',
  GET_CANDIDATES: '/categories/candidates',
  PUT_UPDATE: '/categories/update',
  POST_ADD: '/categories/add',
  DELETE: `/categories/delete`,
}

export const CategoryService = {
  async fetchChildrenCategories() {
    try {
      const res = await api.get(CATEGORY_ENDPOINT.GET_CHILDREN)
      return res.data
    } catch (error) {
      console.error('Error fetching children')
      throw error
    }
  },
  async fetchParentCategories() {
    try {
      const res = await api.get(CATEGORY_ENDPOINT.GET_PARENTS)
      return res.data
    } catch (error) {
      console.error('Error fetching parents')
      throw error
    }
  },

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
      const res = await adminApi.put(CATEGORY_ENDPOINT.PUT_UPDATE, body)
      return res.data
    } catch (error) {
      console.error('Error update category')
      throw error
    }
  },

  async addCategory(body, token) {
    try {
      const res = await adminApi.post(CATEGORY_ENDPOINT.POST_ADD, body)
      return res.data
    } catch (error) {
      console.error('Error adding category')
      throw error
    }
  },

  async deleteCategoryById(id, token) {
    try {
      const res = await adminApi.delete(CATEGORY_ENDPOINT.DELETE, {
        params: { id },
      })
      return res.data
    } catch (error) {
      console.error('Error deleting category')
      throw error
    }
  },
}
