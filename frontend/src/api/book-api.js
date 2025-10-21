import axios from 'axios';

export function fetchFeaturedBooks() {
  return axios.get('/api/books/featured')
}

export function fetchBestSellersBooks() {
  return axios.get('/api/books/best-sellers')
}

export function fetchNewArrivalBooks() {
  return null
}