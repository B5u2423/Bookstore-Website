<script setup>
import { OrderService } from '@/api/cart-api'
import { formatPriceVNLocale } from '@/utils/utils'
import { onMounted, ref } from 'vue'

const items = ref([])
const loading = ref(true)
const expandedId = ref(null)

async function fetchOrderHistory() {
  loading.value = true
  try {
    const res = await OrderService.getAllOrdersByEmail()
    items.value = res.content
  } catch (error) {
    console.error('Error fetching user order history', error)
  } finally {
    loading.value = false
  }
}

function toggleExpand(id) {
  expandedId.value = expandedId.value === id ? null : id
}

const STATUS_CONFIG = {
  PENDING:   { label: 'Đang xử lý',      icon: 'mdi-clock-outline',         color: '#b45309', bg: '#fef3c7', border: '#fde68a' },
  PAID:      { label: 'Đã thanh toán',   icon: 'mdi-check-circle-outline',  color: '#2a9d5c', bg: '#f0fdf6', border: '#bbf0d4' },
  CANCELLED: { label: 'Đã hủy',          icon: 'mdi-close-circle-outline',  color: '#a3262c', bg: '#f7e9e8', border: '#fcd4d4' },
}

function getStatus(status) {
  return STATUS_CONFIG[String(status)] || { label: status, icon: 'mdi-help-circle-outline', color: '#8a7d72', bg: '#fbf8f4', border: '#e8e1d8' }
}

function formatDate(dateStr) {
  if (!dateStr) return '—'
  return new Intl.DateTimeFormat('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' }).format(new Date(dateStr))
}

const PAYMENT_LABELS = {
  COD:   'Thanh toán khi nhận hàng',
  VNPAY: 'VNPay',
  CARD:  'Thẻ ngân hàng',
}
function getPaymentLabel(method) {
  return PAYMENT_LABELS[String(method)] || method
}

onMounted(fetchOrderHistory)
</script>

