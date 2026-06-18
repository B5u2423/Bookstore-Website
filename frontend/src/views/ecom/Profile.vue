<script setup>
import { useCartStore } from '@/stores/cart-store'
import { useUserProfileStore } from '@/stores/user-profile-store'
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const userInfoStore = useUserProfileStore()
const cartStore = useCartStore()
const route = useRoute()

const items = [
  { label: 'Thông tin cá nhân', nameRoute: 'user-info', icon: 'mdi-account-circle-outline' },
  { label: 'Lịch sử đơn hàng', nameRoute: 'history', icon: 'mdi-receipt-text-outline' },
]

const currentItem = computed(() => items.find(i => i.nameRoute === route.name))

onMounted(() => {
  userInfoStore.getUserInfo()
  cartStore.syncCartWithBackEnd()
})
</script>

<template>
  <div class="profile-page">
    <!-- Sidebar -->
    <aside class="profile-sidebar" aria-label="Điều hướng tài khoản">
      <!-- User identity block -->
      <div class="profile-identity">
        <div class="profile-avatar" aria-hidden="true">
          {{ (userInfoStore.userInfo?.name || 'U')[0].toUpperCase() }}
        </div>
        <div class="profile-identity-text">
          <p class="profile-name">{{ userInfoStore.userInfo?.name || '—' }}</p>
          <p class="profile-email">{{ userInfoStore.userInfo?.email || '' }}</p>
        </div>
      </div>

      <div class="sidebar-divider" />

      <!-- Nav links -->
      <nav>
        <p class="sidebar-section-label">Tài khoản của tôi</p>
        <router-link
          v-for="item in items"
          :key="item.nameRoute"
          :to="{ name: item.nameRoute }"
          class="sidebar-link"
          active-class="sidebar-link--active"
        >
          <v-icon :icon="item.icon" size="18" class="sidebar-link-icon" />
          {{ item.label }}
        </router-link>
      </nav>
    </aside>

    <!-- Main content -->
    <main class="profile-main">
      <!-- Breadcrumb header -->
      <div class="profile-content-header">
        <h1 class="profile-content-title">
          {{ currentItem?.label || 'Tài khoản' }}
        </h1>
        <div class="profile-breadcrumb">
          <router-link to="/" class="breadcrumb-link">Trang chủ</router-link>
          <v-icon icon="mdi-chevron-right" size="14" class="breadcrumb-sep" />
          <span>{{ currentItem?.label || 'Tài khoản' }}</span>
        </div>
      </div>

      <div class="profile-content-body">
        <router-view />
      </div>
    </main>
  </div>
</template>

<style scoped>
.profile-page {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;

  max-width: 1100px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 240px 1fr;
  gap: 28px;
  align-items: start;
  color: var(--ink);
  font-family: system-ui, -apple-system, sans-serif;
  font-size: 0.9rem;
}

.profile-sidebar {
  background: #fff;
  border: 1px solid var(--border);
  border-radius: 12px;
  overflow: hidden;
  position: sticky;
  top: 80px;
}

.profile-identity {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px 16px;
  background: var(--paper);
}
.profile-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: var(--accent);
  color: #fff;
  font-size: 1.1rem;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.profile-identity-text { min-width: 0; }
.profile-name {
  font-weight: 600;
  font-size: 0.9rem;
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  color: var(--ink);
}
.profile-email {
  font-size: 0.77rem;
  color: var(--muted);
  margin: 2px 0 0;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.sidebar-divider {
  height: 1px;
  background: var(--border);
}

nav { padding: 10px 8px 12px; }
.sidebar-section-label {
  font-size: 0.7rem;
  text-transform: uppercase;
  letter-spacing: 0.08em;
  font-weight: 700;
  color: var(--muted);
  padding: 6px 10px 8px;
  margin: 0;
}
.sidebar-link {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 12px;
  border-radius: 8px;
  text-decoration: none;
  color: var(--ink);
  font-size: 0.875rem;
  font-weight: 500;
  transition: background 0.15s, color 0.15s;
  margin-bottom: 2px;
}
.sidebar-link:hover {
  background: var(--accent-soft);
  color: var(--accent);
}
.sidebar-link--active {
  background: var(--accent-soft);
  color: var(--accent);
  font-weight: 600;
}
.sidebar-link--active .sidebar-link-icon { color: var(--accent); }
.sidebar-link-icon { color: var(--muted); flex-shrink: 0; transition: color 0.15s; }
.sidebar-link:hover .sidebar-link-icon { color: var(--accent); }
.sidebar-link:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: -2px;
}

.profile-main { min-width: 0; }

.profile-content-header {
  display: flex;
  align-items: baseline;
  justify-content: space-between;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 20px;
  padding-bottom: 16px;
  border-bottom: 1px solid var(--border);
}
.profile-content-title {
  font-size: 1.4rem;
  font-weight: 700;
  margin: 0;
  color: var(--ink);
}
.profile-breadcrumb {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 0.8rem;
  color: var(--muted);
}
.breadcrumb-link {
  color: var(--muted);
  text-decoration: none;
  transition: color 0.15s;
}
.breadcrumb-link:hover { color: var(--accent); }
.breadcrumb-sep { color: var(--border); }

.profile-content-body {
  background: #fff;
  border: 1px solid var(--border);
  border-radius: 12px;
  overflow: hidden;
}

@media (max-width: 768px) {
  .profile-page {
    grid-template-columns: 1fr;
    padding: 20px 16px 48px;
    gap: 16px;
  }
  .profile-sidebar {
    position: static;
    /* Horizontal pill nav on mobile */
    display: flex;
    flex-direction: column;
  }
  .profile-identity { padding: 16px; }
  nav {
    display: flex;
    flex-direction: row;
    padding: 8px;
    gap: 6px;
    overflow-x: auto;
  }
  .sidebar-section-label { display: none; }
  .sidebar-link {
    flex-shrink: 0;
    margin-bottom: 0;
  }
}
</style>
