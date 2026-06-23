<script setup>
import { AddressInfoService, OrderService, PaymentService } from '@/api/cart-api'
import { CouponService } from '@/api/coupon-api'
import HorizontalBookCard from '@/components/books/HorizontalBookCard.vue'
import SnackBarOnFailure from '@/components/common/SnackBarOnFailure.vue'
import SnackBarOnSuccess from '@/components/common/SnackBarOnSuccess.vue'
import router from '@/router'
import { useCartStore } from '@/stores/cart-store'
import { useUserProfileStore } from '@/stores/user-profile-store'
import { formatPriceVNLocale } from '@/utils/utils'
import { computed, onMounted, ref, watch } from 'vue'

const cartStore = useCartStore()
const userProfileStore = useUserProfileStore()

// snackbars
const isError = ref(false)
const isSuccess = ref(false)
const message = ref('')

// drawer
const isCouponDrawerOpen = ref(false)
const availableCoupons = ref([])
const isFetchingCoupons = ref(false)

async function openCouponDrawer() {
  isCouponDrawerOpen.value = true
  if (availableCoupons.value.length) return // already loaded
  isFetchingCoupons.value = true
  try {
    const res = await CouponService.getAvailableCoupons({
      itemsTotal: shippingInfo.value.itemsTotal,
    })
    // expect: [{ code, discountType:'PERCENT'|'FIXED', discountValue,
    //            startDate, expirationDate, usedCount, maxUsage }]
    availableCoupons.value = res.data ?? res ?? []
  } catch (e) {
    console.error('Cannot fetch coupons', e)
    // fallback demo data so the drawer still renders nicely in dev
    availableCoupons.value = []
  } finally {
    isFetchingCoupons.value = false
  }
}

function closeCouponDrawer() {
  isCouponDrawerOpen.value = false
}

function selectCoupon(code) {
  shippingInfo.value.couponCode = code
  closeCouponDrawer()
  applyCoupon()
}

// Formats discount label: "20%" or "50.000 ₫"
function formatDiscount(coupon) {
  if (coupon.discountType === 'PERCENT') return `${coupon.discountValue}%`
  return `${formatPriceVNLocale(coupon.discountValue)} ₫`
}

// return 0-100 usage percentage
function usagePct(coupon) {
  if (!coupon.maxUsage) return 0
  return Math.min(100, Math.round((coupon.usedCount / coupon.maxUsage) * 100))
}

// Nicely formatted Vietnamese short date
function fmtDate(iso) {
  if (!iso) return '—'
  return new Date(iso).toLocaleDateString('vi-VN', {
    day: '2-digit',
    month: '2-digit',
    year: 'numeric',
  })
}

// shipping constants
const HANOI_SHIPPING_FEE = ref(0)
const OTHERS_SHIPPING_FEE = ref(0)
const FREE_SHIP_REQ = ref(0)

function bootStrapValues() {
  return {
    cityId: null,
    communeId: null,
    cityName: '',
    communeName: '',
    street: '',
    itemsTotal: cartStore.totalAmount,
    isFreeShip: false,
    shippingFee: 0,
    discountValue: 0,
    orderTotal: 0,
    couponCode: '',
    email: userProfileStore.userInfo.email,
    phone: userProfileStore.userInfo.phone,
    name: userProfileStore.userInfo.name,
    paymentMethod: 'COD',
    info: '',
    vnpTxnRef: '',
  }
}

const cities = ref([])
const communes = ref([])
const shippingInfo = ref(bootStrapValues())
const selectedAddrId = ref(null)
const isPlacingOrder = ref(false) // ~ loading

const rules = {
  required: (v) => !!v || 'Không được bỏ trống trường này',
  phone: (v) => /^\d+$/.test(v) || 'Số điện thoại không hợp lệ',
}

async function getAllData() {
  await Promise.all([
    fetchCities(),
    userProfileStore.getUserInfo(),
    fetchShippingFeeInfo(),
  ])
}

async function fetchShippingFeeInfo() {
  try {
    const payload = await OrderService.getShippingFeeInfo()
    // TODO: may need revisit
    FREE_SHIP_REQ.value = payload['FREE_SHIP_REQ'] ?? 500000
    HANOI_SHIPPING_FEE.value = payload['HANOI'] ?? 25000
    OTHERS_SHIPPING_FEE.value = payload['OTHERS'] ?? 50000
  } catch (e) {
    console.error(`Checkout Error: Cannot fetch shipping fee. ${e.message}`)
  }
}

async function fetchCities() {
  try {
    cities.value = await AddressInfoService.getCities()
  } catch (e) {
    console.error('Checkout Error: Cannot fetch cities', e)
  }
}

async function fetchCommunes() {
  try {
    shippingInfo.value.communeId = ''
    communes.value = await AddressInfoService.getCommunes(shippingInfo.value.cityId)
  } catch (e) {
    console.error('Error fetching communes', e)
  }
}

