<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

const goToHomePage = () => {
  router.push({ name: 'landing' })
}
</script>

<template>
  <v-toolbar class="bg-white ma-0">
    <v-container class="d-flex align-center">
      <!-- Brand icon -->
      <v-img  
        class="cursor-pointer"
        @click="goToHomePage"
        src="https://cdn.vuetifyjs.com/docs/images/brand-kit/v-logo-circle.svg" 
        alt="ThriftBooks" 
        max-width="120"></v-img>

      <!-- Search bar -->
      <v-text-field
        append-inner-icon="mdi-magnify"
        placeholder="Search by title, author, or ISBN"
        hide-details
        variant="outlined"
        class="mx-5 flex-grow-1"
      />

      <!-- Icon buttons -->
      <v-btn prepend-icon="mdi-heart" stacked size="x-small">MY LIST</v-btn>
      <v-btn prepend-icon="mdi-cart" stacked size="x-small">CART</v-btn>
      <v-btn text size="small"
        :to="{ name: 'user-register' }" 
      >REGISTER</v-btn> |
      <v-btn text size="small"
        :to="{ name: 'user-login' }" 
      >LOGIN</v-btn>
      </v-container>
  </v-toolbar>
</template>