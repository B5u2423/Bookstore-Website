<script setup>
import VerticalBookCard from './VerticalBookCard.vue'
const books = [
  {
    id: 1,
    slug: 'title-in-kebab',
    image:
      'https://raw.githubusercontent.com/gwenf/vuetify-responsive/master/public/img/products/product-1.jpg',
    title: 'Nintendo Switch',
    author: 'Author A',
    price: 299.99,
  },
]
</script>

<template>

  <router-link class="text-h5">Something here</router-link>

  <v-slide-group class="pa-4">

    <v-slide-group-item
      v-for="book in books"
      class="ma-3"
    >

      <VerticalBookCard
        :book="book"
        class="ma-3"
      />

    </v-slide-group-item>

  </v-slide-group>

</template>

