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
    class="custom bg-white pa-1"
    hover
    :to="{ name: 'book-detail', params: { id: book.id, slug: book.urlSlug } }"
    :ripple="false"
    width="100%"
    height="100%"
  >

    <!-- `cover` prop to crop the image to fill the parent size -->

    <v-img
      cover
      :src="book.imageUrl"
      height="300"
    >

    </v-img>

    <v-card-text>

      <v-tooltip
        :text="book.title"
        target="cursor"
        location="bottom"
      >

        <template v-slot:activator="{ props }">

          <h3
            class="mb-3 text-truncate"
            v-bind="props"
          >
             {{ book.title }}
          </h3>

        </template>

      </v-tooltip>

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

