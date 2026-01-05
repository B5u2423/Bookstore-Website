<script setup>
import { AdminService } from '@/api/admin-api'
import AdminNavDrawer from '@/components/admin/AdminNavDrawer.vue'
import { computed, onMounted, toRef, ref, shallowRef } from 'vue'

// table
const headers = ref([
  { title: 'Họ và tên', key: 'name', align: 'start' },
  { title: 'Email', key: 'email', align: 'start' },
  { title: 'SDT', key: 'phoneNumber', align: 'start' },
])
const itemsPerPage = ref(10)
const loading = ref(false)
const serverItems = ref([])
const totalItems = ref(0)

async function loadItems({ page, itemsPerPage }) {
  loading.value = true
  try {
    // page on BE start with index 0
    const payload = await AdminService.getAllCustomersPaginated({
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
    item-value="name"
    items-per-page-text="Số lượng"
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
           Thông tin sách
        </v-toolbar-title>

        <v-btn
          class="me-2"
          prepend-icon="mdi-plus"
          rounded="lg"
          text="Thêm sách"
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

  </v-data-table-server>

</template>

