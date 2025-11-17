import axios from 'axios'

const bookApi = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
})

export function fetchFeaturedBooks() {
  return bookApi.get('/api/v1/books/featured')
}

export function fetchBestSellersBooks() {
  return bookApi.get('/api/v1/books/best-sellers')
}

export function fetchNewArrivalBooks() {
  return bookApi.get('/api/v1/books/new')
}

export function fetchAllBooks() {
  return bookApi.get('/api/v1/books')
}

export function fetchBookById(id) {
  return bookApi.get(`/api/v1/books/${id}`)
}
