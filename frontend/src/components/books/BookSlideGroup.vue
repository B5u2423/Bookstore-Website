<script setup>
import VerticalBookCard from './VerticalBookCard.vue';
const books = [
  {
    itemId: 1,
    image: 'https://raw.githubusercontent.com/gwenf/vuetify-responsive/master/public/img/products/product-1.jpg',
    title: 'Nintendo Switch',
    author: 'Author A',
    price: 299.99
  },

]
</script>

<template>
  <router-link class="text-h5">Something here</router-link>

  <v-slide-group class="pa-4">

    <v-slide-group-item v-for="book in books" class="ma-3">

      <VerticalBookCard :book="book" class="ma-3"/>

    </v-slide-group-item>

  </v-slide-group>

</template>

