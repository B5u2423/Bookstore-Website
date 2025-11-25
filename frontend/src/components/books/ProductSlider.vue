<script setup>
// ======================================
// Display item slider on the Landing page
// ======================================
import ProductItem from './ProductItem.vue'

const props = defineProps(['title', 'subtitle', 'products'])
</script>

<template>
   <v-container
    >
    <h2>{{ title }}</h2>

    <p>{{ subtitle }}</p>
     <v-slide-group show-arrows
      > <v-slide-group-item v-for="(product, index) in products" :key="index"
        > <ProductItem :product="product" /> </v-slide-group-item
      > </v-slide-group
    > </v-container
  >
</template>

