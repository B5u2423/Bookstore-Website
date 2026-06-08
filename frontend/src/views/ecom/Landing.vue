<script setup>
import BookSlideGroup from '@/components/books/BookSlideGroup.vue'
import Carousel from '@/components/books/Carousel.vue'
import { ref, onMounted } from 'vue'
import { BookService } from '@/api/book-api'
import Book from '../admin/Book.vue'

const bestsellers = ref([])
const newbooks = ref([])
const loading = ref(false)
const error = ref(null)

const fetchBooks = async () => {
  loading.value = true
  try {
    bestsellers.value = await BookService.fetchBestSellersBooks()
    newbooks.value = await BookService.fetchNewArrivalBooks()
  } catch (error) {
    console.error('Error fetching books:', error)
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
        route-to="login"
      />

      <BookSlideGroup
        group-header="Sách mới về"
        :books="newbooks.slice(0, 10)"
        route-to="login"
      />

      <BookSlideGroup
        group-header="Sách mới về"
        :books="newbooks.slice(0, 10)"
        route-to="login"
      />

      <BookSlideGroup
        group-header="Sách mới về"
        :books="newbooks.slice(0, 10)"
        route-to="login"
      />

    </div>

  </v-sheet>

</template>

<style>

</style>

