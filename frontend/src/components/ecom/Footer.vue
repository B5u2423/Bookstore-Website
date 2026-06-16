<script setup>
const services = [
  { name: 'Về chúng tôi' },
  { name: 'Hướng dẫn mua sách' },
  { name: 'FAQ' },
  { name: 'Liên hệ' },
]
const supports = [
  { name: 'Điều khoản sử dụng' },
  { name: 'Chính sách bảo mật' },
  { name: 'Chính sách vận chuyển' },
  { name: 'Chính sách đổi trả' },
  { name: 'Hình thức thanh toán' },
]
const accounts = [
  { name: 'Đăng nhập / Tạo tài khoản', route: 'login' },
  { name: 'Chi tiết tài khoản', route: 'user-info' },
  { name: 'Lịch sử mua hàng', route: 'history' },
]

const socials = [
  { icon: 'mdi-facebook', label: 'Facebook', href: '#' },
  { icon: 'mdi-instagram', label: 'Instagram', href: '#' },
  { icon: 'mdi-youtube', label: 'YouTube', href: '#' },
]

const email = ref('')
const subscribed = ref(false)
const brandName = ref('BookShelf')

function handleSubscribe() {
  if (!email.value.trim()) return
  // TODO: subscribe API here
  subscribed.value = true
  email.value = ''
}
</script>

<script>
import { ref } from 'vue'
</script>

<template>
  <footer class="site-footer">
    <!-- newsletter -->
    <div class="newsletter-band">
      <div class="newsletter-inner">
        <div class="newsletter-copy">
          <span class="newsletter-eyebrow">Bản tin sách</span>
          <p class="newsletter-headline">Đừng bỏ lỡ sách mới & ưu đãi đặc biệt</p>
        </div>

        <Transition name="fade-slide" mode="out-in">
          <div v-if="subscribed" key="thanks" class="subscribed-msg">
            <v-icon icon="mdi-check-circle" size="20" class="mr-2" />
            Cảm ơn bạn đã đăng ký!
          </div>

          <div v-else key="form" class="newsletter-form">
            <input
              v-model="email"
              type="email"
              class="newsletter-input"
              placeholder="email của bạn"
              aria-label="Email đăng ký bản tin"
              @keydown.enter="handleSubscribe"
            />
            <button class="newsletter-btn" type="button" @click="handleSubscribe">
              Đăng ký
            </button>
          </div>
        </Transition>
      </div>
    </div>

    <!-- footer body -->
    <div class="footer-body">
      <div class="footer-inner">
        <!-- brand column -->
        <div class="brand-col">
          <router-link to="/" class="brand-wordmark">
            <span class="brand-icon">📚</span>
            <span class="brand-name">{{ brandName }}</span>
          </router-link>
          <p class="brand-tagline">
            Nơi mỗi cuốn sách là một chuyến hành trình.
          </p>
          <div class="social-row">
            <a
              v-for="s in socials"
              :key="s.icon"
              :href="s.href"
              :aria-label="s.label"
              class="social-link"
            >
              <v-icon :icon="s.icon" size="20" />
            </a>
          </div>
        </div>

        <!-- link columns -->
        <nav class="links-grid" aria-label="Footer navigation">
          <div class="link-col">
            <h3 class="link-col-heading">Thông tin</h3>
            <ul class="link-list">
              <li v-for="item in services" :key="item.name">
                <a href="#" class="footer-link">{{ item.name }}</a>
              </li>
            </ul>
          </div>

          <div class="link-col">
            <h3 class="link-col-heading">Chính sách</h3>
            <ul class="link-list">
              <li v-for="item in supports" :key="item.name">
                <a href="#" class="footer-link">{{ item.name }}</a>
              </li>
            </ul>
          </div>

          <div class="link-col">
            <h3 class="link-col-heading">Tài khoản</h3>
            <ul class="link-list">
              <li v-for="item in accounts" :key="item.name">
                <router-link :to="{ name: item.route }" class="footer-link">
                  {{ item.name }}
                </router-link>
              </li>
            </ul>
          </div>
        </nav>
      </div>
    </div>

    <!-- bottom bar -->
    <div class="footer-bottom">
      <span>© {{ new Date().getFullYear() }} {{ brandName }}. All rights reserved.</span>
    </div>
  </footer>
</template>

<style scoped>

.site-footer {
  --accent:      #a3262c;
  --accent-soft: #c94247;      /* lighter for hover on dark bg */
  --ink:         #2b2420;
  --ink-light:   #3d302b;      /* card / band bg */
  --muted:       #8a7d72;
  --muted-light: #b5a99f;      /* text on dark bg */
  --border:      rgba(255,255,255,0.08);
  --paper:       #fbf8f4;

  font-family: system-ui, -apple-system, sans-serif;
  color: var(--muted-light);
  background-color: var(--ink);
}

