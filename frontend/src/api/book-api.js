import api from './api-config'

export function fetchFeaturedBooks() {
  return api.get('/api/v1/books/featured')
}

export function fetchBestSellersBooks() {
  return api.get('/api/v1/books/best-sellers')
}

export function fetchNewArrivalBooks() {
  return api.get('/api/v1/books/new')
}

export function fetchAllBooks() {
  return api.get('/api/v1/books')
}

export function fetchBookById(id) {
  return api.get(`/api/v1/books/${id}`)
}
