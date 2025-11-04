<script setup>
import { ref } from 'vue';

const props = defineProps(['product'])

// Custom hover to style `a` element
const isHovered = ref(false)
</script>

<template>
  <v-card class="ma-2" width="200" 
    @mouseenter="isHovered = true"
    @mouseleave="isHovered = false"
    hover>
    <router-link
      :to="{ name: 'product-detail', params: { title: product.title} }">
      <v-img
        :src="product.image"
        height="250"
        cove
      ></v-img>
      
      <v-card-title :class="[ isHovered ? 'text-decoration-underline' : '' ]">{{ product.title }}</v-card-title>
      <v-card-subtitle>{{ product.author }}</v-card-subtitle>
      <v-card-text>
        From: <strong>{{ product.price }}</strong>
      </v-card-text>
    </router-link>
  </v-card>
</template>

<style scoped>
  a:link,
  a:visited {
    color: black; /* or whatever color you want */
    text-decoration: none;
  }
</style>