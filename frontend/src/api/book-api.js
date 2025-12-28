import api, { adminApi } from './api-config'

const API_ENDPOINTS = {
  NEW_ARRIVAL: '/books/new',
  BEST_SELLERS: '/books/best-sellers',
  ADD_NEW_BOOK: '/books/add',
  UPDATE_BOOK: '/books/update',
}

export const BookService = {
  /**
   * Fetch best seller books from API
   * @returns {Promise<Book[]>} A promise resolves to an array of best seller books
   * @throws {Error} if API call fails
   */
  async fetchBestSellersBooks() {
    try {
      const res = await api.get(API_ENDPOINTS.BEST_SELLERS)
      return res.data
    } catch (error) {
      console.error('Error fetching best sellers: ', error)
      throw error
    }
  },

  /**
   * Fetch new arrival books from API
   * @returns {Promise<Book[]>} A promise resolves to an array of new arrival books
   * @throws {Error} if API call fails
   */
  async fetchNewArrivalBooks() {
    try {
      const res = await api.get(API_ENDPOINTS.NEW_ARRIVAL)
      return res.data
    } catch (error) {
      console.error('Error fetching new arrival books: ', error)
      throw error
    }
  },

  /**
   * Fetch a book by its ID
   * @param {String} id - The ID of a book to fetch
   * @returns {Promise<Book>} A promise resolves to a book object
   * @throws {Error} if API call fails
   */
  async fetchBookById(id) {
    try {
      const res = await api.get(`/books/${id}`)
      return res.data
    } catch (error) {
      console.error(`Error fetching book with id: ${id}`, error)
      throw error
    }
  },

  /**
   * Fetch all books with optional query parameters
   * @param {Object} params - Optional query parameters for filtering, sorting, or pagination
   * @returns {Promise<Book[]>} A promise resolves to an array of books
   * @throws {Error} if API call fails
   */
  async fetchAllBooks(params = {}) {
    try {
      const res = await api.get('/books', { params })
      return res.data
    } catch (error) {
      console.error('Error fetching all books', error)
      throw error
    }
  },

  /**
   * Delete a book by its ID
   * @param {String} id - The ID of the book to delete
   * @param {String} token - Authorization token for the request
   * @returns {Promise<Object>} A promise resolves to the API response data
   * @throws {Error} if API call fails
   */
  async deleteBookById(id, token) {
    try {
      const res = await adminApi.delete('/books/delete', {
        params: { id },
      })
      return res.data
    } catch (error) {
      console.error(`Error deleting book with id ${id}`, error)
      throw error
    }
  },

  /**
   * Update a book by its ID
   * @param {Object} body - The book data to update
   * @param {String} id - The ID of the book to update
   * @param {String} token - Authorization token for the request
   * @returns {Promise<Object>} A promise resolves to the API response data
   * @throws {Error} if API call fails
   */
  async updateBookById(body, id, token) {
    try {
      const res = await adminApi.put(API_ENDPOINTS.UPDATE_BOOK, body, {
        params: { id },
        headers: {
          'Content-Type': `multipart/form-data`,
        },
      })
      return res.data
    } catch (error) {
      console.error(`Error updating book with id ${id}`, error)
      throw error
    }
  },

  /**
   * Add a new book to the system
   * @param {Object} body - The book data to create
   * @param {String} token - Authorization token for the request
   * @returns {Promise<Object>} A promise resolves to the created book data
   * @throws {Error} if API call fails
   */
  async addNewBook(body, token) {
    try {
      const res = await adminApi.post(API_ENDPOINTS.ADD_NEW_BOOK, body, {
        headers: {
          'Content-Type': `multipart/form-data`,
        },
      })
      return res.data
    } catch (error) {
      console.error('Error creating new book', error)
      throw error
    }
  },

  async searchBook(params = {}) {
    try {
      const res = await api.get('/books/search', { params })
      return res.data
    } catch (error) {
      console.error('Error searching book', error)
      throw error
    }
  }
}
