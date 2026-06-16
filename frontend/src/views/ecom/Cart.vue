<script setup>
import { useAuthStore } from '@/stores/auth-store'
import { useCartStore } from '@/stores/cart-store'
import { formatPriceVNLocale } from '@/utils/utils'
import { useRouter } from 'vue-router'

const cartStore = useCartStore()
const authStore = useAuthStore()
const router = useRouter()

async function checkout() {
  if (!authStore.isAuthenticated) {
    router.push({ name: 'login' })
  } else {
    router.push({ name: 'checkout' })
  }
}

function decreaseQty(item) {
  if (item.quantity > 1) item.quantity--
}

function increaseQty(item) {
  item.quantity++
}

function clampQty(item) {
  let q = Number(item.quantity)
  if (!q || q < 1) item.quantity = 1
  else item.quantity = q
}
</script>

<template>
  <div class="cart-page">

    <!-- page header -->
    <div class="cart-header">
      <h1 class="cart-heading">Giỏ hàng của bạn</h1>
      <span v-if="cartStore.cartItemsCount > 0" class="cart-count">
        {{ cartStore.cartItemsCount }} sản phẩm
      </span>
    </div>

    <!-- empty state -->
    <div v-if="cartStore.cartItemsCount === 0" class="cart-empty">
      <div class="empty-icon" aria-hidden="true">🛒</div>
      <h2 class="empty-title">Giỏ hàng trống</h2>
      <p class="empty-body">Bạn chưa thêm sản phẩm nào. Hãy khám phá cửa hàng nhé!</p>
      <router-link :to="{ name: 'landing' }" class="empty-cta">
        <v-icon icon="mdi-arrow-left" size="16" class="mr-1" />
        Tiếp tục mua sắm
      </router-link>
    </div>

    <!-- cart content -->
    <template v-else>
      <div class="cart-layout">

        <!-- item table -->
        <div class="cart-items-card">

          <!-- header -->
          <div class="cart-table-head">
            <span class="col-product">Sản phẩm</span>
            <span class="col-price">Đơn giá</span>
            <span class="col-qty">Số lượng</span>
            <span class="col-total">Thành tiền</span>
            <span class="col-remove" aria-hidden="true" />
          </div>

          <!-- items -->
          <div
            v-for="item in cartStore.activeCart"
            :key="item.id"
            class="cart-row"
          >
            <!-- book detail -->
            <div class="col-product cart-product">
              <router-link
                :to="{ name: 'book-detail', params: { id: item.id, slug: item.slug } }"
                class="cart-thumb-link"
                :aria-label="item.title"
              >
                <v-img
                  :src="item.image"
                  :alt="item.title"
                  width="64"
                  height="86"
                  cover
                  class="cart-thumb"
                />
              </router-link>
              <div class="cart-product-info">
                <router-link
                  :to="{ name: 'book-detail', params: { id: item.id, slug: item.slug } }"
                  class="cart-product-title"
                >
                  {{ item.title }}
                </router-link>
                <span v-if="item.author" class="cart-product-author">{{ item.author }}</span>
              </div>
            </div>

            <!-- unit price -->
            <div class="col-price cart-price">
              {{ formatPriceVNLocale(item.price) }}₫
            </div>

            <!-- quantity stepper -->
            <div class="col-qty">
              <div class="qty-stepper">
                <button
                  class="qty-btn"
                  type="button"
                  :disabled="item.quantity <= 1"
                  aria-label="Giảm số lượng"
                  @click="decreaseQty(item)"
                >
                  <v-icon icon="mdi-minus" size="14" />
                </button>
                <input
                  v-model.number="item.quantity"
                  type="number"
                  class="qty-input"
                  min="1"
                  aria-label="Số lượng"
                  @change="clampQty(item)"
                />
                <button
                  class="qty-btn"
                  type="button"
                  aria-label="Tăng số lượng"
                  @click="increaseQty(item)"
                >
                  <v-icon icon="mdi-plus" size="14" />
                </button>
              </div>
            </div>

            <!-- item total -->
            <div class="col-total cart-line-total">
              {{ formatPriceVNLocale(item.price * item.quantity) }}₫
            </div>

            <!-- remove -->
            <div class="col-remove">
              <button
                class="remove-btn"
                type="button"
                :aria-label="`Xoá ${item.title}`"
                @click="cartStore.removeItemFromCart({ itemId: item.id })"
              >
                <v-icon icon="mdi-close" size="16" />
              </button>
            </div>
          </div>

          <!-- continue shopping -->
          <div class="cart-footer-link">
            <router-link :to="{ name: 'landing' }" class="continue-link">
              <v-icon icon="mdi-arrow-left" size="15" class="mr-1" />
              Tiếp tục mua sắm
            </router-link>
          </div>
        </div>

        <!-- order summary -->
        <aside class="cart-summary" aria-label="Tóm tắt đơn hàng">
          <h2 class="summary-heading">Tóm tắt đơn hàng</h2>

          <div class="summary-line">
            <span>Tạm tính ({{ cartStore.cartItemsCount }} sản phẩm)</span>
            <span>{{ formatPriceVNLocale(cartStore.totalAmount) }}₫</span>
          </div>

          <div class="summary-line">
            <span>Phí vận chuyển</span>
            <span class="summary-free">Miễn phí</span>
          </div>

          <div class="summary-divider" />

          <div class="summary-total">
            <span>Tổng cộng</span>
            <span class="summary-total-price">
              {{ formatPriceVNLocale(cartStore.totalAmount) }}₫
            </span>
          </div>

          <button class="checkout-btn" type="button" @click="checkout">
            <v-icon icon="mdi-lock-outline" size="16" class="mr-2" />
            Tiến hành thanh toán
          </button>

          <p class="summary-note">
            <v-icon icon="mdi-shield-check-outline" size="14" class="mr-1" />
            Thanh toán an toàn & bảo mật
          </p>
        </aside>

      </div>
    </template>
  </div>
