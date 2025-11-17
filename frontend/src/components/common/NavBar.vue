<template>
   <v-toolbar color="grey" density="comfortable"
    > <v-container class="d-flex align-center"
      > <!-- Main navigation buttons -->
      <div class="d-flex">
         <v-btn v-for="item in buttons" :key="item.name" :to="item.path" variant="text"
          > {{ item.name }} </v-btn
        >
      </div>
       <v-spacer></v-spacer> </v-container
    > </v-toolbar
  >
</template>

<script setup>
const buttons = [
  { name: 'Tất cả danh mục', path: '/' },
  { name: 'Tất cả sách', path: '/books' },
  { name: 'Khuyến mãi', path: '/deals' },
]
</script>

