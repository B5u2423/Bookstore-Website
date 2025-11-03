import axios from 'axios';

export function fetchFeaturedBooks() {
  return axios.get('/api/v1/books/featured')
}

export function fetchBestSellersBooks() {
  return axios.get('/api/v1/books/best-sellers')
}

export function fetchNewArrivalBooks() {
  return null
}