async function confirmCheckout() {
  isPlacingOrder.value = true
  try {
    // map city name and commune name from selected ids
    const selectedCity = cities.value.find(o => o?.code === shippingInfo.value.cityId)
    if (selectedCity) shippingInfo.value.cityName = selectedCity.name

    const selectedCommune = communes.value.find(o => o?.code === shippingInfo.value.communeId)
    if (selectedCommune) shippingInfo.value.communeName = selectedCommune.name

    shippingInfo.value.orderTotal = tmpOrderTotal.value

    // make call to api
    if (shippingInfo.value.paymentMethod === 'COD') {
      try {
        await callToOrderApi()
        setTimeout(() => {
          cartStore.reset()
          router.push('/')
        }, 500)
      } catch (e) {
        console.error('Error COD checkout')
      }
    } else {
      const res = await PaymentService.createPaymentPage({
        amount: cartStore.totalAmount,
        info: shippingInfo.value.info,
      })
      const urlObj = new URL(res.paymentUrl)
      shippingInfo.value.vnpTxnRef = urlObj.searchParams.get('vnp_TxnRef')
      try {
        await callToOrderApi()
        setTimeout(() => {
          cartStore.reset()
          window.location.href = res?.paymentUrl
        }, 500)
      } catch (e) {
        console.error('Error VNPAY checkout')
      }
    }
  } catch (e) {
    console.error('Error checkout', e)
    notify(false, 'Lỗi khi đặt đơn')
  } finally {
    isPlacingOrder.value = false
  }
}

async function callToOrderApi() {
  try {
    const res = await OrderService.createOrder(shippingInfo.value)
    notify(true, 'Đặt đơn thành công')
    return res
  } catch (e) {
    console.error('Error calling to order API')
    notify(false, 'Lỗi gọi đến API đặt hàng')
    throw e
  }
}

const isPhoneValid = computed(() => /^\d+$/.test(shippingInfo.value.phone))
const isShippingValid = computed(() =>
  isPhoneValid.value
  && !!shippingInfo.value.communeId
  && !!shippingInfo.value.cityId
  && !!shippingInfo.value.street
  && !!shippingInfo.value.phone
  && !!selectedShipping.value
)
const tmpOrderTotal = computed(() =>
  shippingInfo.value.itemsTotal - shippingInfo.value.discountValue + shippingInfo.value.shippingFee
)

const shippingOptions = computed(() => {
  const opts = []
  if (shippingInfo.value.itemsTotal >= FREE_SHIP_REQ.value) {
    opts.push({
      label: `Miễn phí — đơn hàng trên ${formatPriceVNLocale(FREE_SHIP_REQ.value)} ₫`,
      value: 'FREE',
      fee: 0,
    })
  }
  if (String(shippingInfo.value.cityId).padStart(2, '0') === '01') {
    opts.push({
      label: 'Nội thành Hà Nội · 1-3 ngày',
      value: 'HANOI',
      fee: HANOI_SHIPPING_FEE.value,
    })
  } else if (shippingInfo.value.cityId) {
    opts.push({
      label: 'Tỉnh thành khác · 2-6 ngày',
      value: 'OTHERS',
      fee: OTHERS_SHIPPING_FEE.value,
    })
  }
  return opts
})

const selectedShipping = ref(null)

watch(selectedShipping, (val) => {
  const opt = shippingOptions.value.find(o => o.value === val)
  shippingInfo.value.shippingFee = opt?.fee ?? 0
  if (val === 'FREE') shippingInfo.value.isFreeShip = true
})

watch(selectedAddrId, async (id) => {
  const item = userProfileStore.userInfo.addressList.find(a => a.id === id)
  if (!item) return
  shippingInfo.value.cityId = item.cityId
  shippingInfo.value.cityName = item.city
  await fetchCommunes()
  shippingInfo.value.communeId = item.communeId
  shippingInfo.value.communeName = item.commune
  shippingInfo.value.street = item.street
})

watch(
  [() => shippingInfo.value.cityId, () => shippingInfo.value.communeId],
  () => {
    if (!!selectedAddrId) {
      selectedAddrId.value = null
      // bug: Hanoi shipping with non-Hanoi address
      // if not clear the selectedShipping value
      selectedShipping.value = null
    }
  },
)

function notify(success, msg) {
  if (success) {
    isSuccess.value = true
    message.value = msg
  } else {
    isError.value = true
    message.value = msg
  }
  setTimeout(() => {
    isSuccess.value = false
    isError.value = false
  }, 2500)
}

async function applyCoupon() {
  try {
    const res = await CouponService.applyCoupon({
      couponCode: shippingInfo.value.couponCode,
      itemsTotal: shippingInfo.value.itemsTotal,
    })
    const { discountValue } = res.data
    shippingInfo.value.discountValue = discountValue
    notify(true, `Áp dụng mã ${shippingInfo.value.couponCode} thành công`)
  } catch (e) {
    notify(false, `Lỗi khi áp mã ${shippingInfo.value.couponCode}`)
    console.log(`Error applying coupon ${e.message}`)
  }
}

