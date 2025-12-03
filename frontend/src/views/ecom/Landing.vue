<script setup>
import BookSlideGroup from '@/components/books/BookSlideGroup.vue'
import Carousel from '@/components/books/Carousel.vue'
import { ref, onMounted } from 'vue'
import { fetchBestSellersBooks, fetchNewArrivalBooks } from '@/api/book-api'

const bestsellers = ref([])
const newbooks = ref([])
const loading = ref(false)
const error = ref(null)

const fetchBooks = async () => {
  loading.value = true
  try {
    const [bestSellersResponse, newArrivalsResponse] = await Promise.all([
      fetchBestSellersBooks(),
      fetchNewArrivalBooks(),
    ])

    bestsellers.value = bestSellersResponse.data
    console.log(bestsellers.value)
    newbooks.value = newArrivalsResponse.data
    console.log(newbooks.value)
  } catch (err) {
    error.value = err.message
    console.error('Error fetching books:', err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchBooks()
})
</script>

<template>

  <v-sheet>

    <Carousel />

    <div class="ma-4">

      <BookSlideGroup
        group-header="Bán chạy"
        :books="bestsellers.slice(0, 10)"
      />

      <BookSlideGroup
        group-header="Sách mới về"
        :books="newbooks.slice(0, 10)"
      />

    </div>

  </v-sheet>

</template>

<style>

</style>

