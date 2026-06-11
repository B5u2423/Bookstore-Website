<script setup>
import { formatPriceVNLocale } from '@/utils/utils'

const props = defineProps({
  book: Object,
  loading: Boolean,
})
</script>

<template>

  <v-skeleton-loader
    v-if="loading"
    :elevation="1"
    type="image, article"
    width="180"
    class="ma-2"
  />

  <v-card
    v-else
    variant="flat"
    elevation="4"
    class="custom"
    hover
    :to="{ name: 'book-detail', params: { id: book.id, slug: book.urlSlug } }"
    :ripple="false"
    width="186"
  >

    <!-- `cover` prop to crop the image to fill the parent size -->

    <v-img
      cover
      :src="book.imageUrl"
      :aspect-ratio="3 / 4"
      style="background-color: #f5f5f5"
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
            v-bind="props"
            class="mb-3 text-truncate"
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

