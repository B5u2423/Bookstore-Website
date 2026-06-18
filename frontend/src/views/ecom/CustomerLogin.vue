<script setup>
import GoogleIcon from '@/components/common/GoogleIcon.vue'
import SnackBarOnFailure from '@/components/common/SnackBarOnFailure.vue'
import SnackBarOnSuccess from '@/components/common/SnackBarOnSuccess.vue'
import { useAuthStore } from '@/stores/auth-store'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const email = ref('')
const password = ref('')
const isValidForm = ref(false)
const visible = ref(false)
const isLoading = ref(false)

const rules = {
  required: (v) => !!v || 'Không được bỏ trống trường này',
  email: (v) => /[\w\d.-]{6,30}@[\w\d.-]+/.test(v) || 'Email không hợp lệ',
}

const isError = ref(false)
const isSuccess = ref(false)
const message = ref('')

async function handleLogin() {
  isLoading.value = true
  const result = await authStore.login({
    email: email.value,
    password: password.value,
  })

  if (result.success) {
    isSuccess.value = true
    message.value = 'Đăng nhập thành công!'
    setTimeout(() => {
      router.push({ name: 'profile-root' })
      isLoading.value = false
    }, 1500)
  } else {
    isLoading.value = false
    isError.value = true
    message.value = 'Email hoặc mật khẩu không đúng'
  }
}

async function googleAuth() {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google'
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-card">
      <!-- Brand mark -->
      <div class="auth-brand">
        <span class="auth-brand-icon" aria-hidden="true">📚</span>
        <span class="auth-brand-name">BookShelf</span>
      </div>

      <h1 class="auth-heading">Đăng nhập</h1>
      <p class="auth-subheading">Chào mừng bạn trở lại!</p>

      <v-form ref="form" v-model="isValidForm" @submit.prevent="handleLogin">
        <div class="field-group">
          <label class="field-label" for="login-email">Email</label>
          <v-text-field
            id="login-email"
            v-model="email"
            :rules="[rules.required, rules.email]"
            density="compact"
            variant="outlined"
            placeholder="you@example.com"
            prepend-inner-icon="mdi-email-outline"
            hide-details="auto"
            class="auth-field"
            autocomplete="email"
          />
        </div>

        <div class="field-group">
          <div class="field-label-row">
            <label class="field-label" for="login-password">Mật khẩu</label>
            <router-link to="/reset" class="forgot-link">Quên mật khẩu?</router-link>
          </div>
          <v-text-field
            id="login-password"
            v-model="password"
            :rules="[rules.required]"
            :type="visible ? 'text' : 'password'"
            :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
            density="compact"
            variant="outlined"
            placeholder="••••••••"
            prepend-inner-icon="mdi-lock-outline"
            hide-details="auto"
            class="auth-field"
            autocomplete="current-password"
            @click:append-inner="visible = !visible"
          />
        </div>

        <button
          class="submit-btn"
          type="submit"
          :disabled="!isValidForm || isLoading"
        >
          <v-progress-circular
            v-if="isLoading"
            indeterminate
            size="18"
            width="2"
            color="white"
            class="mr-2"
          />
          <span>{{ isLoading ? 'Đang đăng nhập…' : 'Đăng nhập' }}</span>
        </button>
      </v-form>

      <div class="divider-row">
        <span class="divider-line" aria-hidden="true" />
        <span class="divider-text">hoặc</span>
        <span class="divider-line" aria-hidden="true" />
      </div>

      <button class="google-btn" type="button" @click="googleAuth">
        <google-icon></google-icon>
        Tiếp tục với Google
      </button>

      <p class="auth-footer">
        Chưa có tài khoản?
        <router-link to="/register" class="auth-footer-link">Đăng ký ngay</router-link>
      </p>
    </div>

    <snack-bar-on-failure :show="isError" :message="message" />
    <snack-bar-on-success :show="isSuccess" :message="message" />
  </div>
</template>

<style scoped>
.auth-page {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;

  display: flex;
  justify-content: center;
  padding: 32px 16px;
  background-color: var(--paper);
  background-image:
    radial-gradient(ellipse at 20% 50%, rgba(163,38,44,0.06) 0%, transparent 60%),
    radial-gradient(ellipse at 80% 20%, rgba(43,36,32,0.04) 0%, transparent 50%);
}

/* card */
.auth-card {
  width: 100%;
  max-width: 420px;
  background: #fff;
  border: 1px solid var(--border);
  border-radius: 16px;
  padding: 40px 36px 32px;
  box-shadow:
    0 4px 6px -1px rgba(43,36,32,0.06),
    0 10px 30px -8px rgba(43,36,32,0.1);
}

/* brand */
.auth-brand {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-bottom: 24px;
}
.auth-brand-icon { font-size: 1.5rem; line-height: 1; }
.auth-brand-name {
  font-size: 1.2rem;
  font-weight: 700;
  color: var(--ink);
}

/* headings */
.auth-heading {
  font-size: 1.6rem;
  font-weight: 700;
  text-align: center;
  margin: 0 0 4px;
  color: var(--ink);
}
.auth-subheading {
  text-align: center;
  color: var(--muted);
  font-size: 0.9rem;
  margin: 0 0 28px;
}

/* fields */
.field-group {
  margin-bottom: 16px;
}
.field-label {
  display: block;
  font-size: 0.82rem;
  font-weight: 600;
  color: var(--ink);
  margin-bottom: 6px;
  letter-spacing: 0.01em;
}
.field-label-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 6px;
}
.forgot-link {
  font-size: 0.8rem;
  color: var(--accent);
  text-decoration: none;
  font-weight: 500;
  transition: opacity 0.15s;
}
.forgot-link:hover { opacity: 0.75; }

