<script setup>
import { ref } from 'vue'
import VerticalBookCard from './VerticalBookCard.vue'

defineProps({
  routeTo: String,
  routeParams: { type: Object, default: () => ({}) },
  groupHeader: String,
  books: Array,
  loading: Boolean,
})

const track = ref(null)
const bookCardWidth = ref('186px')

function scrollByOneItem(direction) {
  const firstItem = track.value?.firstElementChild
  if (!firstItem) return
  const gap = parseFloat(window.getComputedStyle(track.value).gap)
  const itemWidth = firstItem.getBoundingClientRect().width + gap
  track.value.scrollBy({ left: direction * itemWidth, behavior: 'smooth' })
}
</script>

<template>
  <section class="slide-group">
    <!-- Header -->
    <div class="slide-group-header">
      <div class="slide-group-title-wrap">
        <span class="slide-group-accent-bar" aria-hidden="true" />
        <router-link
          :to="{ name: routeTo, params: routeParams }"
          class="slide-group-title"
        >
          {{ groupHeader.toLocaleUpperCase() }}
        </router-link>
      </div>

      <div class="slide-group-controls">
        <button
          class="slide-btn"
          type="button"
          aria-label="Cuộn trái"
          @click="scrollByOneItem(-1)"
        >
          <v-icon icon="mdi-chevron-left" size="20" />
        </button>
        <button
          class="slide-btn"
          type="button"
          aria-label="Cuộn phải"
          @click="scrollByOneItem(1)"
        >
          <v-icon icon="mdi-chevron-right" size="20" />
        </button>
      </div>
    </div>

    <!-- Track -->
    <div class="slide-track-wrap">
      <div class="slide-track" ref="track">
        <template v-if="loading">
          <vertical-book-card v-for="n in 6" :key="n" :loading="true" />
        </template>
        <template v-else>
          <vertical-book-card
            v-for="book in books"
            :key="book.id"
            :book="book"
            :width="bookCardWidth"
          />
        </template>
      </div>
    </div>
  </section>
</template>

<style scoped>
.slide-group {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;

  margin-bottom: 30px;
}

.slide-group-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 8px;
}
.slide-group-title-wrap {
  display: flex;
  align-items: center;
  gap: 10px;
}
.slide-group-accent-bar {
  display: inline-block;
  width: 4px;
  height: 22px;
  border-radius: 2px;
  background: var(--accent);
  flex-shrink: 0;
}
.slide-group-title {
  font-size: 1.15rem;
  font-weight: 700;
  color: var(--ink);
  text-decoration: none;
  transition: color 0.15s;
}
.slide-group-title:hover { color: var(--accent); }
.slide-group-title:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
  border-radius: 2px;
}

.slide-group-controls { display: flex; gap: 6px; }
.slide-btn {
  width: 34px;
  height: 34px;
  border-radius: 8px;
  border: 1px solid var(--border);
  background: #fff;
  color: var(--ink);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.15s, border-color 0.15s, color 0.15s;
}
.slide-btn:hover {
  background: var(--accent-soft);
  border-color: var(--accent);
  color: var(--accent);
}
.slide-btn:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
}

.slide-track-wrap { overflow: hidden; }
.slide-track {
  display: flex;
  gap: 14px;
  overflow-x: hidden;
  scroll-behavior: smooth;
  padding: 8px 2px 16px;
}
</style>
