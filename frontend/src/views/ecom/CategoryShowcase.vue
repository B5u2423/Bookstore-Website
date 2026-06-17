<script setup>
import { BookService } from '@/api/book-api'
import { CategoryService } from '@/api/category-api'
import VerticalBookCard from '@/components/books/VerticalBookCard.vue'
import { ref, watchEffect } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()
const books = ref([])
const pagination = ref({})
const currentPage = ref(1)
// update items/page size here if change item/grid
const currentSize = ref(25)
const categoryName = ref('')
const loading = ref(true)

const bookCardWidth = ref('100%')

async function fetchAllBooks({ page, size }) {
  const res = await BookService.fetchAllBooks({ page, size })
  pagination.value = res.page
  books.value = res.content
}

async function fetchCategoryName(slug) {
  const res = await CategoryService.fetchCategoryName({ slug })
  categoryName.value = res
}

async function fetchBookByCategory(slug, { page, size }) {
  const res = await CategoryService.fetchBookByCategory(slug, { page, size })
  pagination.value = res.page
  books.value = res.content
}

async function loadPage(page = 0) {
  loading.value = true
  try {
    const slug = route.params.slug
    if (slug === 'tat-ca') {
      await fetchAllBooks({ page, size: currentSize.value })
    } else {
      await fetchBookByCategory(slug, { page, size: currentSize.value })
    }
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

async function onPageChange(page) {
  currentPage.value = page
  await loadPage(page - 1)
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

watchEffect(async () => {
  const slug = route.params.slug
  if (!slug) return
  currentPage.value = 1
  loading.value = true
  try {
    if (slug === 'tat-ca') {
      categoryName.value = 'Tất cả sách'
      await fetchAllBooks({ page: 0, size: currentSize.value })
    } else {
      await Promise.all([
        fetchCategoryName(slug),
        fetchBookByCategory(slug, { page: 0, size: currentSize.value }),
      ])
    }
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
          <div class="category-eyebrow">Danh mục</div>
          <h1 class="category-title">
            <template v-if="loading && !categoryName">
              <span class="title-skeleton" />
            </template>
            <template v-else>{{ categoryName }}</template>
          </h1>

          <!-- breadcrumbs -->
          <div class="cat-breadcrumb">
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

      <!-- book count -->
      <div v-if="pagination.totalElements" class="category-count">
        {{ pagination.totalElements }} sách
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
            Danh mục này hiện chưa có sách. Vui lòng thử danh mục khác.
          </p>
          <router-link
            :to="{ name: 'category-page', params: { slug: 'tat-ca' } }"
            class="empty-cta"
          >
            Xem tất cả sách
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

    <!-- Pagination -->
    <div v-if="!loading && pagination.totalPages > 1" class="pagination-wrap">
      <v-pagination
        v-model="currentPage"
        :length="pagination.totalPages"
        :total-visible="4"
        rounded="lg"
        class="category-pagination"
        @update:model-value="onPageChange"
      />
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

.category-count {
  font-size: 0.82rem;
  color: var(--muted);
  background: var(--paper);
  border: 1px solid var(--border);
  border-radius: 99px;
  padding: 4px 14px;
  white-space: nowrap;
  font-weight: 500;
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

.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}

.category-pagination :deep(.v-pagination__item--is-active .v-btn) {
  background: var(--accent) !important;
  color: #fff !important;
}

.category-pagination :deep(.v-btn) {
  color: var(--ink);
  font-weight: 500;
}

.category-pagination :deep(.v-btn:hover) {
  background: var(--accent-soft) !important;
  color: var(--accent) !important;
}

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
.cat-breadcrumb {
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