onMounted(() => {
  getAllData()
})
</script>

<template>
  <div class="checkout-page">
    <!-- Breadcrumb -->
    <div class="checkout-breadcrumb">
      <router-link to="/" class="breadcrumb-link">Trang chủ</router-link>
      <v-icon icon="mdi-chevron-right" size="14" class="breadcrumb-sep" />
      <router-link :to="{ name: 'cart' }" class="breadcrumb-link">Giỏ hàng</router-link>
      <v-icon icon="mdi-chevron-right" size="14" class="breadcrumb-sep" />
      <span>Thanh toán</span>
    </div>

    <h1 class="checkout-heading">Thanh toán</h1>

    <div class="checkout-layout">
      <!-- LEFT: form -->
      <div class="checkout-form-col">
        <!-- Customer info -->
        <div class="checkout-section">
          <div class="checkout-section-header">
            <div class="section-icon-wrap"><v-icon icon="mdi-account-outline" size="18" /></div>
            <h2 class="checkout-section-title">Thông tin khách hàng</h2>
          </div>
          <div class="field-grid-2">
            <div class="field-group">
              <label class="field-label">Email</label>
              <v-text-field
                v-model="shippingInfo.email"
                variant="outlined"
                density="compact"
                hide-details
                disabled
                prepend-inner-icon="mdi-email-outline"
                class="checkout-field"
              />
            </div>
            <div class="field-group">
              <label class="field-label">Họ và tên</label>
              <v-text-field
                v-model="shippingInfo.name"
                variant="outlined"
                density="compact"
                hide-details
                disabled
                prepend-inner-icon="mdi-account-outline"
                class="checkout-field"
              />
            </div>
          </div>
          <div class="field-group">
            <label class="field-label">Số điện thoại <span class="required">*</span></label>
            <v-text-field
              v-model="shippingInfo.phone"
              variant="outlined"
              density="compact"
              :rules="[rules.required, rules.phone]"
              placeholder="0900 000 000"
              prepend-inner-icon="mdi-phone-outline"
              hide-details="auto"
              class="checkout-field"
            />
          </div>
        </div>

        <!-- Delivery address -->
        <div class="checkout-section">
          <div class="checkout-section-header">
            <div class="section-icon-wrap"><v-icon icon="mdi-map-marker-outline" size="18" /></div>
            <h2 class="checkout-section-title">Địa chỉ giao hàng</h2>
          </div>

          <!-- Address book -->
          <div class="field-group" v-if="userProfileStore.userInfo.addressList?.length">
            <label class="field-label">Chọn từ sổ địa chỉ</label>
            <v-select
              v-model="selectedAddrId"
              :items="userProfileStore.userInfo.addressList"
              item-value="id"
              no-data-text="Không có địa chỉ"
              variant="outlined"
              density="compact"
              hide-details
              placeholder="Chọn địa chỉ đã lưu"
              prepend-inner-icon="mdi-book-outline"
              class="checkout-field"
            >
              <template v-slot:item="{ props, item }">
                <v-list-item
                  v-bind="props"
                  :title="`${item.raw.street}, ${item.raw.commune}, ${item.raw.city}`"
                />
              </template>
              <template v-slot:selection="{ item }">
                {{ item.raw.street }}, {{ item.raw.commune }}, {{ item.raw.city }}
              </template>
            </v-select>
          </div>
          <div class="field-grid-2">
            <div class="field-group">
              <label class="field-label">Tỉnh / Thành phố <span class="required"
                >*</span></label>
              <v-autocomplete
                v-model="shippingInfo.cityId"
                :items="cities"
                item-value="code"
                item-title="name"
                variant="outlined"
                density="compact"
                hide-details
                placeholder="Chọn tỉnh thành"
                prepend-inner-icon="mdi-city-variant-outline"
                class="checkout-field"
                @update:modelValue="fetchCommunes"
              >
                <template v-slot:selection="{ item }">
                  <template v-if="!/\d/.test(item.title)">{{ item.title }}</template>
                  <template v-else>{{ shippingInfo.cityName }}</template>
                </template>
              </v-autocomplete>
            </div>
            <div class="field-group">
              <label class="field-label">Xã / Phường <span class="required">*</span></label>
              <v-autocomplete
                v-model="shippingInfo.communeId"
                :items="communes"
                item-value="code"
                item-title="name"
                variant="outlined"
                density="compact"
                hide-details
                placeholder="Chọn xã phường"
                prepend-inner-icon="mdi-home-map-marker"
                :disabled="!shippingInfo.cityId"
                class="checkout-field"
              >
                <template v-slot:selection="{ item }">
                  <template v-if="!/\d/.test(item.title)">{{ item.title }}</template>
                  <template v-else>{{ shippingInfo.communeName }}</template>
                </template>
              </v-autocomplete>
            </div>
          </div>
          <div class="field-group">
            <label class="field-label">Số nhà, đường, thôn, ngõ <span class="required"
              >*</span></label>
            <v-text-field
              v-model="shippingInfo.street"
              :rules="[rules.required]"
              variant="outlined"
              density="compact"
              hide-details="auto"
              placeholder="VD: 12 Phố Huế, Ngõ 5"
              prepend-inner-icon="mdi-road-variant"
              class="checkout-field"
            />
          </div>
          <div class="field-group">
            <label class="field-label">Ghi chú <span class="field-optional"
              >(tùy chọn)</span></label>
            <v-textarea
              v-model="shippingInfo.info"
              variant="outlined"
              density="compact"
              hide-details
              placeholder="Hướng dẫn giao hàng, thời gian nhận hàng..."
              rows="2"
              class="checkout-field"
            />
          </div>
        </div>

        <!-- Shipping method -->
        <div class="checkout-section">
          <div class="checkout-section-header">
            <div class="section-icon-wrap">
              <v-icon icon="mdi-truck-delivery-outline" size="18" />
            </div>
            <h2 class="checkout-section-title">
              Phương thức vận chuyển <span class="required">*</span>
            </h2>
          </div>
          <div v-if="!shippingInfo.cityId" class="method-hint">
            <v-icon icon="mdi-information-outline" size="15" class="mr-1" />
            Vui lòng chọn tỉnh thành để xem tùy chọn vận chuyển
          </div>
          <div v-else class="method-list">
            <label
              v-for="opt in shippingOptions"
              :key="opt.value"
              class="method-option"
              :class="{ 'method-option--active': selectedShipping === opt.value }"
            >
              <input
                type="radio"
                :value="opt.value"
                v-model="selectedShipping"
                class="method-radio"
              />
              <div class="method-option-body">
                <span class="method-label">{{ opt.label }}</span>
                <span class="method-fee" :class="opt.fee === 0 ? 'method-fee--free' : ''">
                  {{ opt.fee === 0 ? 'Miễn phí' : formatPriceVNLocale(opt.fee) + ' ₫' }}
                </span>
              </div>
            </label>
          </div>
        </div>

        <!-- Payment method -->
        <div class="checkout-section">
          <div class="checkout-section-header">
            <div class="section-icon-wrap"><v-icon icon="mdi-credit-card-outline" size="18" /></div>
            <h2 class="checkout-section-title">Phương thức thanh toán</h2>
          </div>
          <div class="method-list">
            <label
              class="method-option"
              :class="{ 'method-option--active': shippingInfo.paymentMethod === 'COD' }"
            >
              <input
                type="radio"
                value="COD"
                v-model="shippingInfo.paymentMethod"
                class="method-radio"
              />
              <div class="method-option-body">
                <div class="method-label">
                  <v-icon icon="mdi-cash" size="16" class="mr-2 method-icon" />Thanh toán khi
                  nhận hàng (COD)
                </div>
                <span class="method-desc">Trả tiền mặt khi nhận hàng</span>
              </div>
            </label>
            <label
              class="method-option"
              :class="{ 'method-option--active': shippingInfo.paymentMethod === 'VNPAY' }"
            >
              <input
                type="radio"
                value="VNPAY"
                v-model="shippingInfo.paymentMethod"
                class="method-radio"
              />
              <div class="method-option-body">
                <div class="method-label">
                  <v-icon icon="mdi-qrcode-scan" size="16" class="mr-2 method-icon" />VNPay · QR /
                  Internet Banking
                </div>
                <span class="method-desc">Thanh toán online an toàn qua cổng VNPay</span>
              </div>
            </label>
          </div>
        </div>
      </div>

      <!-- RIGHT: Order summary -->
      <aside class="checkout-summary" aria-label="Tóm tắt đơn hàng">
        <h2 class="summary-title">Đơn hàng ({{ cartStore.cartItemsCount }} sản phẩm)</h2>
        <div class="summary-items">
          <horizontal-book-card v-for="item in cartStore.activeCart" :key="item.id" :book="item" />
        </div>

        <!-- Coupon -->
        <div class="coupon-row">
          <v-text-field
            v-model="shippingInfo.couponCode"
            variant="outlined"
            density="compact"
            hide-details
            placeholder="Mã giảm giá"
            prepend-inner-icon="mdi-ticket-percent-outline"
            :readonly="true"
            class="checkout-field coupon-input"
          />
          <button class="coupon-btn" type="button" @click="openCouponDrawer">Chọn mã</button>
        </div>

        <!-- Price breakdown -->
        <div class="summary-lines">
          <div class="summary-line">
            <span>Tạm tính</span><span>{{ formatPriceVNLocale(shippingInfo.itemsTotal) }}
              ₫</span>
          </div>
          <div class="summary-line">
            <span>Giảm giá</span><span>-{{ formatPriceVNLocale(shippingInfo.discountValue) }}
              ₫</span>
          </div>
          <div class="summary-line">
            <span>Phí vận chuyển</span>
            <span :class="shippingInfo.shippingFee === 0 ? 'summary-free' : ''">
              {{
                shippingInfo.shippingFee > 0 ? formatPriceVNLocale(shippingInfo.shippingFee) + ' ₫' : 'Miễn phí'
              }}
            </span>
          </div>
        </div>

        <div class="summary-divider" />

        <div class="summary-total">
          <span>Tổng cộng</span>
          <span class="summary-total-price">{{ formatPriceVNLocale(tmpOrderTotal) }} ₫</span>
        </div>

        <button
          class="place-order-btn"
          type="button"
          :disabled="!isShippingValid || isPlacingOrder"
          @click="confirmCheckout"
        >
          <v-progress-circular
            v-if="isPlacingOrder"
            indeterminate
            size="18"
            width="2"
            color="white"
            class="mr-2"
          />
          <v-icon v-else icon="mdi-lock-outline" size="16" class="mr-2" />
          {{ isPlacingOrder ? 'Đang xử lý…' : 'Hoàn tất đơn hàng' }}
        </button>

        <p class="summary-trust">
          <v-icon icon="mdi-shield-check-outline" size="13" class="mr-1" />
          Thanh toán an toàn & bảo mật
        </p>

        <router-link :to="{ name: 'cart' }" class="back-to-cart">
          <v-icon icon="mdi-arrow-left" size="14" class="mr-1" />
          Quay lại giỏ hàng
        </router-link>
      </aside>
    </div>
  </div>

  <Teleport to="body">
    <Transition name="drawer-backdrop">
      <div
        v-if="isCouponDrawerOpen"
        class="coupon-backdrop"
        @click="closeCouponDrawer"
      />
    </Transition>

    <Transition name="coupon-drawer">
      <aside
        v-if="isCouponDrawerOpen"
        class="coupon-drawer"
        role="dialog"
        aria-modal="true"
        aria-label="Chọn mã giảm giá"
      >
        <!-- Header -->
        <div class="cd-header">
          <div class="cd-header-left">
            <v-icon icon="mdi-ticket-percent-outline" size="20" class="cd-header-icon" />
            <div>
              <h3 class="cd-title">Mã giảm giá</h3>
              <p class="cd-subtitle">
                Áp dụng cho đơn {{ formatPriceVNLocale(shippingInfo.itemsTotal) }} ₫
              </p>
            </div>
          </div>
          <button class="cd-close" @click="closeCouponDrawer" aria-label="Đóng">
            <v-icon icon="mdi-close" size="20" />
          </button>
        </div>

        <!-- Manual input strip inside drawer -->
        <div class="cd-manual">
          <v-text-field
            v-model="shippingInfo.couponCode"
            variant="outlined"
            density="compact"
            hide-details
            placeholder="Nhập mã giảm giá"
            prepend-inner-icon="mdi-tag-outline"
            class="checkout-field cd-manual-field"
          />
          <button
            class="cd-apply-btn"
            type="button"
            :disabled="!shippingInfo.couponCode"
            @click="() => {
              closeCouponDrawer()
              applyCoupon()
            }"
          >
            Dùng ngay
          </button>
        </div>

        <div class="cd-divider-label">
          <span>hoặc chọn từ danh sách bên dưới</span>
        </div>

        <!-- Loading -->
        <div v-if="isFetchingCoupons" class="cd-loading">
          <v-progress-circular indeterminate size="28" width="2" color="#a3262c" />
          <span>Đang tải mã khuyến mãi…</span>
        </div>

        <!-- Empty -->
        <div v-else-if="!availableCoupons.length" class="cd-empty">
          <v-icon icon="mdi-emoticon-sad-outline" size="40" class="cd-empty-icon" />
          <p>Không có mã giảm giá phù hợp với đơn hàng này</p>
        </div>

        <!-- Coupon list -->
        <div v-else class="cd-list">
          <div
            v-for="c in availableCoupons"
            :key="c.code"
            class="coupon-card"
            :class="{ 'coupon-card--selected': shippingInfo.couponCode === c.code }"
            @click="selectCoupon(c.code)"
          >
            <!-- Left badge -->
            <div class="cc-badge">
              <span class="cc-value">{{ formatDiscount(c) }}</span>
              <span class="cc-off">GIẢM</span>
            </div>

            <!-- Punched divider -->
            <div class="cc-punch">
              <div class="cc-punch-circle cc-punch-top" />
              <div class="cc-punch-line" />
              <div class="cc-punch-circle cc-punch-bottom" />
            </div>

            <!-- Right info -->
            <div class="cc-body">
              <div class="cc-top-row">
                <code class="cc-code">{{ c.code }}</code>
                <span v-if="shippingInfo.couponCode === c.code" class="cc-selected-badge">
                  <v-icon icon="mdi-check-circle" size="14" />
                  Đã chọn
                </span>
              </div>

              <p class="cc-desc" v-if="c.description">{{ c.description }}</p>

              <div class="cc-meta-row">
                <span class="cc-date">
                  <v-icon icon="mdi-calendar-range-outline" size="12" class="mr-1" />
                  {{ fmtDate(c.validFrom) }} - {{ fmtDate(c.validUntil) }}
                </span>
              </div>

              <!-- Usage progress bar -->
              <div class="cc-usage">
                <div class="cc-usage-bar-track">
                  <div
                    class="cc-usage-bar-fill"
                    :style="{ width: usagePct(c) + '%' }"
                    :class="{
                      'cc-bar--low': usagePct(c) < 50,
                      'cc-bar--mid': usagePct(c) >= 50 && usagePct(c) < 80,
                      'cc-bar--high': usagePct(c) >= 80,
                    }"
                  />
                </div>
                <span class="cc-usage-label">
                  Đã dùng {{ c.usedCount }}/{{ c.maxUses ?? '∞' }}
                  <span class="cc-pct">({{ usagePct(c) }}%)</span>
                </span>
              </div>
            </div>
          </div>
        </div>
      </aside>
    </Transition>
  </Teleport>

  <snack-bar-on-failure :show="isError" :message="message" />
  <snack-bar-on-success :show="isSuccess" :message="message" />
