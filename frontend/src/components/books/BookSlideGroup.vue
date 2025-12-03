<script setup>
import VerticalBookCard from './VerticalBookCard.vue'

const props = defineProps({
  routeTo: String,
  groupHeader: String,
  books: Array,
})
</script>

<template>

  <router-link
    class="text-h5 custom-link"
    :to="{ name: routeTo }"
  >
    {{ groupHeader }}
  </router-link>

  <v-slide-group class="pa-4">

    <v-slide-group-item v-for="book in books">

      <VerticalBookCard
        :book="book"
        class="ma-3"
      />

    </v-slide-group-item>

  </v-slide-group>

</template>

<style>
.custom-link {
  text-decoration: none;
  color: inherit; /* or any specific color */
}

.custom-link:hover {
  text-decoration: underline;
}
</style>