</template>

<style scoped>

.cart-page {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;

  max-width: 1100px;
  margin: 0 auto;
  padding: 32px 24px 64px;
  color: var(--ink);
  font-size: 0.9rem;
}

/* ── Page header ────────────────────────────────────── */
.cart-header {
  display: flex;
  align-items: baseline;
  gap: 12px;
  margin-bottom: 28px;
}
.cart-heading {
  font-size: clamp(1.4rem, 2vw, 1.85rem);
  font-weight: 700;
  margin: 0;
}
.cart-count {
  font-size: 0.875rem;
  color: var(--muted);
}

/* empty state */
.cart-empty {
  text-align: center;
  padding: 72px 24px;
  background: #fff;
  border: 1px solid var(--border);
  border-radius: 12px;
}
.empty-icon { font-size: 3rem; margin-bottom: 16px; }
.empty-title {
  font-size: 1.3rem;
  font-weight: 600;
  margin: 0 0 8px;
}
.empty-body {
  color: var(--muted);
  margin: 0 0 24px;
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

/* layout */
.cart-layout {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 24px;
  align-items: start;
}

/* horizontal book card */
.cart-items-card {
  background: #fff;
  border: 1px solid var(--border);
  border-radius: 12px;
  overflow: hidden;
}

/* table head */
.cart-table-head {
  display: grid;
  grid-template-columns: 1fr 110px 130px 110px 40px;
  gap: 8px;
  padding: 12px 20px;
  background: var(--paper);
  border-bottom: 1px solid var(--border);
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.07em;
  color: var(--muted);
  font-weight: 600;
}

/* item row */
.cart-row {
  display: grid;
  grid-template-columns: 1fr 110px 130px 110px 40px;
  gap: 8px;
  align-items: center;
  padding: 16px 20px;
  border-bottom: 1px solid var(--border);
  transition: background 0.15s;
}
.cart-row:last-of-type { border-bottom: none; }
.cart-row:hover { background: var(--paper); }

/* column helper */
.col-price, .col-total { text-align: right; }
.col-qty { display: flex; justify-content: center; }
.col-remove { display: flex; justify-content: center; }

/* product cell */
.cart-product {
  display: flex;
  align-items: center;
  gap: 14px;
  min-width: 0;
}
.cart-thumb-link { flex-shrink: 0; }
.cart-thumb {
  border-radius: 6px;
  border: 1px solid var(--border);
}
.cart-product-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
  min-width: 0;
}
.cart-product-title {
  text-decoration: none;
  color: var(--ink);
  font-weight: 600;
  font-size: 0.9rem;
  line-height: 1.35;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color 0.15s;
}
.cart-product-title:hover { color: var(--accent); }
.cart-product-author {
  font-size: 0.8rem;
  color: var(--muted);
}

