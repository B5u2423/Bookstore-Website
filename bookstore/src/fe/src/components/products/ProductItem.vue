<script setup>
const props = defineProps(['product'])
</script>

<template>
  <v-card class="ma-2" width="200" hover>
    <router-link
      :to="{ name: 'product-detail', params: { id: product.id}}">
    <v-img
      :src="product.image"
      height="250"
      cove
    ></v-img>
    
    <v-card-title>{{ product.title }}</v-card-title>
    <v-card-subtitle>{{ product.author }}</v-card-subtitle>
    <v-card-text>
      From: <strong>{{ product.price }}</strong>
    </v-card-text>
    </router-link>
  </v-card>
</template>