<script setup>
import { BookService } from '@/api/book-api'
import { CartService } from '@/api/cart-api.js'
import router from '@/router'
import { useAuthStore } from '@/stores/auth-store.js'
import { useCartStore } from '@/stores/cart-store'
import { formatPriceVNLocale } from '@/utils/utils'
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

const cartStore = useCartStore()
const authStore = useAuthStore()
const route = useRoute()

const loading = ref(true)
const quantity = ref(1)
const snackbar = ref(false)
const snackbarText = ref('')
const showFullDescription = ref(false)

const DESCRIPTION_PREVIEW_LENGTH = 320
const BOOK_TITLE_LENGTH = 80
const catRoute = ref('/')

const book = ref({
  author: '',
  description: '',
  id: '',
  imageUrl: '',
  inStock: 0,
  isbn: '',
  pageCount: '',
  price: 0,
  publishYear: '',
  publisher: '',
  title: '',
  urlSlug: '',
})

const isOutOfStock = computed(() => book.value.inStock < 1)
const buttonText = computed(() => (isOutOfStock.value ? 'Hết hàng' : 'Thêm vào giỏ hàng'))

const descriptionIsLong = computed(
  () => (book.value.description?.length || 0) > DESCRIPTION_PREVIEW_LENGTH,
)
const displayedDescription = computed(() => {
  if (!descriptionIsLong.value || showFullDescription.value) return book.value.description
  return book.value.description.slice(0, DESCRIPTION_PREVIEW_LENGTH).trimEnd() + '…'
})

