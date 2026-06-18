<script setup>
import GoogleIcon from '@/components/common/GoogleIcon.vue'
import SnackBarOnFailure from '@/components/common/SnackBarOnFailure.vue'
import SnackBarOnSuccess from '@/components/common/SnackBarOnSuccess.vue'
import { useAuthStore } from '@/stores/auth-store'
import { computed, ref } from 'vue'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const visible = ref(false)
const visibleRetype = ref(false)
const isLoading = ref(false)
const form = ref(null)
const isValidForm = ref(false)

const formData = ref({
  name: '',
  email: '',
  password: '',
  retypePassword: '',
  userType: 'CUSTOMER',
})

const isError = ref(false)
const isSuccess = ref(false)
const message = ref('')

const rules = {
  required: (v) => !!v || 'Không được bỏ trống trường này',
  email: (v) => /[\w\d.-]{6,30}@[\w\d.-]+/.test(v) || 'Email không hợp lệ',
  minLength: (v) => (v && v.length >= 8) || 'Mật khẩu ít nhất 8 ký tự',
  passwordMatch: (v) => v === formData.value.password || 'Mật khẩu không khớp',
}

async function handleRegister() {
  const { valid } = await form.value.validate()
  if (!valid) return

  isLoading.value = true
  try {
    const res = await authStore.register(formData.value)
    if (res.success) {
      isSuccess.value = true
      message.value = 'Đăng ký thành công! Đang chuyển đến trang đăng nhập…'
      setTimeout(() => router.push({ name: 'login' }), 2000)
    } else {
      isError.value = true
      message.value = 'Email này đã được sử dụng. Vui lòng thử email khác.'
    }
  } finally {
    isLoading.value = false
  }
}

async function googleAuth() {
  window.location.href = 'http://localhost:8080/oauth2/authorization/google'
}
</script>

<template>
  <div class="auth-page">
    <div class="auth-card">
      <!-- Brand -->
      <div class="auth-brand">
        <span class="auth-brand-icon" aria-hidden="true">📚</span>
        <span class="auth-brand-name">BookShelf</span>
      </div>

      <h1 class="auth-heading">Tạo tài khoản</h1>
      <p class="auth-subheading">Tham gia cùng hàng nghìn độc giả hôm nay</p>

      <v-form ref="form" v-model="isValidForm" @submit.prevent="handleRegister">
        <div class="field-group">
          <label class="field-label" for="reg-name">Họ và tên</label>
          <v-text-field
            id="reg-name"
            v-model="formData.name"
            :rules="[rules.required]"
            density="compact"
            variant="outlined"
            placeholder="Nguyễn Văn A"
            prepend-inner-icon="mdi-account-outline"
            hide-details="auto"
            class="auth-field"
            autocomplete="name"
          />
        </div>

        <div class="field-group">
          <label class="field-label" for="reg-email">Email</label>
          <v-text-field
            id="reg-email"
            v-model="formData.email"
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

        <div class="fields-row">
          <div class="field-group">
            <label class="field-label" for="reg-password">Mật khẩu</label>
            <v-text-field
              id="reg-password"
              v-model="formData.password"
              :rules="[rules.required, rules.minLength]"
              :type="visible ? 'text' : 'password'"
              :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
              density="compact"
              variant="outlined"
              placeholder="••••••••"
              prepend-inner-icon="mdi-lock-outline"
              hide-details="auto"
              class="auth-field"
              autocomplete="new-password"
              @click:append-inner="visible = !visible"
            />
          </div>

          <div class="field-group">
            <label class="field-label" for="reg-retype">Nhập lại mật khẩu</label>
            <v-text-field
              id="reg-retype"
              v-model="formData.retypePassword"
              :rules="[rules.required, rules.passwordMatch]"
              :type="visibleRetype ? 'text' : 'password'"
              :append-inner-icon="visibleRetype ? 'mdi-eye-off' : 'mdi-eye'"
              density="compact"
              variant="outlined"
              placeholder="••••••••"
              prepend-inner-icon="mdi-lock-check-outline"
              hide-details="auto"
              class="auth-field"
              autocomplete="new-password"
              @click:append-inner="visibleRetype = !visibleRetype"
            />
          </div>
        </div>

        <!-- Password strength hint -->
        <p class="password-hint">
          <v-icon icon="mdi-information-outline" size="13" class="mr-1" />
          Mật khẩu ít nhất 8 ký tự
        </p>

        <button
          class="submit-btn"
          type="submit"
          :disabled="isLoading"
        >
          <v-progress-circular
            v-if="isLoading"
            indeterminate
            size="18"
            width="2"
            color="white"
            class="mr-2"
          />
          <span>{{ isLoading ? 'Đang tạo tài khoản…' : 'Tạo tài khoản' }}</span>
        </button>
      </v-form>

      <div class="divider-row">
        <span class="divider-line" aria-hidden="true" />
        <span class="divider-text">hoặc</span>
        <span class="divider-line" aria-hidden="true" />
      </div>

      <button class="google-btn" type="button" @click="googleAuth">
        <google-icon></google-icon>
        Đăng ký với Google
      </button>

      <p class="auth-footer">
        Đã có tài khoản?
        <router-link to="/login" class="auth-footer-link">Đăng nhập</router-link>
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
    radial-gradient(ellipse at 80% 50%, rgba(163,38,44,0.06) 0%, transparent 60%),
    radial-gradient(ellipse at 20% 20%, rgba(43,36,32,0.04) 0%, transparent 50%);
}

/* card */
.auth-card {
  width: 100%;
  max-width: 480px;
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
  font-size: 0.875rem;
  margin: 0 0 28px;
}

/* input fields */
.field-group {
  margin-bottom: 14px;
}
.field-label {
  display: block;
  font-size: 0.82rem;
  font-weight: 600;
  color: var(--ink);
  margin-bottom: 6px;
  letter-spacing: 0.01em;
}

/* two password fields side by side on wider screens */
.fields-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

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

/* password hint */
.password-hint {
  display: flex;
  align-items: center;
  font-size: 0.77rem;
  color: var(--muted);
  margin: 2px 0 16px;
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
  transition: background 0.2s, opacity 0.2s;
}
.submit-btn:hover:not(:disabled) { background: #8e1f24; }
.submit-btn:disabled { opacity: 0.5; cursor: not-allowed; }
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

/* Google btn */
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
@media (max-width: 520px) {
  .auth-card { padding: 32px 20px 24px; border-radius: 12px; }
  .fields-row { grid-template-columns: 1fr; }
}
</style>
