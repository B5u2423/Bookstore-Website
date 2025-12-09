import api from './api-config'

/**
 * Admin API functions - these require authentication with ADMIN role
 */

export function getAllUsers() {
  return api.get('/api/v1/admin/users/all')
}

export function createUser(userData) {
  return api.post('/api/v1/admin/users', userData)
}

export function updateUser(userId, userData) {
  return api.put(`/api/v1/admin/users/${userId}`, userData)
}

export function deleteUser(userId) {
  return api.delete(`/api/v1/admin/users/${userId}`)
}

export function createBook(bookData) {
  return api.post('/api/v1/books/add', bookData)
}

export function updateBook(bookId, bookData) {
  return api.put(`/api/v1/admin/books/${bookId}`, bookData)
}

export function deleteBook(bookId) {
  return api.delete(`/api/v1/admin/books/${bookId}`)
}
