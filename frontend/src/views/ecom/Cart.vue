<script setup>
import { useCartStore } from '@/stores/cart-store'

const cartStore = useCartStore()
</script>

<template>

  <v-sheet>

    <v-btn @click="cartStore.addToCart"> click me</v-btn>

  </v-sheet>

</template>

