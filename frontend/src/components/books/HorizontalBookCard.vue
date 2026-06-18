<script setup>
import { formatPriceVNLocale } from '@/utils/utils'

defineProps({
  book: Object,
})
</script>

<template>
  <router-link
    class="order-item"
    :to="{ name: 'book-detail', params: { id: book.id, slug: book.urlSlug ?? book.slug } }"
  >
    <!-- Thumbnail -->
    <div class="order-item-thumb-wrap">
      <v-img
        :src="book.image"
        :alt="book.title"
        cover
        class="order-item-thumb"
      />
      <span class="order-item-qty-badge" aria-label="`Số lượng: ${book.quantity}`">
        {{ book.quantity }}
      </span>
    </div>

    <!-- Info -->
    <div class="order-item-info">
      <p class="order-item-title" :title="book.title">{{ book.title }}</p>
      <p class="order-item-unit">{{ formatPriceVNLocale(book.price) }} ₫ / cuốn</p>
    </div>

    <!-- Line total -->
    <div class="order-item-total">
      {{ formatPriceVNLocale(book.price * book.quantity) }} ₫
    </div>
  </router-link>
</template>

<style scoped>
.order-item {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;

  display: flex;
  align-items: center;
  gap: 14px;
  padding: 12px 0;
  border-bottom: 1px solid var(--border);
  text-decoration: none;
  font-family: system-ui, -apple-system, sans-serif;
}
.order-item:last-child { border-bottom: none; }
.order-item:hover .order-item-title { color: var(--accent)}


.order-item-thumb-wrap {
  position: relative;
  flex-shrink: 0;
}
.order-item-thumb {
  width: 60px;
  height: 80px;
  border-radius: 6px;
  border: 1px solid var(--border);
  background: var(--paper);
  display: block;
}
.order-item-qty-badge {
  position: absolute;
  top: -7px;
  right: -7px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: var(--ink);
  color: #fff;
  font-size: 0.7rem;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  line-height: 1;
}

.order-item-info {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.order-item-title {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--ink);
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  line-height: 1.35;
}
.order-item-unit {
  font-size: 0.78rem;
  color: var(--muted);
  margin: 0;
}

.order-item-total {
  font-size: 0.9rem;
  font-weight: 700;
  color: var(--accent);
  white-space: nowrap;
  flex-shrink: 0;
}
</style>
