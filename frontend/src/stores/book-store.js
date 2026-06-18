import * as bookApi from '@/api/book-api'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useBookStore = defineStore('bookstore', () => {
  const isLoading = ref(false)
  const error = ref('')

  const featuredBooks = ref([])
  const bestSellersBooks = ref([])
  const newArrivalBooks = ref([])
  async function fetchFeaturedBooks() {
    const res = await bookApi.fetchFeaturedBooks()
    featuredBooks.value = res.data
  }

  async function fetchBestSellersBooks() {
    const res = await bookApi.fetchBestSellersBooks()
    bestSellersBooks.value = res.data
  }

  async function fetchLandingPageBooks() {
    await Promise.all([fetchFeaturedBooks(), fetchBestSellersBooks()])
  }

  return {
    featuredBooks,
    bestSellersBooks,
    newArrivalBooks,
    fetchFeaturedBooks,
    fetchBestSellersBooks,
    fetchLandingPageBooks,
  }
})
