<script setup>
import { CategoryService } from '@/api/category-api'
import { ref, onMounted } from 'vue'

const categories = ref([])

function capitalizeFirstword(sentence) {
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
        >
           Tất Cả Sách
          <template v-slot:append>

            <v-icon
              icon="mdi-menu-right"
              size="small"
            ></v-icon>

          </template>

        </v-list-item>

        <v-list-item
          density="compact"
          class="text-left"
          v-for="(category, i) in categories"
          :to="{ name: 'category-page', params: { slug: category.categorySlug } }"
          link
          :active="false"
          :key="i"
        >

          <v-list-item-title>{{ capitalizeFirstword(category.categoryName) }}</v-list-item-title>

          <template v-slot:append>

            <v-icon
              icon="mdi-menu-right"
              size="small"
            ></v-icon>

          </template>

        </v-list-item>

      </v-list>

      <!-- List group or treeview should be suffice for this -->

    </v-card-text>

  </v-card>

</template>

