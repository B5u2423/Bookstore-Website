<script setup>
import { CategoryService } from '@/api/category-api'
import { ref } from 'vue'
// table
const headers = ref([
  { title: 'ID', key: 'id', align: 'start' },
  { title: 'Tên danh mục', key: 'categoryName', align: 'start' },
  { title: 'Danh mục cha', key: 'parent', align: 'start' },
  { title: 'Các danh mục con', key: 'children', align: 'start' },
  { title: 'Thao tác', key: 'actions', align: 'end', sortable: false },
])
const itemsPerPage = ref(10)
const loading = ref(false)
const serverItems = ref([])
const totalItems = ref(0)

async function loadItems({ page, itemsPerPage }) {
  loading.value = true
  try {
    // page on BE start with index 0
    const payload = await CategoryService.fetchAllCategoriesPaginated({
      page: page - 1,
      size: itemsPerPage,
    })
    serverItems.value = payload.content
    totalItems.value = payload.page.totalElements
  } catch (error) {
    console.error('Error loading books from server', error)
  } finally {
    loading.value = false
  }
}
</script>

<template>

  <v-data-table-server
    v-model:items-per-page="itemsPerPage"
    :headers="headers"
    :items="serverItems"
    :items-length="totalItems"
    :loading="loading"
    item-value="title"
    items-per-page-text="Số danh mục hiển thị"
    @update:options="loadItems"
  >

    <template v-slot:top>

      <v-toolbar flat>

        <v-toolbar-title>

          <v-icon
            color="medium-emphasis"
            icon="mdi-book-multiple"
            size="x-small"
            start
          ></v-icon>
           Thông tin danh mục
        </v-toolbar-title>

        <v-btn
          class="me-2"
          prepend-icon="mdi-plus"
          rounded="lg"
          text="Thêm danh mục"
          variant="outlined"
          @click="add"
        ></v-btn>

      </v-toolbar>

    </template>

    <!-- style the header -->

    <template v-slot:headers="{ columns }">

      <tr>

        <template
          v-for="column in columns"
          :key="column.key"
        >

          <th>

            <div class="d-flex align-center">

              <span
                class="me-2 cursor-pointer font-weight-bold"
                v-text="column.title"
              ></span>

            </div>

          </th>

        </template>

      </tr>

    </template>

    <!-- action buttons -->

    <template v-slot:item.actions="{ item }">

      <div class="d-flex ga-2 justify-end">

        <v-icon
          color="medium-emphasis"
          icon="mdi-pencil"
          size="small"
        ></v-icon>

        <v-icon
          color="medium-emphasis"
          icon="mdi-delete"
          size="small"
        ></v-icon>

      </div>

    </template>

    <!-- parent category -->

    <template v-slot:item.parent="{ item }">

      <template v-if="item.parent == null">

        <v-chip
          color="red-lighten-1"
          variant="outlined"
        >
           Không có
        </v-chip>

      </template>

      <template v-else>

        <v-chip> ID: {{ item.parent }} - {{ item.parentName }} </v-chip>

      </template>

    </template>

    <!-- children categories -->

    <template v-slot:item.children="{ item }">

      <template v-if="!item.children.length">

        <v-chip
          color="red-lighten-1"
          variant="outlined"
        >
           Không có
        </v-chip>

      </template>

      <template v-else>

        <v-chip
          v-for="child in item.children"
          class="ma-1"
          color="green-darken-1"
        >
           {{ child.categoryName }}
        </v-chip>

      </template>

    </template>

  </v-data-table-server>

</template>

