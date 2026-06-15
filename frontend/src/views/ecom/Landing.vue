<script setup>
import { BookService } from '@/api/book-api'
import BookSlideGroup from '@/components/books/BookSlideGroup.vue'
import { onMounted, ref } from 'vue'

const bestsellers = ref([])
const newbooks = ref([])
const loading = ref(true)

const fetchBooks = async () => {
  try {
    bestsellers.value = await BookService.getBooksInCollectionForLandingPage({
      collection: 'best-sellers',
    })
    newbooks.value = await BookService.getBooksInCollectionForLandingPage({ collection: 'new' })
  } catch (error) {
    console.error('Error fetching books:', error)
  } finally {
    loading.value = false
  }
}

const carouselItems = [
  'https://theme.hstatic.net/200000845405/1001223012/14/home_slider_image_2.jpg?v=475',
  'https://theme.hstatic.net/200000845405/1001223012/14/home_slider_image_3.jpg?v=475',
  'https://cdn.hstatic.net/files/200001055148/file/banner_xmas_750x422px-01.jpg',
]

onMounted(() => {
  fetchBooks()
})
</script>

<template>
  <v-sheet>
    <v-carousel
      hide-delimiters
      transition-duration="700"
      crossfade
      show-arrows="hover"
    >
      <v-carousel-item
        v-for="(item, i) in carouselItems"
        :key="i"
        :src="item"
        cover
      ></v-carousel-item>
    </v-carousel>

    <div class="ma-4">
      <book-slide-group
        group-header="Bán chạy"
        :books="bestsellers"
        :loading="loading"
        route-to="login"
      />

      <book-slide-group
        group-header="Sách mới về"
        :books="newbooks"
        :loading="loading"
        route-to="login"
      />
    </div>
  </v-sheet>
</template>

<style>
</style>