.newsletter-band {
  background-color: var(--ink-light);
  border-bottom: 1px solid var(--border);
}
.newsletter-inner {
  max-width: 1100px;
  margin: 0 auto;
  padding: 28px 32px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  flex-wrap: wrap;
}
.newsletter-eyebrow {
  display: block;
  font-size: 0.72rem;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: var(--accent-soft);
  font-weight: 600;
  margin-bottom: 4px;
}
.newsletter-headline {
  margin: 0;
  /* font-family: 'Lora', serif; */
  font-size: 1.1rem;
  font-weight: 600;
  color: #fff;
  line-height: 1.3;
}
.newsletter-form {
  display: flex;
  gap: 8px;
  flex: 0 1 480px;
}
.newsletter-input {
  flex: 1;
  min-width: 0;
  height: 42px;
  padding: 0 14px;
  background: rgba(255,255,255,0.06);
  border: 1px solid var(--border);
  border-radius: 6px;
  color: #fff;
  font-size: 0.9rem;
  transition: border-color 0.2s;
}
.newsletter-input::placeholder { color: var(--muted); }
.newsletter-input:focus {
  outline: none;
  border-color: var(--accent-soft);
}
.newsletter-btn {
  height: 42px;
  padding: 0 22px;
  background: var(--accent);
  color: #fff;
  font-size: 0.875rem;
  font-weight: 600;
  letter-spacing: 0.03em;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  white-space: nowrap;
  transition: background 0.2s;
}
.newsletter-btn:hover { background: var(--accent-soft); }
.newsletter-btn:focus-visible {
  outline: 2px solid var(--accent-soft);
  outline-offset: 2px;
}
.subscribed-msg {
  display: flex;
  align-items: center;
  color: #6ee7a0;
  font-weight: 500;
}

.footer-body {
  border-bottom: 1px solid var(--border);
}
.footer-inner {
  max-width: 1100px;
  margin: 0 auto;
  padding: 52px 32px 48px;
  display: flex;
  gap: 48px;
  flex-wrap: wrap;
}

/* Brand column */
.brand-col {
  flex: 0 0 220px;
  display: flex;
  flex-direction: column;
  gap: 14px;
}
.brand-wordmark {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  color: #fff;
}
.brand-icon { font-size: 1.5rem; line-height: 1; }
.brand-name {
  /* font-family: 'Lora', serif; */
  font-size: 1.35rem;
  font-weight: 700;
  letter-spacing: -0.01em;
}
.brand-tagline {
  margin: 0;
  font-size: 0.875rem;
  line-height: 1.6;
  color: var(--muted);
  font-style: italic;
}
.social-row { display: flex; gap: 8px; margin-top: 4px; }
.social-link {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 1px solid var(--border);
  color: var(--muted-light);
  text-decoration: none;
  transition: border-color 0.2s, color 0.2s, background 0.2s;
}
.social-link:hover {
  border-color: var(--accent-soft);
  color: #fff;
  background: rgba(163,38,44,0.2);
}
.social-link:focus-visible {
  outline: 2px solid var(--accent-soft);
  outline-offset: 2px;
}

/* Links grid */
.links-grid {
  flex: 1;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 32px;
}
.link-col-heading {
  font-size: 0.72rem;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  font-weight: 700;
  color: #fff;
  margin: 0 0 16px;
}
.link-list {
  list-style: none;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  gap: 10px;
}
.footer-link {
  color: var(--muted-light);
  text-decoration: none;
  font-size: 0.9rem;
  line-height: 1.4;
  transition: color 0.15s;
  display: inline-block;
}
.footer-link:hover { color: #fff; }
.footer-link:focus-visible {
  outline: 2px solid var(--accent-soft);
  outline-offset: 2px;
  border-radius: 2px;
}

.footer-bottom {
  max-width: 1100px;
  margin: 0 auto;
  padding: 18px 32px;
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 0.8rem;
  color: var(--muted);
  flex-wrap: wrap;
}
.footer-bottom-sep { opacity: 0.4; }
.sr-only {
  position: absolute;
  width: 1px; height: 1px;
  overflow: hidden;
  clip: rect(0,0,0,0);
  white-space: nowrap;
}

.fade-slide-enter-active,
.fade-slide-leave-active { transition: opacity 0.25s ease, transform 0.25s ease; }
.fade-slide-enter-from { opacity: 0; transform: translateY(6px); }
.fade-slide-leave-to   { opacity: 0; transform: translateY(-6px); }

@media (max-width: 768px) {
  .newsletter-inner {
    flex-direction: column;
    align-items: flex-start;
  }
  .newsletter-form { flex: 1 1 auto; width: 100%; }
  .footer-inner { flex-direction: column; }
  .brand-col { flex: 0 0 auto; }
  .links-grid { grid-template-columns: repeat(2, 1fr); }
}

@media (max-width: 480px) {
  .links-grid { grid-template-columns: 1fr; }
  .footer-inner { padding: 36px 20px 32px; }
  .newsletter-inner { padding: 24px 20px; }
  .footer-bottom { padding: 16px 20px; }
}
</style>
