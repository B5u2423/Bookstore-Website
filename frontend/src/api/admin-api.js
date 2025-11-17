import api from './auth-api'

/**
 * Admin API functions - these require authentication with ADMIN role
 */

/**
 * Get all users (admin only)
 * @returns {Promise} API response with all users data
 */
export function getAllUsers() {
  return api.get('/api/v1/admin/users/all')
}

/**
 * Create a new user (admin only)
 * @param {Object} userData - User data to create
 * @returns {Promise} API response
 */
export function createUser(userData) {
  return api.post('/api/v1/admin/users', userData)
}

/**
 * Update user data (admin only)
 * @param {string} userId - ID of user to update
 * @param {Object} userData - Updated user data
 * @returns {Promise} API response
 */
export function updateUser(userId, userData) {
  return api.put(`/api/v1/admin/users/${userId}`, userData)
}

/**
 * Delete user (admin only)
 * @param {string} userId - ID of user to delete
 * @returns {Promise} API response
 */
export function deleteUser(userId) {
  return api.delete(`/api/v1/admin/users/${userId}`)
}

/**
 * Create a new book (admin only)
 * @param {Object} bookData - Book data following BookDTO structure
 * @returns {Promise} API response
 */
export function createBook(bookData) {
  return api.post('/api/v1/books/add', bookData)
}

/**
 * Update book inventory (admin only)
 * @param {string} bookId - ID of book to update
 * @param {Object} bookData - Updated book data
 * @returns {Promise} API response
 */
export function updateBook(bookId, bookData) {
  return api.put(`/api/v1/admin/books/${bookId}`, bookData)
}

/**
 * Delete book (admin only)
 * @param {string} bookId - ID of book to delete
 * @returns {Promise} API response
 */
export function deleteBook(bookId) {
  return api.delete(`/api/v1/admin/books/${bookId}`)
}
