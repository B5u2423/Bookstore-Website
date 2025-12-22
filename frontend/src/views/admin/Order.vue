<script setup>
import { CartService, OrderService } from '@/api/cart-api'
import { useAdminAuthStore } from '@/stores/admin-auth-store'
import { ref } from 'vue'

const adminAuthStore = useAdminAuthStore()

function createNew() {
  return {
    id: null,
    email: '',
    phoneNumber: '',
    firstName: '',
    lastName: '',
    note: '',
    orderStatus: '',
    items: [],
    paymentMethod: '',
    itemsTotal: 0,
    shippingFee: 0,
    orderTotal: 0,
    couponCode: '',
    orderDate: null,
    city: '',
    commune: '',
    street: '',
  }
}

// table
const headers = ref([
  { title: 'ID', key: 'id', align: 'start' },
  { title: 'Họ Và Tên Người Mua', key: 'fullname', align: 'start' },
  { title: 'Email Người Mua', key: 'email', align: 'start' },
  { title: 'Địa Chỉ Giao Hàng', key: 'address', align: 'start' },
  { title: 'Tình Trạng Đơn Hàng', key: 'orderStatus', align: 'start' },
  { title: 'Phương Thức Thanh Toán', key: 'paymentMethod', align: 'start' },
  { title: 'Ngày Đặt Hàng', key: 'orderDate', align: 'start' },
  { title: 'Thao tác', key: 'actions', align: 'end', sortable: false },
])
const itemsPerPage = ref(10)
const loading = ref(false)
const serverItems = ref([])
const totalItems = ref(0)

// order details dialog
const detailDialog = ref(createNew())
const dialog = ref(false)

function showMoreDetails(id) {
  const found = serverItems.value.find((order) => order.id === id)

  detailDialog.value = {
    id: found.id,
    email: found.email,
    phoneNumber: found.phoneNumber,
    firstName: found.firstName,
    lastName: found.lastName,
    note: found.note,
    orderStatus: found.orderStatus,
    items: found.items,
    paymentMethod: found.paymentMethod,
    itemsTotal: found.itemsTotal,
    shippingFee: found.shippingFee,
    orderTotal: found.orderTotal,
    couponCode: found.couponCode,
    orderDate: found.orderDate,
    city: found.city,
    commune: found.commune,
    street: found.street,
  }
}

async function loadItems({ page, itemsPerPage }) {
  loading.value = true
  try {
    // page on BE start with index 0
    const payload = await OrderService.getAllOrdersPaginated(adminAuthStore.accessToken, {
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

    <template v-slot:item.fullname="{ item }">

      <div class="d-flex ga-2 justify-start"
      @click="dialog = !dialog"
      > {{ item.lastName }} {{ item.firstName }} </div>

    </template>

    <template v-slot:item.address="{ item }">

      <div class="d-flex ga-2 justify-start">
         {{ item.street }}, {{ item.commune }}, {{ item.city }}
      </div>

    </template>

    <!-- action buttons -->

    <template v-slot:item.actions="{ item }">

      <div class="d-flex ga-2 justify-end">

        <v-icon
          color="medium-emphasis"
          icon="mdi-information-outline"
          size="small"
          @click="showMoreDetails(item.id)"
        ></v-icon>

      </div>

    </template>

  </v-data-table-server>

  <v-dialog
    v-model="dialog"
    max-width="800"
  >

    <v-card
      title="Thông Tin Đơn Hàng"
    >

      <template v-slot:text>

        <v-row class="px-3">

          <v-col
            cols="12"
            md="6"
          >

            <div class="text-subtitle-1 text-high-emphasis">
               Họ Và Tên Người Mua
            </div>

            <v-text-field variant="outlined"
              :model-value="detailDialog.lastName + ' ' + detailDialog.firstName"
              density="compact"
              hide-details="true"
              :readonly="true"
            ></v-text-field>

          </v-col>
          <v-col
            cols="12"
            md="3"
          >

            <div class="text-subtitle-1 text-high-emphasis">
              Email
            </div>

            <v-text-field variant="outlined"
              :model-value="detailDialog.email"
              density="compact"
              hide-details="true"
              :readonly="true"
            ></v-text-field>

          </v-col>
          <v-col
            cols="12"
            md="3"
          >

            <div class="text-subtitle-1 text-high-emphasis">
              Số Điện Thoại
            </div>

            <v-text-field variant="outlined"
              :model-value="detailDialog.phoneNumber"
              density="compact"
              hide-details="true"
              :readonly="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="6"
          >

            <div class="text-subtitle-1 text-high-emphasis">
              Địa Chỉ Giao Hàng
            </div>

            <v-text-field variant="outlined"
              :model-value="detailDialog.street + ' ' + detailDialog.commune + ' ' + detailDialog.city"
              density="compact"
              hide-details="true"
              :readonly="true"
            ></v-text-field>

          </v-col>


          <v-col
            cols="12"
            md="4"
          >

            <div class="text-subtitle-1 text-high-emphasis">
              Mã giảm giá
            </div>

            <v-text-field variant="outlined"
              :model-value="detailDialog.couponCode"
              density="compact"
              hide-details="true"
              :readonly="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="4"
          >
            <div class="text-subtitle-1 text-high-emphasis">
              Tình Trạng Đơn Hàng
            </div>

            <v-text-field variant="outlined"
              :model-value="detailDialog.orderStatus"
              density="compact"
              hide-details="true"
              :readonly="true"
            ></v-text-field>

          </v-col>
          <v-col
            cols="12"
            md="4"
          >
            <div class="text-subtitle-1 text-high-emphasis">
              Phương Thức Thanh Toán
            </div>

            <v-text-field variant="outlined"
              :model-value="detailDialog.paymentMethod"
              density="compact"
              hide-details="true"
              :readonly="true"
            ></v-text-field>

          </v-col>
        </v-row>

      </template>

      <v-divider></v-divider>

      <v-card-actions class="bg-surface-light">

        <v-btn
          color="red-lighten-1"
          variant="elevated"
        >
           Hủy
        </v-btn>

      </v-card-actions>

    </v-card>

  </v-dialog>
</template>

