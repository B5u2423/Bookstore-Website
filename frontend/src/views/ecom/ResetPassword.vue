<script setup>
import { AuthService } from '@/api/auth-api'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const email = ref('')
const form = ref(null)
const isValidForm = ref(false)
const isLoading = ref(false)
const submitted = ref(false)

const rules = {
  required: (v) => !!v || 'Không được bỏ trống trường này',
  email: (v) => /[\w\d.-]{6,30}@[\w\d.-]+/.test(v) || 'Email không hợp lệ',
}

async function handleResetPassword() {
  const { valid } = await form.value.validate()
  if (!valid) return

  isLoading.value = true
  try {
    await AuthService.resetPassword({ email: email.value })
    submitted.value = true
  } catch {
    console.error('Error resetting password')
  } finally {
    isLoading.value = false
  }
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

      <!-- Success state -->
      <Transition name="fade" mode="out-in">
        <div v-if="submitted" key="success" class="success-state">
          <div class="success-icon-wrap" aria-hidden="true">
            <v-icon icon="mdi-email-check-outline" size="36" />
          </div>
          <h1 class="auth-heading">Kiểm tra hộp thư!</h1>
          <p class="auth-subheading">
            Chúng tôi đã gửi hướng dẫn đặt lại mật khẩu đến
            <strong>{{ email }}</strong>. Vui lòng kiểm tra cả thư mục spam.
          </p>
          <router-link
            to="/login"
            class="submit-btn"
            style="text-decoration:none; display:flex; justify-content:center;"
          >
            Quay lại đăng nhập
          </router-link>
        </div>

        <!-- Form state -->
        <div v-else key="form">
          <h1 class="auth-heading">Khôi phục mật khẩu</h1>
          <p class="auth-subheading">
            Nhập email đã đăng ký, chúng tôi sẽ đặt lại mật khẩu và gửi vào
            hòm thư của bạn.
          </p>

          <v-form ref="form" v-model="isValidForm" @submit.prevent="handleResetPassword">
            <div class="field-group">
              <label class="field-label" for="reset-email">Email</label>
              <v-text-field
                id="reset-email"
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
              <span>{{ isLoading ? 'Đang gửi…' : 'Gửi mật khẩu khôi phục' }}</span>
            </button>
          </v-form>

          <p class="auth-footer">
            Nhớ mật khẩu?
            <router-link to="/login" class="auth-footer-link">Đăng nhập</router-link>
          </p>
        </div>
      </Transition>
    </div>
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
  --green:       #2a9d5c;
  --green-soft:  #f0fdf6;

  display: flex;
  justify-content: center;
  padding: 32px 16px;
  background-color: var(--paper);
  background-image:
    radial-gradient(ellipse at 25% 60%, rgba(163,38,44,0.06) 0%, transparent 55%),
    radial-gradient(ellipse at 80% 20%, rgba(43,36,32,0.04) 0%, transparent 50%);
}

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

.auth-heading {
  font-size: 1.5rem;
  font-weight: 700;
  text-align: center;
  margin: 0 0 8px;
  color: var(--ink);
}
.auth-subheading {
  text-align: center;
  color: var(--muted);
  font-size: 0.875rem;
  line-height: 1.6;
  margin: 0 0 24px;
}
.auth-subheading strong { color: var(--ink); }

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
  margin-top: 4px;
  transition: background 0.2s, opacity 0.2s;
}
.submit-btn:hover:not(:disabled) { background: #8e1f24; }
.submit-btn:disabled { opacity: 0.5; cursor: not-allowed; }
.submit-btn:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 3px;
}

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

.success-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}
.success-icon-wrap {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: var(--green-soft);
  color: var(--green);
  border: 2px solid #bbf0d4;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.fade-enter-active,
.fade-leave-active { transition: opacity 0.2s ease, transform 0.2s ease; }
.fade-enter-from   { opacity: 0; transform: translateY(8px); }
.fade-leave-to     { opacity: 0; transform: translateY(-8px); }

@media (max-width: 480px) {
  .auth-card { padding: 32px 20px 24px; border-radius: 12px; }
}
</style>
