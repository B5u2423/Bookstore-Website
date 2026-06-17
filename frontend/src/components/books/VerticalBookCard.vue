<script setup>
import { formatPriceVNLocale } from '@/utils/utils'

defineProps({
  book: Object,
  loading: Boolean,
  width: String,
})
</script>

<template>
  <!-- Skeleton -->
  <div v-if="loading" class="book-card-skeleton" :style="{ width: width }">
    <div class="skel-cover" />
    <div class="skel-body">
      <div class="skel-line skel-line--title" />
      <div class="skel-line skel-line--author" />
      <div class="skel-line skel-line--price" />
    </div>
  </div>

  <!-- Card -->
  <router-link
    v-else
    :to="{ name: 'book-detail', params: { id: book.id, slug: book.urlSlug } }"
    class="book-card"
    :style="{ width: width }"
    :title="book.title"
  >
    <div class="book-card-cover-wrap">
      <v-img
        :src="book.imageUrl"
        :alt="book.title"
        :aspect-ratio="3 / 4"
        cover
        class="book-card-cover"
      />
      <div class="book-card-cover-overlay" aria-hidden="true" />
    </div>

    <div class="book-card-body">
      <p class="book-card-title">{{ book.title }}</p>
      <p class="book-card-author">{{ book.author }}</p>
      <p class="book-card-price">{{ formatPriceVNLocale(book.price) }} ₫</p>
    </div>
  </router-link>
</template>

<style scoped>
.book-card,
.book-card-skeleton {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;
  flex-shrink: 0;
}

.book-card-skeleton {
  border-radius: 10px;
  overflow: hidden;
  background: #fff;
  border: 1px solid var(--border);
}
.skel-cover {
  aspect-ratio: 3 / 4;
  background: linear-gradient(90deg, var(--border) 25%, #f0ebe4 50%, var(--border) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.4s infinite;
}
.skel-body { padding: 12px; display: flex; flex-direction: column; gap: 8px; }
.skel-line {
  height: 10px;
  border-radius: 5px;
  background: linear-gradient(90deg, var(--border) 25%, #f0ebe4 50%, var(--border) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.4s infinite;
}
.skel-line--title  { width: 85%; }
.skel-line--author { width: 60%; }
.skel-line--price  { width: 45%; }
@keyframes shimmer {
  0%   { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

.book-card {
  display: block;
  text-decoration: none;
  border-radius: 10px;
  overflow: hidden;
  background: #fff;
  border: 1px solid var(--border);
  transition: transform 0.25s ease, box-shadow 0.25s ease, border-color 0.25s ease;
  position: relative;
}
.book-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 12px 28px -8px rgba(43, 36, 32, 0.22);
  border-color: #c8bfb5;
}
.book-card:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
}

.book-card-cover-wrap {
  position: relative;
  overflow: hidden;
}
.book-card-cover {
  display: block;
  background-color: var(--paper);
  transition: transform 0.35s ease;
}
.book-card:hover .book-card-cover { transform: scale(1.04); }
.book-card-cover-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(to top, rgba(43,36,32,0.06) 0%, transparent 40%);
  pointer-events: none;
}

.book-card-body { padding: 11px 12px 14px; }
.book-card-title {
  font-size: 0.84rem;
  font-weight: 600;
  color: var(--ink);
  margin: 0 0 4px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.35;
  transition: color 0.15s;
}
.book-card:hover .book-card-title { color: var(--accent); }
.book-card-author {
  font-size: 0.77rem;
  color: var(--muted);
  margin: 0 0 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.book-card-price {
  font-size: 0.875rem;
  font-weight: 700;
  color: var(--accent);
  margin: 0;
}
</style>
