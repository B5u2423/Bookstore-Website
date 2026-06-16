<script setup>
import { OrderService } from '@/api/cart-api'
import { formatPriceVNLocale } from '@/utils/utils'
import { computed, onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const responseCode = ref(route.query.vnp_ResponseCode)
const vnpTxnRef = ref(route.query.vnp_TxnRef)
const isSuccess = computed(() => responseCode.value === '00')

const responseCodeMessage = computed(() => {
  const responseCodes = {
    '07': 'Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).',
    '09': 'Thẻ/Tài khoản chưa đăng ký dịch vụ InternetBanking tại ngân hàng.',
    '10': 'Xác thực thông tin thẻ/tài khoản không đúng quá 3 lần.',
    '11': 'Đã hết hạn chờ thanh toán. Vui lòng thực hiện lại giao dịch.',
    '12': 'Thẻ/Tài khoản bị khóa.',
    '13': 'Nhập sai mật khẩu xác thực giao dịch (OTP). Vui lòng thực hiện lại giao dịch.',
    '24': 'Giao dịch đã bị hủy.',
    '51': 'Tài khoản không đủ số dư để thực hiện giao dịch.',
    '65': 'Tài khoản đã vượt quá hạn mức giao dịch trong ngày.',
    '75': 'Ngân hàng thanh toán đang bảo trì.',
    '79': 'Nhập sai mật khẩu thanh toán quá số lần quy định. Vui lòng thực hiện lại giao dịch.',
    '99': 'Lỗi không xác định.',
  }
  return responseCodes[String(responseCode.value)] || 'Lỗi không xác định.'
})

async function updateStatus() {
  try {
    await OrderService.updateStatus({
      vnpTxnRef: vnpTxnRef.value,
      isCancelled: !isSuccess.value,
    })
  } catch (error) {
    console.error('Error updating order status', error)
  }
}

onMounted(updateStatus)
</script>

<template>
  <div class="result-page">
    <div class="result-card" :class="isSuccess ? 'result-card--success' : 'result-card--failure'">

      <!-- Top accent bar -->
      <div class="result-bar" :class="isSuccess ? 'result-bar--success' : 'result-bar--failure'" />

      <div class="result-body">

        <!-- Icon -->
        <div class="result-icon-wrap" :class="isSuccess ? 'result-icon-wrap--success' : 'result-icon-wrap--failure'">
          <v-icon
            :icon="isSuccess ? 'mdi-check-circle' : 'mdi-close-circle'"
            size="48"
          />
        </div>

        <!-- Heading -->
        <h1 class="result-heading">
          {{ isSuccess ? 'Thanh toán thành công!' : 'Thanh toán thất bại' }}
        </h1>
        <p class="result-subheading">
          {{
            isSuccess
              ? 'Đơn hàng của bạn đã được xác nhận và đang được xử lý.'
              : 'Đơn hàng của bạn không thể hoàn tất. Vui lòng thử lại.'
          }}
        </p>

        <!-- Success details -->
        <template v-if="isSuccess">
          <div class="result-details">
            <div class="detail-row">
              <span class="detail-label">
                <v-icon icon="mdi-identifier" size="15" class="mr-1" />
                Mã giao dịch
              </span>
              <span class="detail-value detail-value--mono">
                {{ route.query.vnp_BankTranNo || '—' }}
              </span>
            </div>

            <div class="detail-divider" />

            <div class="detail-row">
              <span class="detail-label">
                <v-icon icon="mdi-bank-outline" size="15" class="mr-1" />
                Ngân hàng
              </span>
              <span class="detail-value">
                {{ route.query.vnp_BankCode || '—' }}
              </span>
            </div>

            <div class="detail-divider" />

            <div class="detail-row detail-row--total">
              <span class="detail-label">
                <v-icon icon="mdi-cash-multiple" size="15" class="mr-1" />
                Tổng tiền
              </span>
              <span class="detail-value detail-value--price">
                {{ formatPriceVNLocale(route.query.vnp_Amount / 100) }} ₫
              </span>
            </div>
          </div>

          <div class="result-trust">
            <v-icon icon="mdi-shield-check-outline" size="14" class="mr-1" />
            Giao dịch được bảo mật bởi VNPay
          </div>
        </template>

        <!-- Failure reason -->
        <template v-else>
          <div class="failure-reason">
            <v-icon icon="mdi-information-outline" size="16" class="mr-2 flex-shrink" />
            <span>
              <strong>Lý do:</strong>
              {{ responseCodeMessage }}
            </span>
          </div>

          <p class="failure-help">
            Nếu tiền đã bị trừ, vui lòng liên hệ hỗ trợ với mã giao dịch
            <strong class="failure-txn">{{ vnpTxnRef || '—' }}</strong>.
          </p>
        </template>

        <!-- Actions -->
        <div class="result-actions">
          <router-link :to="{ name: 'landing' }" class="btn-primary">
            <v-icon icon="mdi-storefront-outline" size="16" class="mr-2" />
            Tiếp tục mua sắm
          </router-link>

          <router-link
            v-if="isSuccess"
            :to="{ name: 'history' }"
            class="btn-secondary"
          >
            <v-icon icon="mdi-receipt-text-outline" size="16" class="mr-2" />
            Xem đơn hàng
          </router-link>

          <router-link
            v-else
            :to="{ name: 'cart' }"
            class="btn-secondary"
          >
            <v-icon icon="mdi-cart-outline" size="16" class="mr-2" />
            Quay lại giỏ hàng
          </router-link>
        </div>

      </div>
    </div>
  </div>
</template>

<style scoped>

.result-page {
  --accent:        #a3262c;
  --accent-soft:   #f7e9e8;
  --ink:           #2b2420;
  --muted:         #8a7d72;
  --border:        #e8e1d8;
  --paper:         #fbf8f4;
  --green:         #2a9d5c;
  --green-soft:    #f0fdf6;
  --green-border:  #bbf0d4;

  display: flex;
  justify-content: center;
  padding: 32px 16px;
  background-color: var(--paper);
  background-image:
    radial-gradient(ellipse at 30% 70%, rgba(163,38,44,0.05) 0%, transparent 55%),
    radial-gradient(ellipse at 75% 20%, rgba(43,36,32,0.04) 0%, transparent 50%);
  color: var(--ink);
  font-family: system-ui, -apple-system, sans-serif;
}

.result-card {
  width: 100%;
  max-width: 480px;
  background: #fff;
  border-radius: 16px;
  overflow: hidden;
  box-shadow:
    0 4px 6px -1px rgba(43,36,32,0.07),
    0 12px 32px -8px rgba(43,36,32,0.12);
}

.result-bar {
  height: 5px;
  width: 100%;
}
.result-bar--success { background: var(--green); }
.result-bar--failure { background: var(--accent); }

.result-body {
  padding: 36px 36px 32px;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.result-icon-wrap {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}
.result-icon-wrap--success {
  background: var(--green-soft);
  color: var(--green);
  border: 2px solid var(--green-border);
}
.result-icon-wrap--failure {
  background: var(--accent-soft);
  color: var(--accent);
  border: 2px solid #fcd4d4;
}

.result-heading {
  font-size: 1.55rem;
  font-weight: 700;
  margin: 0 0 8px;
  color: var(--ink);
}
.result-subheading {
  font-size: 0.9rem;
  color: var(--muted);
  margin: 0 0 28px;
  line-height: 1.5;
}

.result-details {
  width: 100%;
  background: var(--paper);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 4px 0;
  margin-bottom: 16px;
  text-align: left;
}
.detail-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  gap: 12px;
}
.detail-row--total {
  background: transparent;
}
.detail-divider {
  height: 1px;
  background: var(--border);
  margin: 0 16px;
}
.detail-label {
  display: flex;
  align-items: center;
  font-size: 0.8rem;
  color: var(--muted);
  white-space: nowrap;
}
.detail-value {
  font-size: 0.875rem;
  font-weight: 600;
  color: var(--ink);
  text-align: right;
  word-break: break-all;
}
.detail-value--mono {
  font-family: 'Courier New', monospace;
  font-size: 0.8rem;
  letter-spacing: 0.03em;
}
.detail-value--price {
  font-size: 1.1rem;
  color: var(--green);
}

.result-trust {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.775rem;
  color: var(--muted);
  margin-bottom: 24px;
}

.failure-reason {
  display: flex;
  align-items: flex-start;
  gap: 8px;
  width: 100%;
  background: var(--accent-soft);
  border: 1px solid #fcd4d4;
  border-radius: 8px;
  padding: 14px 16px;
  font-size: 0.875rem;
  color: var(--ink);
  text-align: left;
  margin-bottom: 14px;
  line-height: 1.5;
}
.flex-shrink { flex-shrink: 0; margin-top: 2px; color: var(--accent); }

.failure-help {
  font-size: 0.8rem;
  color: var(--muted);
  line-height: 1.5;
  margin: 0 0 24px;
}
.failure-txn {
  font-family: 'Courier New', monospace;
  color: var(--ink);
  letter-spacing: 0.03em;
}

.result-actions {
  display: flex;
  gap: 10px;
  width: 100%;
  flex-wrap: wrap;
}

.btn-primary,
.btn-secondary {
  flex: 1 1 0;
  min-width: 140px;
  height: 44px;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  transition: background 0.2s, border-color 0.2s, box-shadow 0.2s;
  cursor: pointer;
}
.btn-primary {
  background: var(--accent);
  color: #fff;
  border: none;
}
.btn-primary:hover { background: #8e1f24; }

.btn-secondary {
  background: #fff;
  color: var(--ink);
  border: 1px solid var(--border);
}
.btn-secondary:hover {
  border-color: var(--accent);
  color: var(--accent);
  box-shadow: 0 0 0 3px var(--accent-soft);
}

.btn-primary:focus-visible,
.btn-secondary:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
}

@media (max-width: 480px) {
  .result-body { padding: 28px 20px 24px; }
  .result-heading { font-size: 1.3rem; }
  .result-actions { flex-direction: column; }
  .btn-primary, .btn-secondary { flex: 1 1 auto; min-width: unset; }
}
</style>