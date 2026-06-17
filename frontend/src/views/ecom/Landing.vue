<script setup>
import { BookService } from '@/api/book-api'
import BookSlideGroup from '@/components/books/BookSlideGroup.vue'
import { onMounted, ref } from 'vue'

const bestsellers = ref([])
const newbooks = ref([])
const loading = ref(true)
const slide = ref(0)

const fetchBooks = async () => {
  try {
    ;[bestsellers.value, newbooks.value] = await Promise.all([
      BookService.getBooksInCollectionForLandingPage({ collection: 'best-sellers' }),
      BookService.getBooksInCollectionForLandingPage({ collection: 'new' }),
    ])
  } catch (error) {
    console.error('Error fetching books:', error)
  } finally {
    loading.value = false
  }
}

const carouselItems = [
  {
    src: 'https://theme.hstatic.net/200000845405/1001223012/14/home_slider_image_2.jpg?v=475',
    label: 'Khám phá kho sách phong phú',
    cta: 'Xem ngay',
  },
  {
    src: 'https://theme.hstatic.net/200000845405/1001223012/14/home_slider_image_3.jpg?v=475',
    label: 'Sách hay, giá tốt mỗi ngày',
    cta: 'Mua sắm',
  },
  {
    src: 'https://cdn.hstatic.net/files/200001055148/file/banner_xmas_750x422px-01.jpg',
    label: 'Ưu đãi đặc biệt mùa lễ hội',
    cta: 'Xem ưu đãi',
  },
]

const categories = [
  { label: 'Văn học', icon: 'mdi-book-open-variant', slug: 'van-hoc' },
  { label: 'Kinh tế', icon: 'mdi-chart-line', slug: 'kinh-te' },
  { label: 'Thiếu nhi', icon: 'mdi-star-outline', slug: 'sach-thieu-nhi' },
  { label: 'Tâm lý', icon: 'mdi-head-heart-outline', slug: 'tam-ly' },
  { label: 'Lịch sử', icon: 'mdi-timeline-outline', slug: 'lich-su' },
]

onMounted(fetchBooks)
</script>

<template>
  <div class="landing">
    <!-- carousel -->
    <section class="hero" aria-label="Ảnh giới thiệu">
      <v-carousel
        v-model="slide"
        hide-delimiters
        show-arrows="hover"
        cycle
        interval="5000"
        height="auto"
        crossfade
        class="hero-carousel"
      >
        <v-carousel-item
          v-for="(item, i) in carouselItems"
          :key="i"
          :src="item.src"
          cover
          class="hero-slide"
        >
          <!-- Gradient overlay + caption -->
          <div class="hero-overlay">
            <div class="hero-caption">
              <p class="hero-caption-text">{{ item.label }}</p>
              <router-link
                :to="{ name: 'category-page', params: { slug: 'tat-ca' } }"
                class="hero-cta"
              >
                {{ item.cta }}
                <v-icon icon="mdi-arrow-right" size="16" class="ml-1" />
              </router-link>
            </div>
          </div>
        </v-carousel-item>
      </v-carousel>

      <!-- Dot indicators -->
      <div class="hero-dots" aria-hidden="true">
        <button
          v-for="(_, i) in carouselItems"
          :key="i"
          class="hero-dot"
          :class="{ 'hero-dot--active': slide === i }"
          @click="slide = i"
        />
      </div>
    </section>

    <!-- trust bar -->
    <div class="trust-bar">
      <div class="trust-item">
        <v-icon icon="mdi-truck-fast-outline" size="20" class="trust-icon" />
        <span>Miễn phí vận chuyển</span>
      </div>
      <div class="trust-sep" aria-hidden="true" />
      <div class="trust-item">
        <v-icon icon="mdi-shield-check-outline" size="20" class="trust-icon" />
        <span>Thanh toán an toàn</span>
      </div>
      <div class="trust-sep" aria-hidden="true" />
      <div class="trust-item">
        <v-icon icon="mdi-refresh" size="20" class="trust-icon" />
        <span>Đổi trả trong 30 ngày</span>
      </div>
      <div class="trust-sep" aria-hidden="true" />
      <div class="trust-item">
        <v-icon icon="mdi-headset" size="20" class="trust-icon" />
        <span>Hỗ trợ 24/7</span>
      </div>
    </div>

    <!-- main -->
    <div class="landing-content">
      <!-- quick links -->
      <section class="cat-section" aria-label="Danh mục nổi bật">
        <div class="cat-grid">
          <router-link
            v-for="cat in categories"
            :key="cat.slug"
            :to="{ name: 'category-page', params: { slug: cat.slug } }"
            class="cat-pill"
          >
            <v-icon :icon="cat.icon" size="18" class="cat-pill-icon" />
            {{ cat.label }}
          </router-link>
        </div>
      </section>

      <!-- book sliders -->
      <book-slide-group
        group-header="Bán chạy nhất"
        :books="bestsellers"
        :loading="loading"
        route-to="category-page"
        :route-params="{ slug: 'ban-chay' }"
      />

      <book-slide-group
        group-header="Sách mới về"
        :books="newbooks"
        :loading="loading"
        route-to="category-page"
        :route-params="{ slug: 'sach-moi' }"
      />

      <!-- call to action strip -->
      <div class="promo-banner">
        <div class="promo-banner-text">
          <p class="promo-banner-eyebrow">Ưu đãi hội viên</p>
          <h2 class="promo-banner-heading">
            Đăng ký tài khoản — nhận ngay ưu đãi độc quyền
          </h2>
        </div>
        <router-link :to="{ name: 'register' }" class="promo-banner-btn">
          Tạo tài khoản miễn phí
        </router-link>
      </div>
    </div>
  </div>