async function loadBookDetail() {
  loading.value = true
  try {
    book.value = await BookService.fetchBookById(route.params.id)
    // bind the value of category breadcrumb
    catRoute.value = convertToSlug(book.value.categoryName)
  } catch (error) {
    console.error('Error fetching book', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadBookDetail()
})

function decreaseQuantity() {
  if (quantity.value > 1) quantity.value--
}

function increaseQuantity() {
  if (quantity.value < book.value.inStock) quantity.value++
}

function clampQuantity() {
  let q = Number(quantity.value)
  if (!q || q < 1) q = 1
  if (book.value.inStock && q > book.value.inStock) q = book.value.inStock
  quantity.value = q
}

async function handleAddToCart() {
  cartStore.addItemToLocalCart({
    id: book.value.id,
    title: book.value.title,
    author: book.value.author,
    price: book.value.price,
    slug: book.value.urlSlug,
    image: book.value.imageUrl,
    quantity: quantity.value,
  })

  // call the endpoint directly
  if (authStore.isAuthenticated) {
    try {
      await CartService.addToCart({
        bookId: book.value.id,
        quantity: quantity.value,
      })
    } catch (error) {
      console.error('Error adding new book when already logged in', error)
    }
  }

  snackbarText.value = `Đã thêm ${quantity.value} "${book.value.title}" vào giỏ hàng`
  snackbar.value = true
}

function convertToSlug(cat) {
  if (cat == null) {
    throw new Error('Input string must not be null')
  }

  return cat
    .normalize('NFD') // normalize
    .replace(/[\u0300-\u036f]+/g, '') // remove diacritics
    .replace(/Đ/g, 'D') // remove diacritics
    .replace(/đ/g, 'd') // remove diacritics
    .toLowerCase()
    .replace(/[^a-z0-9]+/g, '-') // replace non-alphanumeric chars with hyphens
    .replace(/^-+|-+$/g, '') // trim leading/trailing hyphens
}
const breadCrumbTitleIsLong = computed(
  () => (book.value?.title?.length || 0) > BOOK_TITLE_LENGTH,
)
const displayedBreadCrumbTitle = computed(() => {
  if (!breadCrumbTitleIsLong.value || 0) return book.value.title
  return book.value.title.slice(0, BOOK_TITLE_LENGTH).trimEnd() + '…'
})
</script>

<template>
  <v-container class="book-detail py-8">
    <!-- breadcrumbs -->
    <div class="detail-breadcrumb">
      <router-link
        to="/"
        class="breadcrumb-link"
      >
        Trang chủ
      </router-link>
      <v-icon icon="mdi-chevron-right" size="18" class="breadcrumb-sep" />
      <router-link
        :to="{ name: 'category-page', params: { slug: catRoute } }"
        class="breadcrumb-link"
      >
        {{ book.categoryName || 'Danh mục sách' }}
      </router-link>
      <v-icon icon="mdi-chevron-right" size="18" class="breadcrumb-sep" />
      <span>{{ displayedBreadCrumbTitle || 'Sách' }}</span>
    </div>

    <!-- loading skeleton -->
    <v-row v-if="loading" class="mt-4">
      <v-col cols="12" md="5" class="d-flex justify-center">
        <v-skeleton-loader
          type="image"
          width="100%"
          max-width="320"
          height="420"
          class="rounded-lg"
        />
      </v-col>
      <v-col cols="12" md="7">
        <v-skeleton-loader type="article, divider, list-item-three-line, actions" />
      </v-col>
    </v-row>

    <!-- book detail -->
    <v-row v-else class="mt-2">
      <!-- cover image -->
      <v-col cols="12" md="5" class="d-flex justify-center align-start">
        <div class="cover-wrap">
          <v-img :src="book.imageUrl" :alt="book.title" class="cover-img" cover />
        </div>
      </v-col>

      <!-- other book details -->
      <v-col cols="12" md="7">
        <div class="info-col">
          <h1 class="book-title">{{ book.title }}</h1>

          <!-- author (subtitle) -->
          <p class="book-author">
            <v-icon icon="mdi-account-outline" size="16" class="mr-1" />
            {{ book.author }}
          </p>

          <!-- price -->
          <div class="price-row">
            <span class="price-currency">VND</span>
            <span class="price">{{ formatPriceVNLocale(book.price) }}</span>
            <v-chip
              :color="isOutOfStock ? 'error' : 'success'"
              variant="flat"
              size="small"
              class="ml-3 stock-chip"
            >
              {{ isOutOfStock ? 'Hết hàng' : `Còn ${book.inStock} sản phẩm` }}
            </v-chip>
          </div>

          <v-divider class="my-5" />

          <!-- book details -->
          <dl class="spec-list">
            <div class="spec-item">
              <dt><v-icon icon="mdi-barcode" size="16" class="mr-1" />ISBN</dt>
              <dd>{{ book.isbn }}</dd>
            </div>
            <div class="spec-item">
              <dt><v-icon icon="mdi-domain" size="16" class="mr-1" />Nhà xuất bản</dt>
              <dd>{{ book.publisher }}</dd>
            </div>
            <div class="spec-item">
              <dt><v-icon icon="mdi-calendar-blank" size="16" class="mr-1" />Năm xuất bản</dt>
              <dd>{{ book.publishYear }}</dd>
            </div>
            <div class="spec-item" v-if="book.pageCount">
              <dt>
                <v-icon icon="mdi-book-open-page-variant-outline" size="16" class="mr-1" />Số
                trang
              </dt>
              <dd>{{ book.pageCount }}</dd>
            </div>
          </dl>

          <v-divider class="my-5" />

          <!-- purchase row -->
          <div class="purchase-row">
            <div class="qty-stepper" :class="{ 'is-disabled': isOutOfStock }">
              <button
                class="qty-btn"
                type="button"
                :disabled="isOutOfStock || quantity <= 1"
                aria-label="Giảm số lượng"
                @click="decreaseQuantity"
              >
                <v-icon icon="mdi-minus" size="18" />
              </button>

              <input
                v-model.number="quantity"
                type="number"
                class="qty-input"
                :min="1"
                :max="book.inStock"
                :disabled="isOutOfStock"
                aria-label="Số lượng"
                @change="clampQuantity"
              />

              <button
                class="qty-btn"
                type="button"
                :disabled="isOutOfStock || quantity >= book.inStock"
                aria-label="Tăng số lượng"
                @click="increaseQuantity"
              >
                <v-icon icon="mdi-plus" size="18" />
              </button>
            </div>

            <v-btn
              class="add-to-cart-btn"
              size="large"
              elevation="0"
              :disabled="isOutOfStock"
              @click="handleAddToCart"
            >
              <v-icon icon="mdi-cart-plus" class="mr-2" />
              {{ buttonText }}
            </v-btn>
          </div>
        </div>
      </v-col>
    </v-row>

    <!-- desc -->
    <v-row v-if="!loading" class="mt-8">
      <v-col cols="12">
        <v-card variant="flat" class="description-card">
          <h2 class="section-heading">Giới thiệu sách</h2>
          <p class="description-text">{{ displayedDescription }}</p>
          <button
            v-if="descriptionIsLong"
            class="toggle-desc"
            type="button"
            @click="showFullDescription = !showFullDescription"
          >
            {{ showFullDescription ? 'Thu gọn' : 'Xem thêm' }}
            <v-icon
              :icon="showFullDescription ? 'mdi-chevron-up' : 'mdi-chevron-down'"
              size="18"
            />
          </button>
        </v-card>
      </v-col>
    </v-row>

    <!-- notif when add item to cart -->
    <v-snackbar v-model="snackbar" timeout="2500" location="bottom right">
      <div class="d-flex align-center">
        <v-icon icon="mdi-check-circle" color="success" class="mr-2" />
        {{ snackbarText }}
      </div>
    </v-snackbar>
  </v-container>
</template>

<style scoped>
.book-detail {
  --accent: #a3262c;
  --accent-soft: #f7e9e8;
  --ink: #2b2420;
  --muted: #8a7d72;
  --border: #e8e1d8;
  --paper: #fbf8f4;
  max-width: 1100px;
  color: var(--ink);
}

/* back link */
.back-link {
  display: inline-flex;
  align-items: center;
  font-size: 0.875rem;
  color: var(--muted);
  text-decoration: none;
  transition: color 0.2s ease;
}
.back-link:hover {
  color: var(--accent);
}

/* cover */
.cover-wrap {
  position: relative;
  width: 100%;
  max-width: 320px;
  border-radius: 12px;
  overflow: hidden;
  box-shadow:
    0 24px 48px -24px rgba(43, 36, 32, 0.35),
    0 2px 8px rgba(43, 36, 32, 0.08);
  transition: transform 0.35s ease, box-shadow 0.35s ease;
}
.cover-wrap:hover {
  transform: translateY(-4px);
  box-shadow:
    0 32px 56px -20px rgba(43, 36, 32, 0.4),
    0 4px 10px rgba(43, 36, 32, 0.1);
}
.cover-wrap::after {
  content: '';
  position: absolute;
  inset: 0;
  border-radius: 12px;
  border: 1px solid rgba(43, 36, 32, 0.06);
  pointer-events: none;
}
.cover-img {
  aspect-ratio: 3 / 4;
  border-radius: 12px;
}

/* book details */
.info-col {
  border-left: 3px solid var(--accent);
  padding-left: 20px;
}

.book-title {
  font-weight: 700;
  font-size: clamp(1.6rem, 2.4vw, 2.25rem);
  line-height: 1.25;
  margin: 0 0 8px;
}

.book-author {
  display: flex;
  align-items: center;
  color: var(--muted);
  font-size: 1rem;
  margin: 0 0 20px;
}

/* price */
.price-row {
  display: flex;
  align-items: baseline;
  flex-wrap: wrap;
}
.price-currency {
  font-size: 0.9rem;
  color: var(--muted);
  margin-right: 6px;
}
.price {
  font-weight: 700;
  font-size: 2rem;
  color: var(--accent);
}
.stock-chip {
  font-weight: 600;
}

/* spec list */
.spec-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(170px, 1fr));
  gap: 14px 24px;
  margin: 0;
}
.spec-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}
.spec-item dt {
  display: flex;
  align-items: center;
  font-size: 0.78rem;
  color: var(--muted);
  text-transform: uppercase;
  letter-spacing: 0.04em;
}
.spec-item dd {
  font-size: 0.95rem;
  font-weight: 500;
  margin: 0;
}

