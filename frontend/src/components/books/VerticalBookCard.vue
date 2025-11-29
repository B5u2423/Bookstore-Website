<script setup>
const props = defineProps({
  book: Object,
})
</script>

<template>
    <v-card variant="outlined" elevation="4" class="pb-3">
    <!-- `cover` prop to crop the image to fill the parent size -->
    <v-img cover :src="book.image" height="200px"> </v-img>
    <v-card-title>{{ book.title }}</v-card-title>
    <v-card-subtitle>
      <div>

      {{ book.author }}
      </div>
      <div> 

      {{ book.price }}
      </div>
    </v-card-subtitle>
  </v-card>
</template>