</template>

<style scoped>
.checkout-page {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;
  --green:       #2a9d5c;

  max-width: 1100px;
  margin: 0 auto;
  padding: 28px 24px 64px;
  color: var(--ink);
  font-family: system-ui, -apple-system, sans-serif;
  font-size: 0.9rem;
}

.checkout-breadcrumb { display: flex; align-items: center; gap: 4px; font-size: 0.8rem; color: var(--muted); margin-bottom: 16px; }
.breadcrumb-link { color: var(--muted); text-decoration: none; transition: color 0.15s; }
.breadcrumb-link:hover { color: var(--accent); }
.breadcrumb-sep { color: var(--border); }
.checkout-heading { font-size: clamp(1.3rem, 2vw, 1.7rem); font-weight: 700; margin: 0 0 28px; }

.checkout-layout { display: grid; grid-template-columns: 1fr 360px; gap: 28px; align-items: start; }

.checkout-section { background: #fff; border: 1px solid var(--border); border-radius: 12px; padding: 20px 24px 24px; margin-bottom: 16px; }
.checkout-section:last-child { margin-bottom: 0; }
.checkout-section-header { display: flex; align-items: center; gap: 10px; margin-bottom: 18px; }
.section-icon-wrap { width: 34px; height: 34px; border-radius: 8px; background: var(--accent-soft); color: var(--accent); display: flex; align-items: center; justify-content: center; flex-shrink: 0; }
.checkout-section-title { font-size: 0.95rem; font-weight: 600; margin: 0; }

.field-grid-2 { display: grid; grid-template-columns: 1fr 1fr; gap: 12px 16px; }
.field-group { margin-bottom: 14px; }
.field-group:last-child { margin-bottom: 0; }
.field-label { display: block; font-size: 0.8rem; font-weight: 600; color: var(--ink); margin-bottom: 6px; letter-spacing: 0.01em; }
.field-optional { font-weight: 400; color: var(--muted); font-size: 0.75rem; }
.required { color: var(--accent); margin-left: 2px; }

.checkout-field :deep(.v-field) { border-radius: 8px !important; font-size: 0.875rem; }
.checkout-field :deep(.v-field--focused .v-field__outline) { color: var(--accent) !important; }
.checkout-field :deep(.v-field__prepend-inner .v-icon) { color: var(--muted) !important; opacity: 1; }
.checkout-field :deep(.v-field--disabled) { opacity: 0.6; }

.method-hint { display: flex; align-items: center; font-size: 0.82rem; color: var(--muted); background: var(--paper); border: 1px solid var(--border); border-radius: 8px; padding: 10px 14px; }
.method-list { display: flex; flex-direction: column; gap: 10px; }
.method-option { display: flex; align-items: flex-start; gap: 12px; padding: 14px 16px; border: 1px solid var(--border); border-radius: 10px; cursor: pointer; transition: border-color 0.15s, background 0.15s; }
.method-option:hover { border-color: #c8bfb5; }
.method-option--active { border-color: var(--accent); background: var(--accent-soft); }
.method-radio { margin-top: 2px; accent-color: var(--accent); flex-shrink: 0; }
.method-option-body { flex: 1; display: flex; flex-direction: column; gap: 3px; }
.method-label { font-size: 0.875rem; font-weight: 600; color: var(--ink); display: flex; align-items: center; }
.method-icon { color: var(--muted); }
.method-desc { font-size: 0.78rem; color: var(--muted); }
.method-fee { font-size: 0.82rem; font-weight: 600; color: var(--ink); }
.method-fee--free { color: var(--green); }

.checkout-summary { background: #fff; border: 1px solid var(--border); border-radius: 12px; padding: 24px; position: sticky; top: 80px; }
.summary-title { font-size: 1rem; font-weight: 600; margin: 0 0 16px; padding-bottom: 14px; border-bottom: 1px solid var(--border); }
.summary-items { margin-bottom: 16px; }

.coupon-row { display: flex; gap: 8px; margin-bottom: 16px; }
.coupon-input { flex: 1; }
.coupon-btn { height: 40px; padding: 0 16px; background: var(--paper); border: 1px solid var(--border); border-radius: 8px; font-size: 0.82rem; font-weight: 600; color: var(--ink); cursor: pointer; white-space: nowrap; transition: border-color 0.15s, background 0.15s; flex-shrink: 0; }
.coupon-btn:hover { border-color: var(--accent); color: var(--accent); background: var(--accent-soft); }

.summary-lines { display: flex; flex-direction: column; gap: 10px; margin-bottom: 14px; }
.summary-line { display: flex; justify-content: space-between; font-size: 0.875rem; color: var(--muted); }
.summary-free { color: var(--green); font-weight: 600; }
.summary-divider { height: 1px; background: var(--border); margin-bottom: 14px; }
.summary-total { display: flex; justify-content: space-between; align-items: baseline; margin-bottom: 20px; font-weight: 700; }
.summary-total-price { font-size: 1.3rem; color: var(--accent); }

.place-order-btn { width: 100%; height: 48px; background: var(--accent); color: #fff; border: none; border-radius: 8px; font-size: 0.9rem; font-weight: 700; letter-spacing: 0.02em; cursor: pointer; display: flex; align-items: center; justify-content: center; transition: background 0.2s, opacity 0.2s; margin-bottom: 10px; }
.place-order-btn:hover:not(:disabled) { background: #8e1f24; }
.place-order-btn:disabled { opacity: 0.45; cursor: not-allowed; }
.place-order-btn:focus-visible { outline: 2px solid var(--accent); outline-offset: 3px; }

.summary-trust { display: flex; align-items: center; justify-content: center; font-size: 0.77rem; color: var(--muted); margin: 0 0 14px; }
.back-to-cart { display: flex; align-items: center; justify-content: center; font-size: 0.82rem; color: var(--muted); text-decoration: none; transition: color 0.15s; }
.back-to-cart:hover { color: var(--accent); }

@media (max-width: 900px) {
  .checkout-layout { grid-template-columns: 1fr; }
  .checkout-summary { position: static; order: -1; }
}
@media (max-width: 600px) {
  .checkout-page { padding: 20px 16px 48px; }
  .field-grid-2 { grid-template-columns: 1fr; }
  .checkout-section { padding: 16px; }
}

.coupon-backdrop {
  position: fixed;
  inset: 0;
  background: rgba(43, 36, 32, 0.35);
  z-index: 1000;
  backdrop-filter: blur(2px);
}

.coupon-drawer {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  width: 420px;
  max-width: 100vw;
  background: #fff;
  z-index: 1001;
  display: flex;
  flex-direction: column;
  box-shadow: -8px 0 32px rgba(43, 36, 32, 0.12);
  font-family: system-ui, -apple-system, sans-serif;
  font-size: 0.9rem;
  color: #2b2420;
}

/* Drawer transitions */
.drawer-backdrop-enter-active,
.drawer-backdrop-leave-active { transition: opacity 0.25s ease; }
.drawer-backdrop-enter-from,
.drawer-backdrop-leave-to { opacity: 0; }

.coupon-drawer-enter-active,
.coupon-drawer-leave-active { transition: transform 0.28s cubic-bezier(0.4, 0, 0.2, 1); }
.coupon-drawer-enter-from,
.coupon-drawer-leave-to { transform: translateX(100%); }

/* Drawer header */
.cd-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 18px 20px 16px;
  border-bottom: 1px solid #e8e1d8;
  flex-shrink: 0;
}
.cd-header-left { display: flex; align-items: center; gap: 12px; }
.cd-header-icon { color: #a3262c; }
.cd-title { font-size: 1rem; font-weight: 700; margin: 0 0 2px; }
.cd-subtitle { font-size: 0.77rem; color: #8a7d72; margin: 0; }
.cd-close { background: none; border: none; cursor: pointer; color: #8a7d72; padding: 4px; border-radius: 6px; line-height: 1; transition: color 0.15s, background 0.15s; }
.cd-close:hover { color: #a3262c; background: #f7e9e8; }

/* Manual input inside drawer */
.cd-manual {
  display: flex;
  gap: 8px;
  padding: 14px 20px 0;
  flex-shrink: 0;
}
.cd-manual-field { flex: 1; }
.cd-apply-btn {
  height: 40px;
  padding: 0 16px;
  background: #a3262c;
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 0.82rem;
  font-weight: 700;
  cursor: pointer;
  white-space: nowrap;
  flex-shrink: 0;
  transition: background 0.15s, opacity 0.15s;
}
.cd-apply-btn:hover:not(:disabled) { background: #8e1f24; }
.cd-apply-btn:disabled { opacity: 0.4; cursor: not-allowed; }

/* Divider label */
.cd-divider-label {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 14px 20px 4px;
  flex-shrink: 0;
}
.cd-divider-label::before,
.cd-divider-label::after { content: ''; flex: 1; height: 1px; background: #e8e1d8; }
.cd-divider-label span { font-size: 0.73rem; color: #8a7d72; white-space: nowrap; }

/* Loading / empty */
.cd-loading {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  flex: 1;
  color: #8a7d72;
  font-size: 0.85rem;
}
.cd-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 10px;
  flex: 1;
  padding: 24px;
  text-align: center;
  color: #8a7d72;
  font-size: 0.85rem;
}
.cd-empty-icon { color: #c8bfb5; }

/* Scrollable coupon list */
.cd-list {
  flex: 1;
  overflow-y: auto;
  padding: 12px 20px 24px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.cd-list::-webkit-scrollbar { width: 4px; }
.cd-list::-webkit-scrollbar-thumb { background: #e8e1d8; border-radius: 4px; }

.coupon-card {
  display: flex;
  border: 1.5px solid #e8e1d8;
  border-radius: 12px;
  overflow: hidden;
  cursor: pointer;
  transition: border-color 0.15s, box-shadow 0.15s, transform 0.1s;
  background: #fff;
  user-select: none;
}
.coupon-card:hover {
  border-color: #c8bfb5;
  box-shadow: 0 2px 10px rgba(163, 38, 44, 0.08);
  transform: translateY(-1px);
}
.coupon-card--selected {
  border-color: #a3262c;
  background: #fdf5f5;
  box-shadow: 0 2px 12px rgba(163, 38, 44, 0.14);
}

/* Left badge */
.cc-badge {
  width: 72px;
  flex-shrink: 0;
  background: #a3262c;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 2px;
  padding: 12px 6px;
}
.coupon-card--selected .cc-badge { background: #8e1f24; }
.cc-value {
  font-size: 1.1rem;
  font-weight: 800;
  color: #fff;
  line-height: 1;
  text-align: center;
  word-break: break-all;
}
.cc-off {
  font-size: 0.6rem;
  font-weight: 700;
  color: rgba(255,255,255,0.75);
  letter-spacing: 0.08em;
}

/* Punched divider */
.cc-punch {
  width: 14px;
  flex-shrink: 0;
  background: #fff;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  position: relative;
}
.coupon-card--selected .cc-punch { background: #fdf5f5; }
.cc-punch-circle {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: #f0ebe4;
  flex-shrink: 0;
}
.coupon-card--selected .cc-punch-circle { background: #f0ebe4; }
.cc-punch-line {
  flex: 1;
  width: 1px;
  background: repeating-linear-gradient(to bottom, #e8e1d8 0, #e8e1d8 4px, transparent 4px, transparent 8px);
}

/* Right body */
.cc-body {
  flex: 1;
  padding: 12px 14px;
  display: flex;
  flex-direction: column;
  gap: 5px;
  min-width: 0;
}
.cc-top-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 8px;
}
.cc-code {
  font-size: 0.88rem;
  font-weight: 700;
  color: #2b2420;
  letter-spacing: 0.05em;
  background: #f3ede6;
  padding: 2px 8px;
  border-radius: 4px;
  font-family: 'Courier New', monospace;
}
.coupon-card--selected .cc-code { background: #f7e9e8; color: #a3262c; }
.cc-selected-badge {
  display: flex;
  align-items: center;
  gap: 3px;
  font-size: 0.72rem;
  font-weight: 600;
  color: #a3262c;
  white-space: nowrap;
}
.cc-desc { font-size: 0.78rem; color: #5a4f48; margin: 0; line-height: 1.35; }
.cc-meta-row { display: flex; align-items: center; gap: 10px; }
.cc-date { font-size: 0.73rem; color: #8a7d72; display: flex; align-items: center; }

/* Usage bar */
.cc-usage { display: flex; flex-direction: column; gap: 4px; margin-top: 2px; }
.cc-usage-bar-track { height: 5px; background: #ede8e2; border-radius: 99px; overflow: hidden; }
.cc-usage-bar-fill { height: 100%; border-radius: 99px; transition: width 0.4s ease; }
.cc-bar--low  { background: #2a9d5c; }
.cc-bar--mid  { background: #e8a838; }
.cc-bar--high { background: #a3262c; }
.cc-usage-label { font-size: 0.7rem; color: #8a7d72; }
.cc-pct { font-weight: 600; }

@media (max-width: 480px) {
  .coupon-drawer { width: 100vw; }
}
</style>
