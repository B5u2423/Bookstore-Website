<script setup>
import { BookService } from '@/api/book-api'
import { formatPriceVNLocale } from '@/utils/utils'
import { onMounted, ref } from 'vue'
const headers = ref([
  { title: 'Tên sách', key: 'title', align: 'start' },
  { title: 'Mã ISBN', key: 'isbn', align: 'start' },
  { title: 'Tác giả', key: 'author', align: 'start' },
  { title: 'Nhà xuất bản', key: 'publisher', align: 'start' },
  { title: 'Mã sản phẩm', key: 'productCode', align: 'start' },
  { title: 'Giá sản phẩm (₫)', key: 'price', align: 'start' },
  { title: 'Số lượng trong kho', key: 'inStock', align: 'start' },
  { title: 'Thao tác', key: 'actions', align: 'end', sortable: false },
])
const itemsPerPage = ref(10)
const loading = ref(false)
const serverItems = ref([])
const totalItems = ref(0)

async function loadItems({page, itemsPerPage }) {
  loading.value = true
  try {
    // page on BE start with index 0
    const payload = await BookService.fetchAllBooks({ page: page - 1, size: itemsPerPage })
    serverItems.value = payload.content
    totalItems.value = payload.page.totalElements
  } catch (error) {
    console.error('Error loading books from server', error)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadItems()
})
</script>

<template>

  <v-data-table-server
    v-model:items-per-page="itemsPerPage"
    :headers="headers"
    :items="serverItems"
    :items-length="totalItems"
    :loading="loading"
    item-value="title"
    items-per-page-text="Số sản phẩm hiển thị"
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
           Popular books
        </v-toolbar-title>

        <v-btn
          class="me-2"
          prepend-icon="mdi-plus"
          rounded="lg"
          text="Thêm sách"
          variant="outlined"
        ></v-btn>

      </v-toolbar>

    </template>

    <!-- stylized book title as chips -->

    <template v-slot:item.title="{ value }">

      <v-chip
        :text="value"
        class="border-thin"
        prepend-icon="mdi-book"
        label
      >

        <template v-slot:prepend>

          <v-icon color="medium-emphasis"></v-icon>

        </template>

      </v-chip>

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

    <!-- format price-->

    <template v-slot:item.price="{ value }"> {{ formatPriceVNLocale(value) }} </template>

  </v-data-table-server>

</template>