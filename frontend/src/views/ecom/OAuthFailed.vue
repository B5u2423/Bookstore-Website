<script setup>
import { useRouter } from 'vue-router'

const router = useRouter()
</script>

<template>
  <div class="auth-page">
    <div class="auth-card">
      <!-- Brand -->
      <div class="auth-brand">
        <span class="auth-brand-icon" aria-hidden="true">📚</span>
        <span class="auth-brand-name">BookShelf</span>
      </div>

      <!-- Error icon -->
      <div class="error-icon-wrap" aria-hidden="true">
        <v-icon icon="mdi-google" size="28" class="provider-icon" />
        <div class="error-badge">
          <v-icon icon="mdi-close" size="12" />
        </div>
      </div>

      <h1 class="auth-heading">Đăng nhập thất bại</h1>
      <p class="auth-subheading">
        Không thể đăng nhập bằng tài khoản Google của bạn. Điều này có thể
        xảy ra do bạn đã từ chối cấp quyền hoặc có lỗi xảy ra phía máy chủ.
      </p>

      <!-- What to do -->
      <div class="suggestions">
        <div class="suggestion-item">
          <v-icon icon="mdi-refresh" size="15" class="suggestion-icon" />
          <span>Thử đăng nhập lại bằng Google</span>
        </div>
        <div class="suggestion-item">
          <v-icon icon="mdi-email-outline" size="15" class="suggestion-icon" />
          <span>Hoặc đăng nhập bằng email và mật khẩu</span>
        </div>
        <div class="suggestion-item">
          <v-icon icon="mdi-headset" size="15" class="suggestion-icon" />
          <span>Liên hệ hỗ trợ nếu vấn đề vẫn tiếp tục</span>
        </div>
      </div>

      <!-- Actions -->
      <div class="action-stack">
        <router-link to="/login" class="btn-primary">
          <v-icon icon="mdi-arrow-left" size="15" class="mr-2" />
          Quay lại đăng nhập
        </router-link>

        <router-link to="/" class="btn-secondary">
          <v-icon icon="mdi-home-outline" size="15" class="mr-2" />
          Về trang chủ
        </router-link>
      </div>
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

  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 16px;
  background-color: var(--paper);
  background-image:
    radial-gradient(ellipse at 20% 60%, rgba(163,38,44,0.07) 0%, transparent 55%),
    radial-gradient(ellipse at 80% 20%, rgba(43,36,32,0.04) 0%, transparent 50%);
}

.auth-card {
  width: 100%;
  max-width: 420px;
  background: #fff;
  border: 1px solid var(--border);
  border-top: 4px solid var(--accent);
  border-radius: 16px;
  padding: 40px 36px 32px;
  box-shadow:
    0 4px 6px -1px rgba(43,36,32,0.06),
    0 10px 30px -8px rgba(43,36,32,0.1);
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
}

.auth-brand {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 28px;
}
.auth-brand-icon { font-size: 1.4rem; line-height: 1; }
.auth-brand-name {
  font-size: 1.15rem;
  font-weight: 700;
  color: var(--ink);
}

.error-icon-wrap {
  position: relative;
  width: 68px;
  height: 68px;
  border-radius: 50%;
  background: var(--accent-soft);
  border: 2px solid #fcd4d4;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}
.provider-icon { color: var(--accent); }
.error-badge {
  position: absolute;
  bottom: -2px;
  right: -2px;
  width: 22px;
  height: 22px;
  border-radius: 50%;
  background: var(--accent);
  border: 2px solid #fff;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.auth-heading {
  font-family: 'Lora', serif;
  font-size: 1.45rem;
  font-weight: 700;
  margin: 0 0 10px;
  color: var(--ink);
}
.auth-subheading {
  font-size: 0.875rem;
  color: var(--muted);
  line-height: 1.6;
  margin: 0 0 24px;
}

.suggestions {
  width: 100%;
  background: var(--paper);
  border: 1px solid var(--border);
  border-radius: 10px;
  padding: 14px 16px;
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 24px;
  text-align: left;
}
.suggestion-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  font-size: 0.845rem;
  color: var(--ink);
  line-height: 1.4;
}
.suggestion-icon {
  color: var(--accent);
  flex-shrink: 0;
  margin-top: 1px;
}

.action-stack {
  display: flex;
  flex-direction: column;
  gap: 10px;
  width: 100%;
}
.btn-primary,
.btn-secondary {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  height: 44px;
  border-radius: 8px;
  font-size: 0.875rem;
  font-weight: 600;
  text-decoration: none;
  transition: background 0.2s, border-color 0.15s, color 0.15s;
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
  background: var(--accent-soft);
}
.btn-primary:focus-visible,
.btn-secondary:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
}

@media (max-width: 480px) {
  .auth-card { padding: 32px 20px 24px; border-radius: 12px; }
}
</style>
