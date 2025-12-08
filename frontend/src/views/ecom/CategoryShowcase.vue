<script setup>
import { BookService } from '@/api/book-api';
import VerticalBookCard from '@/components/books/VerticalBookCard.vue';
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute()
const paginationLength = ref([])
const books = ref([])

async function fetchAllBooks() {
  try {
    const res = await BookService.fetchAllBooks({page: 0, size: 24})
    paginationLength.value = res.page
    books.value = res.content
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  fetchAllBooks()
})


</script>

<template>
  <v-row>
    <v-col cols="12" md="3" v-for="book in books">
      <vertical-book-card :book="book" />
    </v-col>
  </v-row>

  <v-pagination :length="paginationLength.totalPages"></v-pagination>
</template>