<template>
  <div class="order-history">

    <!-- Loading skeletons -->
    <template v-if="loading">
      <div v-for="n in 3" :key="n" class="order-skeleton">
        <div class="skel-line skel-line--short" />
        <div class="skel-line skel-line--medium" />
        <div class="skel-line skel-line--long" />
      </div>
    </template>

    <!-- Empty state -->
    <div v-else-if="!items.length" class="order-empty">
      <v-icon icon="mdi-receipt-text-outline" size="40" class="mb-3" />
      <p class="order-empty-title">Chưa có đơn hàng nào</p>
      <p class="order-empty-body">Các đơn hàng của bạn sẽ xuất hiện ở đây sau khi đặt hàng.</p>
      <router-link :to="{ name: 'landing' }" class="order-empty-cta">
        <v-icon icon="mdi-storefront-outline" size="16" class="mr-1" />
        Khám phá sách
      </router-link>
    </div>

    <!-- Order list -->
    <template v-else>
      <div
        v-for="order in items"
        :key="order.id"
        class="order-card"
        :class="{ 'order-card--open': expandedId === order.id }"
      >
        <!-- Order summary row -->
        <div class="order-row" @click="toggleExpand(order.id)" role="button" :aria-expanded="expandedId === order.id" tabindex="0" @keydown.enter="toggleExpand(order.id)" @keydown.space.prevent="toggleExpand(order.id)">

          <!-- Status badge -->
          <div
            class="status-badge"
            :style="{
              color: getStatus(order.orderStatus).color,
              background: getStatus(order.orderStatus).bg,
              borderColor: getStatus(order.orderStatus).border,
            }"
          >
            <v-icon :icon="getStatus(order.orderStatus).icon" size="14" class="mr-1" />
            {{ getStatus(order.orderStatus).label }}
          </div>

          <!-- Meta info -->
          <div class="order-meta">
            <div class="order-meta-item">
              <v-icon icon="mdi-calendar-outline" size="14" class="mr-1" />
              {{ formatDate(order.orderDate) }}
            </div>
            <div class="order-meta-item">
              <v-icon icon="mdi-credit-card-outline" size="14" class="mr-1" />
              {{ getPaymentLabel(order.paymentMethod) }}
            </div>
            <div class="order-meta-item order-meta-item--total">
              <v-icon icon="mdi-cash-multiple" size="14" class="mr-1" />
              {{ formatPriceVNLocale(order.orderTotal) }} ₫
            </div>
          </div>

          <!-- Expand toggle -->
          <div class="order-toggle" aria-hidden="true">
            <v-icon
              :icon="expandedId === order.id ? 'mdi-chevron-up' : 'mdi-chevron-down'"
              size="20"
            />
          </div>

        </div>

        <!-- Expanded detail panel -->
        <Transition name="order-expand">
          <div v-if="expandedId === order.id" class="order-detail">

            <!-- Delivery info -->
            <div class="detail-section">
              <h4 class="detail-section-title">
                <v-icon icon="mdi-truck-delivery-outline" size="15" class="mr-1" />
                Thông tin giao hàng
              </h4>
              <div class="detail-grid">
                <div class="detail-row">
                  <span class="detail-label">Người nhận</span>
                  <span class="detail-value">{{ order.name }}</span>
                </div>
                <div class="detail-row">
                  <span class="detail-label">Địa chỉ</span>
                  <span class="detail-value">{{ [order.street, order.commune, order.city].filter(Boolean).join(', ') }}</span>
                </div>
              </div>
            </div>

            <!-- Price breakdown -->
            <div class="detail-section">
              <h4 class="detail-section-title">
                <v-icon icon="mdi-receipt-text-outline" size="15" class="mr-1" />
                Chi tiết thanh toán
              </h4>
              <div class="detail-grid">
                <div class="detail-row">
                  <span class="detail-label">Tiền sản phẩm</span>
                  <span class="detail-value">{{ formatPriceVNLocale(order.itemsTotal) }} ₫</span>
                </div>
                <div class="detail-row">
                  <span class="detail-label">Phí vận chuyển</span>
                  <span class="detail-value" :class="order.shippingFee === 0 ? 'detail-value--free' : ''">
                    {{ order.shippingFee === 0 ? 'Miễn phí' : formatPriceVNLocale(order.shippingFee) + ' ₫' }}
                  </span>
                </div>
                <div class="detail-row detail-row--total">
                  <span class="detail-label">Tổng cộng</span>
                  <span class="detail-value detail-value--price">{{ formatPriceVNLocale(order.orderTotal) }} ₫</span>
                </div>
              </div>
            </div>

            <!-- Book items -->
            <div class="detail-section">
              <h4 class="detail-section-title">
                <v-icon icon="mdi-bookshelf" size="15" class="mr-1" />
                Sản phẩm ({{ order.items?.length || 0 }})
              </h4>
              <div class="book-list">
                <div
                  v-for="book in order.items"
                  :key="book.isbn"
                  class="book-item"
                >
                  <div class="book-cover-placeholder" aria-hidden="true">
                    <v-icon icon="mdi-book-outline" size="20" />
                  </div>
                  <div class="book-info">
                    <p class="book-title">{{ book.titleAtPurchase }}</p>
                    <p class="book-meta">ISBN: {{ book.isbn }}</p>
                  </div>
                  <div class="book-pricing">
                    <p class="book-price">{{ formatPriceVNLocale(book.priceAtPurchase) }} ₫</p>
                    <p class="book-qty">× {{ book.quantity }}</p>
                  </div>
                </div>
              </div>
            </div>

          </div>
        </Transition>
      </div>
    </template>
  </div>
</template>

<style scoped>
.order-history {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;
  --green:       #2a9d5c;

  display: flex;
  flex-direction: column;
  color: var(--ink);
  font-family: system-ui, -apple-system, sans-serif;
  font-size: 0.875rem;
}

