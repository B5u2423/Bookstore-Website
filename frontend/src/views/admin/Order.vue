<script setup>
  import { CartService, OrderService } from '@/api/cart-api'
import { useAdminAuthStore } from '@/stores/admin-auth-store'
import { ref } from 'vue'

const adminAuthStore = useAdminAuthStore()
// table
const headers = ref([
  { title: 'ID', key: 'id', align: 'start' },
  { title: 'Tên Người Mua', key: 'firstName', align: 'start' },
  { title: 'Email Người Mua', key: 'email', align: 'start' },
  { title: 'Địa Chỉ Giao Hàng', key: 'address', align: 'start' },
  { title: 'Tình Trạng Đơn Hàng', key: 'orderStatus', align: 'start' },
  { title: 'Phương Thức Thanh Toán', key: 'paymentMethod', align: 'start' },
  { title: 'Ngày Đặt Hàng', key: 'orderDate', align: 'start' },
])
const itemsPerPage = ref(10)
const loading = ref(false)
const serverItems = ref([])
const totalItems = ref(0)

async function loadItems({ page, itemsPerPage }) {
  loading.value = true
  try {
    // page on BE start with index 0
    const payload = await OrderService.getAllOrdersPaginated(adminAuthStore.accessToken, { page: page - 1, size: itemsPerPage })
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
           Thông tin đơn hàng
        </v-toolbar-title>

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

    <template v-slot:item.address="{ item }">

      <div class="d-flex ga-2 justify-start">

        {{ item.street }}, {{ item.commune }}, {{ item.city }}


      </div>

    </template>
  </v-data-table-server>

</template>

