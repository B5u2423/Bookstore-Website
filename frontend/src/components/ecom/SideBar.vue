<script setup>
import { CategoryService } from '@/api/category-api'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const categories = ref([])
const loading = ref(true)
const openGroups = ref([])
const router = useRouter()

async function fetchParentCategories() {
  loading.value = true
  try {
    const res = await CategoryService.fetchParentCategories()
    categories.value = res
  } catch (error) {
    console.error('Error fetching all categories', error)
  } finally {
    loading.value = false
  }
}

function toggleGroup(name) {
  const i = openGroups.value.indexOf(name)
  if (i === -1) openGroups.value.push(name)
  else openGroups.value.splice(i, 1)
}

function isOpen(name) {
  return openGroups.value.includes(name)
}

onMounted(fetchParentCategories)
</script>

<template>
  <aside class="cat-sidebar" aria-label="Danh mục sách">
    <div class="cat-header">
      <v-icon icon="mdi-bookshelf" size="16" class="cat-header-icon" aria-hidden="true" />
      <span>Danh mục</span>
    </div>

    <!-- Skeleton -->
    <template v-if="loading">
      <div class="cat-skeleton" v-for="n in 6" :key="n">
        <div class="skel-line" :style="{ width: (55 + Math.random() * 35) + '%' }" />
      </div>
    </template>

    <nav v-else>
      <!-- all books -->
      <router-link
        :to="{ name: 'category-page', params: { slug: 'tat-ca' } }"
        class="cat-item cat-item--all"
        active-class="cat-item--active"
      >
        <v-icon icon="mdi-view-grid-outline" size="15" class="cat-item-icon" />
        Tất cả sách
      </router-link>

      <div class="cat-divider" />

      <!-- category groups -->
      <div
        v-for="c in categories"
        :key="c.categoryName"
        class="cat-group"
      >
        <!-- parent -->
        <div class="cat-parent-row">
          <router-link
            :to="{ name: 'category-page', params: { slug: c.categorySlug } }"
            class="cat-parent-label"
            active-class="cat-item--active"
          >
            {{ c.categoryName }}
          </router-link>

          <button
            v-if="c.children?.length"
            class="cat-toggle"
            :aria-expanded="isOpen(c.categoryName)"
            :aria-label="`Mở rộng ${c.categoryName}`"
            @click="toggleGroup(c.categoryName)"
          >
            <v-icon
              :icon="isOpen(c.categoryName) ? 'mdi-chevron-up' : 'mdi-chevron-down'"
              size="16"
            />
          </button>
        </div>

        <!-- children -->
        <Transition name="cat-expand">
          <div v-if="isOpen(c.categoryName) && c.children?.length" class="cat-children">
            <router-link
              v-for="sub in c.children"
              :key="sub.categorySlug"
              :to="{ name: 'category-page', params: { slug: sub.categorySlug } }"
              class="cat-child"
              active-class="cat-item--active"
            >
              <span class="cat-child-dot" aria-hidden="true" />
              {{ sub.categoryName }}
            </router-link>
          </div>
        </Transition>
      </div>
    </nav>
  </aside>
</template>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Lora:wght@600&display=swap');

/* palette */
.cat-sidebar {
  --accent:      #a3262c;
  --accent-soft: #f7e9e8;
  --ink:         #2b2420;
  --muted:       #8a7d72;
  --border:      #e8e1d8;
  --paper:       #fbf8f4;

  background: #fff;
  border: 1px solid var(--border);
  border-radius: 12px;
  overflow: hidden;
  font-size: 0.875rem;
  color: var(--ink);
  margin-bottom: 24px;
}

/* header */
.cat-header {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 14px 16px 12px;
  background: var(--paper);
  border-bottom: 1px solid var(--border);
  font-family: 'Lora', serif;
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--ink);
  letter-spacing: 0.01em;
}
.cat-header-icon { color: var(--accent); }

/* loader skeleton */
.cat-skeleton {
  padding: 10px 16px;
  border-bottom: 1px solid var(--border);
}
.skel-line {
  height: 12px;
  background: linear-gradient(90deg, var(--border) 25%, #f0ebe4 50%, var(--border) 75%);
  background-size: 200% 100%;
  border-radius: 6px;
  animation: shimmer 1.4s infinite;
}
@keyframes shimmer {
  0%   { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

/* all books */
.cat-item--all {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 11px 16px;
  text-decoration: none;
  color: var(--ink);
  font-weight: 600;
  transition: background 0.15s, color 0.15s;
}
.cat-item--all:hover { background: var(--accent-soft); color: var(--accent); }
.cat-item--all.cat-item--active {
  background: var(--accent-soft);
  color: var(--accent);
}
.cat-item-icon { flex-shrink: 0; }

.cat-divider {
  height: 1px;
  background: var(--border);
}

/* category group */
.cat-group {
  border-bottom: 1px solid var(--border);
}
.cat-group:last-child { border-bottom: none; }

/* Parent row */
.cat-parent-row {
  display: flex;
  align-items: center;
  padding: 0 4px 0 16px;
  min-height: 42px;
  transition: background 0.15s;
}
.cat-parent-row:hover { background: var(--accent-soft); }

.cat-parent-label {
  flex: 1;
  text-decoration: none;
  color: var(--ink);
  font-weight: 500;
  line-height: 1.4;
  padding: 10px 0;
  transition: color 0.15s;
}
.cat-parent-label:hover { color: var(--accent); }
.cat-parent-label.cat-item--active { color: var(--accent); font-weight: 600; }

/* toggle chevron button */
.cat-toggle {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  border: none;
  background: transparent;
  color: var(--muted);
  cursor: pointer;
  border-radius: 6px;
  transition: background 0.15s, color 0.15s;
}
.cat-toggle:hover { background: var(--border); color: var(--ink); }
.cat-toggle:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: 1px;
}

/* children categories */
.cat-children {
  background: var(--paper);
  border-top: 1px solid var(--border);
  padding: 4px 0;
}

.cat-child {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 16px 8px 28px;
  text-decoration: none;
  color: var(--muted);
  font-size: 0.845rem;
  transition: background 0.15s, color 0.15s;
}
.cat-child:hover { background: var(--accent-soft); color: var(--accent); }
.cat-child.cat-item--active { color: var(--accent); font-weight: 600; }
.cat-child.cat-item--active .cat-child-dot { background: var(--accent); }

.cat-child-dot {
  width: 5px;
  height: 5px;
  border-radius: 50%;
  background: var(--border);
  flex-shrink: 0;
  transition: background 0.15s;
}
.cat-child:hover .cat-child-dot { background: var(--accent); }

/* transition */
.cat-expand-enter-active,
.cat-expand-leave-active {
  transition: max-height 0.25s ease, opacity 0.2s ease;
  overflow: hidden;
  max-height: 500px;
}
.cat-expand-enter-from,
.cat-expand-leave-to {
  max-height: 0;
  opacity: 0;
}

/* focus category */
.cat-item--all:focus-visible,
.cat-parent-label:focus-visible,
.cat-child:focus-visible {
  outline: 2px solid var(--accent);
  outline-offset: -2px;
}
</style>
