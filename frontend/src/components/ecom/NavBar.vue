<script setup>
import { BookService } from '@/api/book-api'
import { useAuthStore } from '@/stores/auth-store'
import { useCartStore } from '@/stores/cart-store'
import { useUserProfileStore } from '@/stores/user-profile-store'
import debounce from 'lodash.debounce'
import { onUnmounted, ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const authStore = useAuthStore()
const cartStore = useCartStore()
const userProfileStore = useUserProfileStore()

const query = ref('')
const searchResults = ref([])
const searchOpen = ref(false)

const brandName = ref('BookShelf')

const search = debounce(async (q) => {
  if (!q) {
    searchResults.value = []
    return
  }
  try {
    const res = await BookService.searchBook({ keyword: q })
    searchResults.value = res
  } catch (e) {
    console.error('Error searching book', e)
  }
}, 400)

watch(query, (q) => search(q))
onUnmounted(() => search.cancel())

const goToHomePage = () => router.push('/')

async function handleLogout() {
  try {
    await authStore.logout()
    cartStore.reset()
    router.push('/')
  } catch (error) {
    console.error(error)
  }
}
</script>

<template>
  <v-app-bar flat class="site-nav" height="64">
    <!-- brand -->
    <div
      class="nav-brand"
      @click="goToHomePage"
      role="link"
      tabindex="0"
      @keydown.enter="goToHomePage"
      aria-label="Trang chủ"
    >
      <span class="brand-icon" aria-hidden="true">📚</span>
      <span class="brand-name">{{ brandName }}</span>
    </div>

    <div class="nav-divider" aria-hidden="true" />

    <!-- search bar -->
    <div class="nav-search-wrap">
      <v-autocomplete
        v-model:search="query"
        :items="searchResults"
        :no-filter="true"
        :menu-icon="null"
        placeholder="Tìm kiếm sách, tác giả..."
        variant="solo"
        density="compact"
        hide-details
        no-data-text="Không có kết quả"
        class="nav-search"
        @focus="searchOpen = true"
        @blur="searchOpen = false"
      >
        <template v-slot:prepend-inner>
          <v-icon icon="mdi-magnify" size="18" class="search-icon" />
        </template>

        <template v-slot:item="{ props, item }">
          <v-list-item
            v-bind="props"
            :title="item.raw.title"
            :subtitle="item.raw.author"
            :to="{ name: 'book-detail', params: { id: item.raw.id, slug: item.raw.urlSlug } }"
            class="search-result-item"
          >
            <template v-slot:prepend>
              <v-img
                :src="item.raw.imageUrl"
                width="44"
                height="60"
                cover
                class="search-thumb rounded"
              />
            </template>
          </v-list-item>
        </template>
      </v-autocomplete>
    </div>

    <v-spacer />

    <!-- actions -->
    <div class="nav-actions">
      <!-- not logged in -->
      <template v-if="!authStore.isAuthenticated">
        <router-link :to="{ name: 'register' }" class="nav-text-btn">
          Đăng ký
        </router-link>
        <router-link :to="{ name: 'login' }" class="nav-text-btn nav-text-btn--accent">
          Đăng nhập
        </router-link>
      </template>

      <!-- logged in -->
      <v-menu v-else location="bottom end" transition="fade-transition">
        <template v-slot:activator="{ props }">
          <button class="user-pill" v-bind="props" aria-haspopup="true">
            <span class="user-avatar" aria-hidden="true">
              {{ (userProfileStore.userInfo.name || 'U')[0].toUpperCase() }}
            </span>
            <span class="user-name">{{ userProfileStore.userInfo.name }}</span>
            <v-icon icon="mdi-chevron-down" size="16" class="user-chevron" />
          </button>
        </template>

        <v-list class="user-menu" elevation="3">
          <v-list-item :to="{ name: 'user-info' }" class="user-menu-item">
            <template v-slot:prepend>
              <v-icon icon="mdi-account-circle-outline" size="18" />
            </template>
            <v-list-item-title>Thông tin cá nhân</v-list-item-title>
          </v-list-item>

          <v-divider class="my-1" />

          <v-list-item @click="handleLogout" class="user-menu-item user-menu-item--danger">
            <template v-slot:prepend>
              <v-icon icon="mdi-logout" size="18" />
            </template>
            <v-list-item-title>Đăng xuất</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>

      <!-- cart -->
      <router-link :to="{ name: 'cart' }" class="icon-btn" aria-label="Giỏ hàng">
        <v-badge
          :content="cartStore.cartItemsCount || ''"
          :model-value="cartStore.cartItemsCount > 0"
          color="error"
          floating
        >
          <v-icon icon="mdi-cart-outline" size="22" />
        </v-badge>
      </router-link>

      <!-- notif -->
      <button class="icon-btn" aria-label="Thông báo">
        <v-icon icon="mdi-bell-outline" size="22" />
      </button>
    </div>
  </v-app-bar>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Lora:wght@600;700&display=swap');

/* pallete */
.site-nav {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;

  background-color: var(--paper) !important;
  border-bottom: 1px solid var(--border) !important;
  box-shadow: 0 1px 0 0 var(--border) !important;
  padding: 0 32px !important;
  color: var(--ink);
}

/* brand */
.nav-brand {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  text-decoration: none;
  flex-shrink: 0;
  padding: 4px 0;
  border-radius: 4px;
  transition: opacity 0.2s;
}
.nav-brand:hover { opacity: 0.8; }
.nav-brand:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 3px;
}
.brand-icon { font-size: 1.4rem; line-height: 1; }
.brand-name {
  font-family: 'Lora', serif;
  font-size: 1.25rem;
  font-weight: 700;
  color: var(--ink);
  letter-spacing: -0.01em;
}

