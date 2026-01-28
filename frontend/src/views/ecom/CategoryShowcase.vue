<script setup>
import { BookService } from '@/api/book-api'
import { CategoryService } from '@/api/category-api'
import BookCardShowcase from '@/components/books/BookCardShowcase.vue'
import { ref, watchEffect } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const paginationLength = ref([])
const books = ref([])
const currentPage = ref(1)
const currentSize = ref(24)
const slug = ref(route.params.slug)
const categoryName = ref('')

async function fetchAllBooks({ page: paramPage, size: paramSize }) {
  try {
    const res = await BookService.fetchAllBooks({ page: paramPage, size: paramSize })
    paginationLength.value = res.page
    books.value = res.content
  } catch (error) {
    console.error(error)
  }
}

async function fetchCategoryName(slug) {
  try {
    const res = await CategoryService.fetchCategoryName({ slug: slug })
    categoryName.value = res
  } catch (error) {
    console.error(error)
  }
}

async function fetchBookByCategory(category, { page: paramPage, size: paramSize }) {
  try {
    const res = await CategoryService.fetchBookByCategory(category, {
      page: paramPage,
      size: paramSize,
    })
    paginationLength.value = res.page
    books.value = res.content
  } catch (error) {
    console.error(error)
  }
}

watchEffect(() => {
  const slug = route.params.slug
  if (slug) {
    if (slug === 'tat-ca') {
      categoryName.value = 'Tất Cả Sách'
      fetchAllBooks({ page: 0, size: 24 })
    } else {
      fetchCategoryName(slug)
      fetchBookByCategory(slug, { page: 0, size: 24 })
    }
  }
})
</script>

<template>

  <v-row>

    <h1 class="ml-3">{{ categoryName }}</h1>

  </v-row>

  <v-row>

    <v-col
      cols="12"
      md="3"
      v-for="book in books"
    >

      <book-card-showcase :book="book" />

    </v-col>

  </v-row>

  <v-pagination
    :length="paginationLength.totalPages"
    v-model="currentPage"
    @update:model-value="
      route.params.slug === 'tat-ca'
        ? fetchAllBooks({ page: currentPage - 1, size: currentSize })
        : fetchBookByCategory(route.params.slug, { page: currentPage - 1, size: currentSize })
    "
  ></v-pagination>

</template>

