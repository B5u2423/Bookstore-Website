<script setup>
import { formatPriceVNLocale } from '@/utils/utils'

const props = defineProps({
  book: Object,
})
</script>

<template>

  <v-card
    variant="outlined"
    elevation="4"
    class="custom ma-2"
    hover
    :to="{ name: 'book-detail', params: { id: book.id, slug: book.urlSlug } }"
    :ripple="false"
    width="180"
  >

    <!-- `cover` prop to crop the image to fill the parent size -->

    <v-img
      cover
      :src="book.imageUrl"
      height="200px"
    >

    </v-img>

    <v-card-text>

      <h3 class="mb-3"> {{ book.title }} </h3>

      <div> {{ book.author }} </div>

      <div>{{ formatPriceVNLocale(book.price) }} ₫</div>

    </v-card-text>

  </v-card>

</template>

<style>
.v-card.custom:hover {
  text-decoration: underline;
}
</style>

