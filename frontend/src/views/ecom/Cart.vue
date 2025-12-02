<script setup>
import { useCartStore } from '@/stores/cart-store'
import HorizontalBookCard from '@/components/books/HorizontalBookCard.vue';

const cartStore = useCartStore()
</script>

<template>


  <v-container class="border">

    <v-row v-if="true" class="ma-3">Không có sản phẩm nào trong giỏ hàng. Quay lại cửa hàng để tiếp tục mua sắm.</v-row>
    <v-row v-else>
      
    </v-row>
  </v-container>

</template>

