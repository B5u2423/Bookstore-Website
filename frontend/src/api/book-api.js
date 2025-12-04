import api from './api-config'

const API_ENDPOINTS = {
  NEW_ARRIVAL: '/api/v1/books/new',
  BEST_SELLERS: '/api/v1/books/best-sellers',
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
      console.error("Error fetching best sellers: ", error)
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
      console.error("Error fetching new arrival books: ", error)
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
      const res = await api.get(`/api/v1/books/${id}`)
      return res.data
    } catch (error) {
      console.error(`Error fetching book with id: ${id}`, error)
      throw error
    }
  },
}

export function fetchAllBooks() {
  return api.get('/api/v1/books')
}

