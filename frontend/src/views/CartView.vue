<script setup>
import CommonLayout from '@/components/CommonLayout.vue'
const prop = {
  name: 'Giỏ Hàng',
  items: [
    { title: 'Trang Chủ', disabled: false, href: '/' },
    { title: 'Giỏ Hàng', disabled: true, href: '/cart' },
  ],
}
</script>

<template>
   <v-container fluid class="pa-4"
    > <v-row
      > <v-col cols="12" md="4"> <v-card class="pa-3"> Sidebar / Menu </v-card> </v-col> <v-col
        cols="12"
        md="8"
        > <v-card class="pa-3"> Main content goes here </v-card> </v-col
      > </v-row
    > </v-container
  >
</template>

