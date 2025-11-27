<script setup>
import { useCartStore } from '@/stores/cart-store'

const cartStore = useCartStore()
</script>

<template>

  <v-sheet>

    <v-btn @click="cartStore.addToCart"> click me</v-btn>

    <v-card
      v-for="(item, i) in cartStore.activeCart"
      :key="i"
      :title="item.name"
    ></v-card>

  </v-sheet>

</template>