/* price */
.cart-price { color: var(--ink); }
.cart-line-total { font-weight: 700; color: var(--accent); }

/* quantity setter */
.qty-stepper {
  display: flex;
  align-items: center;
  border: 1px solid var(--border);
  border-radius: 6px;
  overflow: hidden;
  background: #fff;
}
.qty-btn {
  width: 30px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: none;
  cursor: pointer;
  color: var(--ink);
  transition: background 0.15s;
}
.qty-btn:hover:not(:disabled) { background: var(--accent-soft); }
.qty-btn:disabled { color: var(--border); cursor: not-allowed; }
.qty-input {
  width: 38px;
  height: 32px;
  text-align: center;
  border: none;
  border-left: 1px solid var(--border);
  border-right: 1px solid var(--border);
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--ink);
  background: transparent;
}
.qty-input:focus { outline: 2px solid var(--accent); outline-offset: -2px; }
.qty-input::-webkit-inner-spin-button,
.qty-input::-webkit-outer-spin-button { -webkit-appearance: none; }
.qty-input[type='number'] { -moz-appearance: textfield; }

/* remove btn */
.remove-btn {
  width: 28px;
  height: 28px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent;
  border: 1px solid var(--border);
  border-radius: 50%;
  cursor: pointer;
  color: var(--muted);
  transition: border-color 0.15s, color 0.15s, background 0.15s;
}
.remove-btn:hover {
  border-color: var(--accent);
  color: var(--accent);
  background: var(--accent-soft);
}
.remove-btn:focus-visible { outline: 2px solid var(--accent); outline-offset: 2px; }

/* continue link */
.cart-footer-link {
  padding: 14px 20px;
  border-top: 1px solid var(--border);
  background: var(--paper);
}
.continue-link {
  display: inline-flex;
  align-items: center;
  font-size: 0.85rem;
  color: var(--muted);
  text-decoration: none;
  transition: color 0.15s;
}
.continue-link:hover { color: var(--accent); }

/* order summary */
.cart-summary {
  background: #fff;
  border: 1px solid var(--border);
  border-radius: 12px;
  padding: 24px;
  position: sticky;
  top: 80px;
}
.summary-heading {
  font-size: 1.05rem;
  font-weight: 600;
  margin: 0 0 20px;
}
.summary-line {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
  font-size: 0.875rem;
  color: var(--muted);
}
.summary-free {
  color: #2a9d5c;
  font-weight: 600;
}
.summary-divider {
  height: 1px;
  background: var(--border);
  margin: 16px 0;
}
.summary-total {
  display: flex;
  justify-content: space-between;
  align-items: baseline;
  margin-bottom: 20px;
  font-weight: 700;
}
.summary-total-price {
  font-size: 1.35rem;
  color: var(--accent);
}
.checkout-btn {
  width: 100%;
  height: 48px;
  background: var(--accent);
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 700;
  letter-spacing: 0.02em;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s;
}
.checkout-btn:hover { background: #8e1f24; }
.checkout-btn:focus-visible { outline: 2px solid var(--accent); outline-offset: 2px; }
.summary-note {
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 12px 0 0;
  font-size: 0.78rem;
  color: var(--muted);
}

/* responsive */
@media (max-width: 860px) {
  .cart-layout {
    grid-template-columns: 1fr;
  }
  .cart-summary {
    position: static;
  }
  .cart-table-head {
    display: none; /* show labels inline on mobile */
  }
  .cart-row {
    grid-template-columns: 1fr 1fr;
    grid-template-areas:
      "product product"
      "price   qty"
      "total   remove";
    gap: 12px;
    padding: 16px;
  }
  .col-product  { grid-area: product; }
  .col-price    { grid-area: price; text-align: left; }
  .col-qty      { grid-area: qty; justify-content: flex-end; }
  .col-total    { grid-area: total; text-align: left; font-size: 1rem; }
  .col-remove   { grid-area: remove; justify-content: flex-end; }
}

@media (max-width: 480px) {
  .cart-page { padding: 20px 16px 48px; }
}
</style>