.nav-divider {
  width: 1px;
  height: 28px;
  background: var(--border);
  margin: 0 20px;
  flex-shrink: 0;
}

/* search */
.nav-search-wrap {
  flex: 1;
  max-width: 480px;
}

.nav-search :deep(.v-field) {
  background: #fff !important;
  border: 1px solid var(--border) !important;
  border-radius: 15px !important;
  box-shadow: none !important;
  font-size: 0.9rem;
  transition: border-color 0.2s;
}
.nav-search :deep(.v-field--focused) {
  border-color: var(--accent) !important;
}
.nav-search :deep(.v-field__input) {
  padding-top: 6px !important;
  padding-bottom: 6px !important;
  color: var(--ink);
}
.nav-search :deep(.v-field__input::placeholder) {
  color: var(--muted);
}
.nav-search :deep(.v-field__prepend-inner) {
  padding-top: 6px;
}
.search-icon {
  color: var(--muted) !important;
  margin-right: 2px;
}

/* search result dropdown */
.search-result-item {
  padding: 8px 12px !important;
  border-bottom: 1px solid var(--border);
}
.search-result-item:last-child { border-bottom: none; }
.search-result-item :deep(.v-list-item-title) {
  font-size: 0.9rem;
  font-weight: 600;
  color: var(--ink);
}
.search-result-item :deep(.v-list-item-subtitle) {
  font-size: 0.8rem;
  color: var(--muted);
}
.search-thumb {
  border: 1px solid var(--border);
  margin-right: 12px;
  flex-shrink: 0;
}

/* actions button */
.nav-actions {
  display: flex;
  align-items: center;
  gap: 4px;
  flex-shrink: 0;
}

/* Text nav buttons (guest) */
.nav-text-btn {
  display: inline-flex;
  align-items: center;
  height: 36px;
  padding: 0 14px;
  border-radius: 6px;
  font-size: 0.875rem;
  font-weight: 500;
  text-decoration: none;
  color: var(--ink);
  transition: background 0.15s;
}
.nav-text-btn:hover { background: var(--accent-soft); }
.nav-text-btn--accent {
  background: var(--accent);
  color: #fff;
  margin-left: 4px;
}
.nav-text-btn--accent:hover { background: #8e1f24; }
.nav-text-btn:focus-visible,
.nav-text-btn--accent:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
}

/* user pill */
.user-pill {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  height: 36px;
  padding: 0 10px 0 4px;
  background: var(--accent-soft);
  border: 1px solid transparent;
  border-radius: 99px;
  cursor: pointer;
  font-size: 0.875rem;
  font-weight: 500;
  color: var(--ink);
  transition: border-color 0.2s, background 0.2s;
}
.user-pill:hover { border-color: var(--accent); }
.user-pill:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
}
.user-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: var(--accent);
  color: #fff;
  font-size: 0.8rem;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.user-name {
  max-width: 120px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.user-chevron { color: var(--muted); flex-shrink: 0; }

/* User dropdown menu */
.user-menu {
  min-width: 200px;
  border: 1px solid var(--border) !important;
  border-radius: 10px !important;
  overflow: hidden;
  padding: 6px !important;
}
.user-menu-item {
  border-radius: 6px;
  margin-bottom: 2px;
  font-size: 0.9rem !important;
}
.user-menu-item :deep(.v-list-item-title) {
  font-size: 0.875rem;
}
.user-menu-item :deep(.v-list-item__prepend) {
  width: 32px;
}
.user-menu-item--danger { color: var(--accent) !important; }
.user-menu-item--danger :deep(.v-icon) { color: var(--accent) !important; }

/* Icon buttons (cart, bell) */
.icon-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 8px;
  border: none;
  background: transparent;
  color: var(--ink);
  cursor: pointer;
  text-decoration: none;
  transition: background 0.15s, color 0.15s;
  margin-left: 2px;
}
.icon-btn:hover { background: var(--accent-soft); color: var(--accent); }
.icon-btn:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 2px;
}

@media (max-width: 768px) {
  .site-nav { padding: 0 16px !important; }
  .nav-divider { margin: 0 12px; }
  .nav-search-wrap { max-width: 240px; }
  .user-name { display: none; }
  .user-chevron { display: none; }
  .nav-text-btn { padding: 0 10px; font-size: 0.8rem; }
}

@media (max-width: 480px) {
  .nav-search-wrap { max-width: 160px; }
  .brand-name { display: none; }
  .nav-divider { display: none; }
}
</style>
