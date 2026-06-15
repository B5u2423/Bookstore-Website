<script setup>
import { CartService, OrderService } from '@/api/cart-api'
import SnackBarOnFailure from '@/components/common/SnackBarOnFailure.vue'
import SnackBarOnSuccess from '@/components/common/SnackBarOnSuccess.vue'
import { useAdminAuthStore } from '@/stores/admin-auth-store'
import { formatPriceVNLocale } from '@/utils/utils'
import { ref, shallowRef } from 'vue'

const adminAuthStore = useAdminAuthStore()

function createNew() {
  return {
    id: null,
    email: '',
    phoneNumber: '',
    name: '',
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
    name: found.name,
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
  dialog.value = !dialog.value
}

async function loadItems({ page = 1, itemsPerPage: size = itemsPerPage.value } = {}) {
  loading.value = true
  try {
    // page on BE start with index 0
    const payload = await OrderService.getAllOrdersPaginated({
      page: page - 1,
      size,
    })
    serverItems.value = payload.content
    totalItems.value = payload.page.totalElements
  } catch (error) {
    console.error('Error loading books from server', error)
  } finally {
    loading.value = false
  }
}

// change order
const currentStatus = ref(null)
function updateToItemStatus(status) {
  currentStatus.value = status
}
async function updateOrderStatus(orderId) {
  try {
    const res = await OrderService.updateStatusById({
      orderId,
      status: currentStatus.value,
    })
    // success snack
    isSuccess.value = true
    message.value = 'Cập nhật trạng thái đơn hàng thành công'
    // reload
    await loadItems()
  } catch (error) {
    // error snack
    isError.value = true
    message.value = 'Cập nhật trạng thái đơn hàng thất bại'
    console.log('Error update order status by Id', error)
  } finally {
    setTimeout(() => {
      isSuccess.value = false
      isError.value = false
    }, 2000)
  }
}

function computeOrderColor(status) {
  const colorMap = {
    PENDING: 'cyan',
    PAID: 'success',
    CANCELLED: 'red',
  }
  return colorMap[String(status)]
}

// snackbars
const isError = ref(false)
const isSuccess = ref(false)
const message = ref('')
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

    <!-- order: user fullname -->

    <template v-slot:item.fullname="{ item }">
      <div class="d-flex ga-2 justify-start">{{ item.name }}</div>
    </template>

    <!-- order: user's address -->

    <template v-slot:item.address="{ item }">
      <div class="d-flex ga-2 justify-start">
        {{ item.street }}, {{ item.commune }}, {{ item.city }}
      </div>
    </template>

    <!-- order: status -->

    <template v-slot:item.orderStatus="{ item }">
      <div class="d-flex ga-2 justify-start">
        <v-dialog max-width="500">
          <template v-slot:activator="{ props: activatorProps }">
            <v-chip
              v-bind="activatorProps"
              :color="computeOrderColor(item.orderStatus)"
              @click="updateToItemStatus(item.orderStatus)"
            >
              {{ item.orderStatus }}
            </v-chip>
          </template>

          <template v-slot:default="{ isActive }">
            <v-card title="Thay đổi trạng thái đơn">
              <v-card-text>
                <v-select
                  :items="['PENDING', 'PAID', 'CANCELLED']"
                  v-model="currentStatus"
                ></v-select>
              </v-card-text>

              <template v-slot:actions>
                <v-btn
                  @click="() => {
                    updateOrderStatus(item.id)
                    isActive.value = false
                  }"
                >
                  Lưu
                </v-btn>

                <v-btn @click="isActive.value = false">Hủy</v-btn>
              </template>
            </v-card>
          </template>
        </v-dialog>
      </div>
    </template>

    <!-- action buttons -->

    <template v-slot:item.actions="{ item }">
      <div class="d-flex ga-2">
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
    <v-card title="Thông Tin Đơn Hàng">
      <template v-slot:text>
        <v-row
          class="px-3"
          no-gutters
        >
          <v-col
            cols="12"
            md="4"
            class="border-md pa-2"
          >
            <b> Họ và tên người mua </b>
          </v-col>

          <v-col
            cols="12"
            md="8"
            class="border-md pa-2"
          >
            {{ detailDialog.name }}
          </v-col>

          <v-col
            cols="12"
            md="4"
            class="border-md pa-2"
          >
            <b> Số điện thoại </b>
          </v-col>

          <v-col
            cols="12"
            md="8"
            class="border-md pa-2"
          >
            {{ detailDialog.phoneNumber }}
          </v-col>

          <v-col
            cols="12"
            md="4"
            class="border-md pa-2"
          >
            <b> Email </b>
          </v-col>

          <v-col
            cols="12"
            md="8"
            class="border-md pa-2"
          >
            {{ detailDialog.email }}
          </v-col>

          <v-col
            cols="12"
            md="4"
            class="border-md pa-2"
          >
            <b> Địa chỉ giao hàng </b>
          </v-col>

          <v-col
            cols="12"
            md="8"
            class="border-md pa-2"
          >
            {{ detailDialog.street + ' ' + detailDialog.commune + ' ' + detailDialog.city }}
          </v-col>

          <v-col
            cols="12"
            md="4"
            class="border-md pa-2"
          >
            <b> Mã giảm giá </b>
          </v-col>

          <v-col
            cols="12"
            md="8"
            class="border-md pa-2"
          >
            <template v-if="detailDialog.couponCode === null">N/A</template>

            <template v-else> {{ detailDialog.couponCode }} </template>
          </v-col>

          <v-col
            cols="12"
            md="4"
            class="border-md pa-2"
          >
            <b> Tình trạng đơn hàng </b>
          </v-col>

          <v-col
            cols="12"
            md="8"
            class="border-md pa-2"
          >
            {{ detailDialog.orderStatus }}
          </v-col>

          <v-col
            cols="12"
            md="4"
            class="border-md pa-2"
          >
            <b> Ngày đặt hàng </b>
          </v-col>

          <v-col
            cols="12"
            md="8"
            class="border-md pa-2"
          >
            {{ detailDialog.orderDate }}
          </v-col>

          <v-col
            cols="12"
            md="4"
            class="border-md pa-2"
          >
            <b> Phương thức thanh toán </b>
          </v-col>

          <v-col
            cols="12"
            md="8"
            class="border-md pa-2"
          >
            {{ detailDialog.paymentMethod }}
          </v-col>

          <v-col
            cols="12"
            md="4"
            class="border-md pa-2"
          >
            <b>Tổng giá tiền sản phẩm</b>
          </v-col>

          <v-col
            cols="12"
            md="8"
            class="border-md pa-2"
          >
            {{ formatPriceVNLocale(detailDialog.itemsTotal) }} VNĐ
          </v-col>

          <v-col
            cols="12"
            md="4"
            class="border-md pa-2"
          >
            <b>Chi phí giao hàng</b>
          </v-col>

          <v-col
            cols="12"
            md="8"
            class="border-md pa-2"
          >
            {{ formatPriceVNLocale(detailDialog.shippingFee) }} VNĐ
          </v-col>

          <v-col
            cols="12"
            md="4"
            class="border-md pa-2"
          >
            <b>Tổng giá tiền đơn hàng</b>
          </v-col>

          <v-col
            cols="12"
            md="8"
            class="border-md pa-2"
          >
            {{ formatPriceVNLocale(detailDialog.orderTotal) }} VNĐ
          </v-col>

          <v-col
            cols="12"
            md="4"
            class="border-md pa-2"
          >
            <b>Sản phẩm</b>
          </v-col>

          <v-col
            cols="12"
            md="8"
            class="border-md pa-2"
          >
            <v-col v-for="item in detailDialog.items">
              <v-card variant="flat">
                <v-card-title>
                  {{ item.titleAtPurchase }}
                  <v-tooltip
                    activator="parent"
                    location="top"
                    :text="item.titleAtPurchase"
                  />
                </v-card-title>

                <v-card-subtitle>
                  <p>Giá thành: {{ formatPriceVNLocale(item.priceAtPurchase) }} VNĐ</p>

                  <p>Số lượng: {{ item.quantity }}</p>
                </v-card-subtitle>
              </v-card>
            </v-col>
          </v-col>
        </v-row>
      </template>

      <v-divider></v-divider>

      <v-card-actions class="bg-surface-light">
        <v-btn
          color="red-lighten-1"
          variant="elevated"
          @click="dialog = !dialog"
        >
          Hủy
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>

  <!-- snack bars -->

  <snack-bar-on-failure
    :show="isError"
    :message="message"
  />

  <snack-bar-on-success
    :show="isSuccess"
    :message="message"
  />
</template>