/* Vuetify field overrides */
.auth-field :deep(.v-field) {
  border-radius: 8px !important;
  font-size: 0.9rem;
}
.auth-field :deep(.v-field--focused .v-field__outline) {
  color: var(--accent) !important;
}
.auth-field :deep(.v-field__prepend-inner .v-icon) {
  color: var(--muted) !important;
  opacity: 1;
}

/* submit btn */
.submit-btn {
  width: 100%;
  height: 46px;
  background: var(--accent);
  color: #fff;
  border: none;
  border-radius: 8px;
  font-size: 0.9rem;
  font-weight: 700;
  letter-spacing: 0.03em;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-top: 8px;
  transition: background 0.2s, opacity 0.2s;
}
.submit-btn:hover:not(:disabled) { background: #8e1f24; }
.submit-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}
.submit-btn:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 3px;
}

/* divider */
.divider-row {
  display: flex;
  align-items: center;
  gap: 10px;
  margin: 20px 0;
}
.divider-line {
  flex: 1;
  height: 1px;
  background: var(--border);
}
.divider-text {
  font-size: 0.78rem;
  color: var(--muted);
  white-space: nowrap;
}

/* google btn */
.google-btn {
  width: 100%;
  height: 44px;
  background: #fff;
  border: 1px solid var(--border);
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--ink);
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  transition: border-color 0.2s, box-shadow 0.2s;
}
.google-btn:hover {
  border-color: #aaa;
  box-shadow: 0 1px 4px rgba(0,0,0,0.08);
}
.google-btn:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
}
.google-icon { width: 18px; height: 18px; flex-shrink: 0; }
/* footer */
.auth-footer {
  text-align: center;
  margin: 20px 0 0;
  font-size: 0.875rem;
  color: var(--muted);
}
.auth-footer-link {
  color: var(--accent);
  font-weight: 600;
  text-decoration: none;
  margin-left: 4px;
  transition: opacity 0.15s;
}
.auth-footer-link:hover { opacity: 0.75; }

/* responsive */
@media (max-width: 480px) {
  .auth-card {
    padding: 32px 20px 24px;
    border-radius: 12px;
  }
}
</style>
