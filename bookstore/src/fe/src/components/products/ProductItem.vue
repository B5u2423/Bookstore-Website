<script setup>
const props = defineProps(['product'])
</script>

<template>
  <v-card class="ma-2" width="200" hover>
    <v-img
      :src="product.image"
      height="250"
      cover
    ></v-img>
    
    <v-card-title>{{ product.title }}</v-card-title>
    <v-card-subtitle>{{ product.author }}</v-card-subtitle>
    <v-card-text>
      From: <strong>{{ product.price }}</strong>
    </v-card-text>
  </v-card>
</template>