/* purchase btn row */
.purchase-row {
  display: flex;
  align-items: stretch;
  gap: 12px;
  flex-wrap: wrap;
}

.qty-stepper {
  display: flex;
  align-items: center;
  border: 1px solid var(--border);
  border-radius: 8px;
  overflow: hidden;
  background: #fff;
}
.qty-stepper.is-disabled {
  opacity: 0.5;
}
.qty-btn {
  background: transparent;
  border: none;
  width: 44px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: var(--ink);
  transition: background 0.15s ease;
}
.qty-btn:hover:not(:disabled) {
  background: var(--accent-soft);
}
.qty-btn:disabled {
  cursor: not-allowed;
  color: var(--muted);
}
.qty-input {
  width: 52px;
  height: 48px;
  text-align: center;
  border: none;
  border-left: 1px solid var(--border);
  border-right: 1px solid var(--border);
  font-size: 1rem;
  font-weight: 600;
  background: transparent;
  color: var(--ink);
}
.qty-input:focus {
  outline: 2px solid var(--accent);
  outline-offset: -2px;
}
.qty-input::-webkit-outer-spin-button,
.qty-input::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
.qty-input[type='number'] {
  -moz-appearance: textfield;
}

.add-to-cart-btn {
  flex: 1 1 220px;
  background-color: var(--accent) !important;
  color: #fff !important;
  font-weight: 600;
  letter-spacing: 0.02em;
  border-radius: 8px;
  text-transform: none;
  height: 48px !important;
}
.add-to-cart-btn.v-btn--disabled {
  background-color: var(--border) !important;
  color: var(--muted) !important;
  opacity: 1 !important;
}

/* description */
.description-card {
  background: var(--paper);
  border-radius: 12px;
  padding: 28px;
}
.section-heading {
  font-size: 1.25rem;
  font-weight: 600;
  margin: 0 0 12px;
}
.description-text {
  line-height: 1.7;
  color: var(--ink);
  white-space: pre-line;
  margin: 0;
}
.toggle-desc {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-top: 12px;
  background: none;
  border: none;
  color: var(--accent);
  font-weight: 600;
  cursor: pointer;
  padding: 0;
}
.toggle-desc:focus-visible,
.qty-btn:focus-visible,
.back-link:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
}

@media (max-width: 600px) {
  .info-col {
    border-left: none;
    border-top: 3px solid var(--accent);
    padding-left: 0;
    padding-top: 16px;
    margin-top: 16px;
  }
  .purchase-row {
    flex-direction: column;
  }
  .qty-stepper {
    align-self: flex-start;
  }
  .description-card {
    padding: 20px;
  }
}

/* breadcrumbs */
.detail-breadcrumb {
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
.breadcrumb-sep { color: black; }
</style>
