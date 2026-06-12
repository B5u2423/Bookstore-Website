<script setup>
import VerticalBookCard from './VerticalBookCard.vue'
import { ref } from 'vue'

const props = defineProps({
  routeTo: String,
  groupHeader: String,
  books: Array,
  loading: Boolean,
})

const track = ref(null)

function scrollByOneItem(direction) {
  const firstItem = track.value?.firstElementChild
  if (!firstItem) return

  const gap = parseFloat(window.getComputedStyle(track.value).gap)
  const itemWidth = firstItem.getBoundingClientRect().width + gap

  track.value.scrollBy({ left: direction * itemWidth, behavior: 'smooth' })
}
</script>

<template>

  <div class="d-flex align-center justify-space-between flex-wrap ga-2 mb-4">

    <router-link
      class="text-h6 custom-link"
      :to="{ name: routeTo }"
    >
       {{ groupHeader }}
    </router-link>

    <div>

      <v-btn
        density="comfortable"
        size="auto"
        icon="mdi-chevron-left"
        variant="text"
        @click="scrollByOneItem(-1)"
      />

      <v-btn
        density="comfortable"
        size="auto"
        icon="mdi-chevron-right"
        variant="text"
        @click="scrollByOneItem(1)"
      />

    </div>

  </div>

  <div class="slider-wrapper">

    <div
      class="slider-track"
      ref="track"
    >

      <!-- loading: show N skeleton placeholders -->

      <template v-if="loading">

        <VerticalBookCard
          v-for="n in 5"
          :key="n"
          :loading="true"
        />

      </template>

      <!-- loaded: show real cards -->

      <template v-else>

        <VerticalBookCard
          v-for="book in books"
          :key="book.id"
          :book="book"
          class="slider-item"
        />

      </template>

    </div>

  </div>

</template>

<style scoped>
.slider-wrapper {
  display: flex;
  align-items: center;
}

.slider-track {
  display: flex;
  overflow-x: hidden;   /* hides the scrollbar but still scrollable via JS */
  scroll-behavior: smooth;
  gap: 12px;
  padding: 16px 0;
}

.slider-item {
  flex-shrink: 0;  /* prevents cards from squishing */
}

.custom-link {
  text-decoration: none;
  color: inherit; /* or any specific color */
}

.custom-link:hover {
  text-decoration: underline;
}
</style>

