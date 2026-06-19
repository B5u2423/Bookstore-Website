<script setup>
import { useAuthStore } from '@/stores/auth-store'
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()

const stage = ref('loading') // 'loading' | 'success' | 'error'

async function loadProfile() {
  const exchangeCode = route.query.ex
  if (exchangeCode) {
    try {
      await authStore.handleOauthCallback(exchangeCode)
      stage.value = 'success'
      setTimeout(() => router.push('/'), 1200)
    } catch {
      stage.value = 'error'
      setTimeout(() => router.push({ name: 'oauth-failed' }), 1500)
    }
  } else {
    stage.value = 'error'
    setTimeout(() => router.push({ name: 'oauth-failed' }), 1500)
  }
}

onMounted(loadProfile)
</script>

<template>
  <div class="callback-page">
    <div class="callback-card">
      <!-- Brand -->
      <div class="callback-brand">
        <span aria-hidden="true">📚</span>
        <span class="callback-brand-name">BookShelf</span>
      </div>

      <!-- Loading state -->
      <Transition name="fade" mode="out-in">
        <div v-if="stage === 'loading'" key="loading" class="callback-state">
          <div class="spinner-wrap" aria-label="Đang xử lý">
            <svg class="spinner" viewBox="0 0 50 50" aria-hidden="true">
              <circle class="spinner-track" cx="25" cy="25" r="20" fill="none" stroke-width="4" />
              <circle
                class="spinner-arc"
                cx="25"
                cy="25"
                r="20"
                fill="none"
                stroke-width="4"
                stroke-linecap="round"
              />
            </svg>
            <span class="spinner-icon" aria-hidden="true">
              <v-icon icon="mdi-google" size="20" />
            </span>
          </div>
          <h1 class="callback-heading">Đang xác thực…</h1>
          <p class="callback-body">
            Vui lòng đợi trong giây lát, chúng tôi đang xác nhận tài khoản Google
            của bạn.
          </p>
        </div>

        <!-- Success state -->
        <div v-else-if="stage === 'success'" key="success" class="callback-state">
          <div class="status-icon-wrap status-icon-wrap--success">
            <v-icon icon="mdi-check-circle" size="44" />
          </div>
          <h1 class="callback-heading">Đăng nhập thành công!</h1>
          <p class="callback-body">
            Chào mừng bạn trở lại. Đang chuyển hướng về trang chủ…
          </p>
          <div class="redirect-bar-wrap" aria-hidden="true">
            <div class="redirect-bar" />
          </div>
        </div>

        <!-- Error state -->
        <div v-else key="error" class="callback-state">
          <div class="status-icon-wrap status-icon-wrap--error">
            <v-icon icon="mdi-close-circle" size="44" />
          </div>
          <h1 class="callback-heading">Xác thực thất bại</h1>
          <p class="callback-body">
            Có lỗi xảy ra. Đang chuyển hướng về trang đăng nhập…
          </p>
        </div>
      </Transition>
    </div>
  </div>
</template>

<style scoped>
.callback-page {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;
  --green:       #2a9d5c;
  --green-soft:  #f0fdf6;
  --green-border:#bbf0d4;

  display: flex;
  justify-content: center;
  padding: 32px 16px;
  background-color: var(--paper);
  background-image:
    radial-gradient(ellipse at 30% 60%, rgba(163,38,44,0.06) 0%, transparent 55%),
    radial-gradient(ellipse at 75% 25%, rgba(43,36,32,0.04) 0%, transparent 50%);
}

.callback-card {
  width: 100%;
  max-width: 400px;
  background: #fff;
  border: 1px solid var(--border);
  border-radius: 16px;
  padding: 40px 36px 36px;
  box-shadow:
    0 4px 6px -1px rgba(43,36,32,0.06),
    0 10px 30px -8px rgba(43,36,32,0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.callback-brand {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 1.4rem;
  margin-bottom: 32px;
}
.callback-brand-name {
  font-size: 1.15rem;
  font-weight: 700;
  color: var(--ink);
}

.callback-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}

.callback-heading {
  font-size: 1.35rem;
  font-weight: 700;
  color: var(--ink);
  margin: 18px 0 8px;
}
.callback-body {
  font-size: 0.875rem;
  color: var(--muted);
  line-height: 1.6;
  margin: 0;
  max-width: 300px;
}

.spinner-wrap {
  position: relative;
  width: 72px;
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.spinner {
  position: absolute;
  inset: 0;
  width: 100%;
  height: 100%;
  animation: rotate 1.6s linear infinite;
}
.spinner-track { stroke: var(--border); }
.spinner-arc {
  stroke: var(--accent);
  stroke-dasharray: 80 200;
  stroke-dashoffset: 0;
  animation: dash 1.4s ease-in-out infinite;
}
.spinner-icon {
  position: relative;
  z-index: 1;
  color: var(--accent);
  display: flex;
}

@keyframes rotate {
  100% { transform: rotate(360deg); }
}
@keyframes dash {
  0%   { stroke-dasharray: 1 200; stroke-dashoffset: 0; }
  50%  { stroke-dasharray: 100 200; stroke-dashoffset: -30px; }
  100% { stroke-dasharray: 100 200; stroke-dashoffset: -124px; }
}

.status-icon-wrap {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  border: 2px solid;
  display: flex;
  align-items: center;
  justify-content: center;
}
.status-icon-wrap--success {
  background: var(--green-soft);
  border-color: var(--green-border);
  color: var(--green);
}
.status-icon-wrap--error {
  background: var(--accent-soft);
  border-color: #fcd4d4;
  color: var(--accent);
}

.redirect-bar-wrap {
  width: 100%;
  height: 3px;
  background: var(--border);
  border-radius: 99px;
  overflow: hidden;
  margin-top: 20px;
}
.redirect-bar {
  height: 100%;
  background: var(--green);
  border-radius: 99px;
  animation: fill-bar 1.2s ease-out forwards;
}
@keyframes fill-bar {
  from { width: 0%; }
  to   { width: 100%; }
}

.fade-enter-active,
.fade-leave-active { transition: opacity 0.22s ease, transform 0.22s ease; }
.fade-enter-from   { opacity: 0; transform: scale(0.97); }
.fade-leave-to     { opacity: 0; transform: scale(1.02); }

@media (max-width: 480px) {
  .callback-card { padding: 32px 20px 28px; border-radius: 12px; }
}
</style>
