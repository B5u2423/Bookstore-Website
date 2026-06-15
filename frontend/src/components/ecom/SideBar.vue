<script setup>
import { CategoryService } from '@/api/category-api'
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'

const categories = ref([])
const router = useRouter()

function capitalizeFirstWord(sentence) {
  const words = sentence.split(' ')

  return words
    .map((word) => {
      return word[0].toUpperCase() + word.substring(1)
    })
    .join(' ')
}

async function fetchParentCategories() {
  try {
    const res = await CategoryService.fetchParentCategories()
    categories.value = res
  } catch (error) {
    console.error('Error fetching all categories')
  }
}

function reRoute(route) {
  router.push({ name: 'category-page', params: { slug: route } })
}

onMounted(() => {
  fetchParentCategories()
})
</script>

<template>
  <v-card
    elevation="4"
    class="pa-0 align-center justify-center text-center mx-auto mb-6"
  >
    <v-card-title> Danh mục </v-card-title>

    <v-card-text class="pa-0">
      <v-list>
        <v-list-item
          density="compact"
          class="text-left"
          link
          :to="{ name: 'category-page', params: { slug: 'tat-ca' } }"
          :active="false"
          title="Tất Cả Sách"
        >
        </v-list-item>

        <v-list-group
          v-for="c in categories"
          :value="c.categoryName"
        >
          <template v-slot:activator="{ props, isOpen }">
            <v-list-item
              class="text-start"
              density="compact"
              :active="false"
            >
              <v-list-item-title @click="reRoute(c.categorySlug)">
                {{ c.categoryName }}
              </v-list-item-title>

              <template v-slot:append>
                <v-icon
                  class="pa-3"
                  v-show="isOpen == false"
                  v-bind="props"
                  icon="mdi-chevron-down"
                ></v-icon>

                <v-icon
                  class="pa-3"
                  v-show="isOpen"
                  v-bind="props"
                  icon="mdi-chevron-up"
                ></v-icon>
              </template>
            </v-list-item>
          </template>

          <v-list-item
            v-for="sub in c.children"
            :title="sub.categoryName"
            :to="{ name: 'category-page', params: { slug: sub.categorySlug } }"
            class="text-start"
            density="compact"
          ></v-list-item>
        </v-list-group>
      </v-list>
    </v-card-text>
  </v-card>
</template>
