<script setup>
import { BookService } from '@/api/book-api'
import { CollectionService } from '@/api/collection-api'
import VerticalBookCard from '@/components/books/VerticalBookCard.vue'
import { ref, watchEffect } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const books = ref([])
// update items/page size here if change item/grid
const currentSize = ref(15)
const collectionName = ref('')
const loading = ref(true)

const bookCardWidth = ref('100%')

async function fetchAllBooksInCollection(collection) {
  const res = await BookService.getBooksInCollectionForLandingPage({ collection })
  books.value = res.list
  collectionName.value = res.collectionName
}

watchEffect(async () => {
  loading.value = true
  try {
    const slug = route.params.slug
    await Promise.all([
      fetchAllBooksInCollection(slug),
    ])
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
})
</script>

<template>
  <div class="category-page">
    <!-- Page header -->
    <div class="category-header">
      <div class="category-header-left">
        <span class="category-accent-bar" aria-hidden="true" />
        <div>
          <div class="category-eyebrow">Bộ sưu tập</div>
          <h1 class="category-title">
            <template v-if="loading && !collectionName">
              <span class="title-skeleton" />
            </template>
            <template v-else>{{ collectionName }}</template>
          </h1>
          <!-- breadcrumbs -->
          <div class="coll-breadcrumb">
            <router-link
              to="/"
              class="breadcrumb-link"
            >
              <v-icon icon="mdi-arrow-left" size="18" />
              Trang chủ
            </router-link>
          </div>
        </div>
      </div>
    </div>

    <!-- Book grid -->
    <div class="book-grid">
      <!-- Loading state -->
      <template v-if="loading">
        <div v-for="n in currentSize" :key="n" class="book-grid-item">
          <vertical-book-card :loading="true" :width="bookCardWidth" />
        </div>
      </template>

      <!-- Empty state -->
      <template v-else-if="!books.length">
        <div class="empty-state">
          <v-icon icon="mdi-book-off-outline" size="48" class="mb-3" />
          <p class="empty-title">Không tìm thấy sách</p>
          <p class="empty-body">
            Bộ sưu tập hiện chưa có sách. Vui lòng quay lại trang chủ.
          </p>
          <router-link
            :to="{ name: 'landing' }"
            class="empty-cta"
          >
            Trang chủ
          </router-link>
        </div>
      </template>

      <!-- Book cards -->
      <template v-else>
        <div
          v-for="book in books"
          :key="book.id"
          class="book-grid-item"
        >
          <vertical-book-card :book="book" :width="bookCardWidth" />
        </div>
      </template>
    </div>
  </div>
</template>

<style scoped>
.category-page {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;

  color: var(--ink);
  font-family: system-ui, -apple-system, sans-serif;
  padding-bottom: 64px;
}

.category-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 28px;
  padding-bottom: 20px;
  border-bottom: 1px solid var(--border);
  flex-wrap: wrap;
}

.category-header-left {
  display: flex;
  align-items: center;
  gap: 14px;
}

.category-accent-bar {
  display: inline-block;
  width: 4px;
  height: 42px;
  border-radius: 2px;
  background: var(--accent);
  flex-shrink: 0;
}

.category-eyebrow {
  font-size: 0.72rem;
  text-transform: uppercase;
  letter-spacing: 0.09em;
  font-weight: 700;
  color: var(--muted);
  margin-bottom: 3px;
}

.category-title {
  font-size: clamp(1.3rem, 2.2vw, 1.75rem);
  font-weight: 700;
  margin: 0;
  color: var(--ink);
  line-height: 1.2;
}

.title-skeleton {
  display: block;
  width: 200px;
  height: 28px;
  border-radius: 6px;
  background: linear-gradient(90deg, var(--border) 25%, #f0ebe4 50%, var(--border) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.4s infinite;
}

.book-grid {
  display: grid;
  /* grid-template-columns: repeat(auto-fill, minmax(190px, 1fr)); */
  grid-template-columns: repeat(auto-fill, minmax(170px, 1fr));
  gap: 20px;
}

.book-grid-item {
  display: flex;
}

.empty-state {
  grid-column: 1 / -1;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 64px 24px;
  color: var(--muted);
}

.empty-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--ink);
  margin: 0 0 8px;
}

.empty-body {
  font-size: 0.875rem;
  margin: 0 0 24px;
  max-width: 320px;
  line-height: 1.6;
}

.empty-cta {
  display: inline-flex;
  align-items: center;
  padding: 10px 22px;
  background: var(--accent);
  color: #fff;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 600;
  font-size: 0.875rem;
  transition: background 0.2s;
}
.empty-cta:hover { background: #8e1f24; }

@keyframes shimmer {
  0%   { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

@media (max-width: 600px) {
  .book-grid {
    grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
    gap: 14px;
  }
  .category-header { margin-bottom: 20px; }
}

/* breadcrumbs */
.coll-breadcrumb {
  margin: 8px 0px;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 1rem;
  color: var(--muted);
}
.breadcrumb-link {
  color: var(--muted);
  text-decoration: none;
  transition: color 0.15s;
}
.breadcrumb-link:hover { color: var(--accent); }
</style>