.order-skeleton {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 20px 24px;
  border-bottom: 1px solid var(--border);
}
.skel-line {
  height: 12px;
  border-radius: 6px;
  background: linear-gradient(90deg, var(--border) 25%, #f0ebe4 50%, var(--border) 75%);
  background-size: 200% 100%;
  animation: shimmer 1.4s infinite;
}
.skel-line--short  { width: 28%; }
.skel-line--medium { width: 50%; }
.skel-line--long   { width: 72%; }
@keyframes shimmer {
  0%   { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

.order-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 52px 24px;
  color: var(--muted);
}
.order-empty-title {
  font-size: 1.05rem;
  font-weight: 600;
  color: var(--ink);
  margin: 0 0 6px;
}
.order-empty-body { margin: 0 0 20px; font-size: 0.875rem; }
.order-empty-cta {
  display: inline-flex;
  align-items: center;
  padding: 9px 20px;
  background: var(--accent);
  color: #fff;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 600;
  font-size: 0.875rem;
  transition: background 0.2s;
}
.order-empty-cta:hover { background: #8e1f24; }

.order-card {
  border-bottom: 1px solid var(--border);
  transition: background 0.15s;
}
.order-card:last-child { border-bottom: none; }
.order-card--open { background: var(--paper); }

/* Summary row */
.order-row {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 24px;
  cursor: pointer;
  user-select: none;
  transition: background 0.15s;
}
.order-row:hover { background: var(--paper); }
.order-row:focus-visible { outline: 2px solid var(--accent); outline-offset: -2px; }

/* Status badge */
.status-badge {
  display: inline-flex;
  align-items: center;
  padding: 4px 10px;
  border-radius: 99px;
  border: 1px solid;
  font-size: 0.775rem;
  font-weight: 600;
  white-space: nowrap;
  flex-shrink: 0;
}

/* Meta */
.order-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  flex: 1;
  flex-wrap: wrap;
}
.order-meta-item {
  display: flex;
  align-items: center;
  color: var(--muted);
  font-size: 0.82rem;
  white-space: nowrap;
}
.order-meta-item--total {
  font-weight: 700;
  color: var(--ink);
}
.order-toggle { color: var(--muted); flex-shrink: 0; }

.order-detail {
  border-top: 1px solid var(--border);
  background: #fff;
}
.detail-section {
  padding: 18px 24px;
  border-bottom: 1px solid var(--border);
}
.detail-section:last-child { border-bottom: none; }
.detail-section-title {
  display: flex;
  align-items: center;
  font-size: 0.78rem;
  text-transform: uppercase;
  letter-spacing: 0.07em;
  font-weight: 700;
  color: var(--muted);
  margin: 0 0 12px;
}

/* Detail grid */
.detail-grid { display: flex; flex-direction: column; gap: 8px; }
.detail-row {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  gap: 12px;
  padding: 6px 0;
}
.detail-row + .detail-row { border-top: 1px solid var(--border); }
.detail-row--total {
  padding-top: 10px;
  margin-top: 2px;
}
.detail-label {
  font-size: 0.82rem;
  color: var(--muted);
  white-space: nowrap;
}
.detail-value {
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--ink);
  text-align: right;
}
.detail-value--free { color: var(--green); font-weight: 600; }
.detail-value--price {
  font-size: 1.05rem;
  font-weight: 700;
  color: var(--accent);
}

/* Book list */
.book-list { display: flex; flex-direction: column; gap: 10px; }
.book-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: var(--paper);
  border: 1px solid var(--border);
  border-radius: 8px;
}
.book-cover-placeholder {
  width: 40px;
  height: 54px;
  border-radius: 5px;
  background: var(--accent-soft);
  color: var(--accent);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.book-info { flex: 1; min-width: 0; }
.book-title {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--ink);
  margin: 0 0 3px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.book-meta { font-size: 0.775rem; color: var(--muted); margin: 0; }
.book-pricing { text-align: right; flex-shrink: 0; }
.book-price { font-weight: 700; color: var(--ink); margin: 0 0 2px; }
.book-qty { font-size: 0.775rem; color: var(--muted); margin: 0; }

.order-expand-enter-active,
.order-expand-leave-active {
  transition: max-height 0.3s ease, opacity 0.25s ease;
  overflow: hidden;
  max-height: 1200px;
}
.order-expand-enter-from,
.order-expand-leave-to {
  max-height: 0;
  opacity: 0;
}

@media (max-width: 600px) {
  .order-row { padding: 14px 16px; gap: 10px; flex-wrap: wrap; }
  .order-meta { gap: 10px; }
  .order-meta-item { font-size: 0.775rem; }
  .detail-section { padding: 14px 16px; }
}
</style>