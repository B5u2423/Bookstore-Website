<script setup>
// =====================================
// Display item grid on the Landing page
// =====================================
import ProductItem from './ProductItem.vue'

const props = defineProps(['title', 'subtitle', 'products'])
</script>

<template>
   <v-container
    > <v-item-group class="justify-center"
      >
      <h2>Hello </h2>

      <p>Subtitle</p>
       <v-row
        > <v-col
          class="ma-4 pa-0"
          v-for="product in products.slice(0, 10)"
          :key="product"
          lg="2"
          md="3"
          sm="4"
          xs="5"
          cols="12"
          > <v-item> <ProductItem :product="product" /> </v-item> </v-col
        > </v-row
      > </v-item-group
    > </v-container
  >
</template>