</template>

<style scoped>
.landing {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --ink-light:   #3d302b;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;

  background: var(--paper);
  color: var(--ink);
  font-family: system-ui, -apple-system, sans-serif;
}

.hero { position: relative; }

.hero-carousel :deep(.v-carousel__controls) { display: none; }
.hero-carousel :deep(.v-window__container) { border-radius: 0; }

.hero-slide { position: relative; aspect-ratio: 16 / 6; }

.hero-overlay {
  position: absolute;
  inset: 0;
  background: linear-gradient(
    to right,
    rgba(43,36,32,0.58) 0%,
    rgba(43,36,32,0.2) 50%,
    transparent 100%
  );
  display: flex;
  align-items: flex-end;
  padding: 40px 60px;
}
.hero-caption {
  display: flex;
  flex-direction: column;
  gap: 16px;
  max-width: 420px;
}
.hero-caption-text {
  font-size: clamp(1.2rem, 2.5vw, 1.75rem);
  font-weight: 700;
  color: #fff;
  margin: 0;
  line-height: 1.3;
  text-shadow: 0 1px 3px rgba(0,0,0,0.3);
}
.hero-cta {
  display: inline-flex;
  align-items: center;
  align-self: flex-start;
  padding: 10px 22px;
  background: var(--accent);
  color: #fff;
  border-radius: 8px;
  text-decoration: none;
  font-size: 0.875rem;
  font-weight: 700;
  letter-spacing: 0.02em;
  transition: background 0.2s;
}
.hero-cta:hover { background: #8e1f24; }

/* Custom dot indicators */
.hero-dots {
  position: absolute;
  bottom: 16px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  gap: 6px;
  z-index: 2;
}
.hero-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  border: none;
  background: rgba(255,255,255,0.45);
  cursor: pointer;
  transition: background 0.2s, transform 0.2s;
  padding: 0;
}
.hero-dot--active {
  background: #fff;
  transform: scale(1.3);
}

.trust-bar {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0;
  background: #fff;
  border-top: 1px solid var(--border);
  border-bottom: 1px solid var(--border);
  padding: 14px 24px;
  flex-wrap: wrap;
}
.trust-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 0.82rem;
  font-weight: 500;
  color: var(--ink);
  padding: 6px 24px;
}
.trust-icon { color: var(--accent); flex-shrink: 0; }
.trust-sep {
  width: 1px;
  height: 20px;
  background: var(--border);
}

.landing-content {
  max-width: 1100px;
  margin: 0 auto;
  padding: 30px 24px;
}

.cat-section { margin-bottom: 12px; }
.cat-grid {
  display: flex;
  gap: 10px;
  flex-wrap: wrap;
}
.cat-pill {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  padding: 8px 16px;
  background: #fff;
  border: 1px solid var(--border);
  border-radius: 99px;
  text-decoration: none;
  font-size: 0.84rem;
  font-weight: 500;
  color: var(--ink);
  transition: background 0.15s, border-color 0.15s, color 0.15s;
}
.cat-pill:hover {
  background: var(--accent-soft);
  border-color: var(--accent);
  color: var(--accent);
}
.cat-pill:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
}
.cat-pill-icon { color: var(--accent); flex-shrink: 0; }

.promo-banner {
  margin-top: 16px;
  background: var(--ink-light);
  border-radius: 14px;
  padding: 36px 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 24px;
  flex-wrap: wrap;
}
.promo-banner-eyebrow {
  font-size: 0.75rem;
  text-transform: uppercase;
  letter-spacing: 0.1em;
  color: var(--accent-soft);
  margin: 0 0 6px;
  font-weight: 600;
}
.promo-banner-heading {
  font-size: clamp(1rem, 2vw, 1.3rem);
  font-weight: 700;
  color: #fff;
  margin: 0;
  line-height: 1.35;
}
.promo-banner-btn {
  display: inline-flex;
  align-items: center;
  padding: 12px 26px;
  background: var(--accent);
  color: #fff;
  border-radius: 8px;
  text-decoration: none;
  font-size: 0.875rem;
  font-weight: 700;
  white-space: nowrap;
  flex-shrink: 0;
  transition: background 0.2s;
}
.promo-banner-btn:hover { background: #c94247; }
.promo-banner-btn:focus-visible {
  outline: 2px solid #fff;
  outline-offset: 2px;
}

@media (max-width: 768px) {
  .hero-overlay { padding: 24px 28px; }
  .trust-sep { display: none; }
  .trust-bar { justify-content: flex-start; gap: 4px; }
  .trust-item { padding: 4px 12px; }
  .landing-content { padding: 24px 16px 48px; }
  .promo-banner { padding: 24px 20px; }
}

@media (max-width: 480px) {
  .hero-slide { aspect-ratio: 4 / 3; }
  .hero-overlay { align-items: center; }
  .promo-banner { flex-direction: column; align-items: flex-start; }
}
</style>
