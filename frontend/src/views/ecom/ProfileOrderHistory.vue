<script setup>
import { OrderService } from '@/api/cart-api'
import { onMounted, ref } from 'vue'
import { formatPriceVNLocale } from '@/utils/utils'

const headers = ref([
  { title: 'Tình Trạng Đơn Hàng', key: 'orderStatus', align: 'start' },
  { title: 'Phương Thức Thanh Toán', key: 'paymentMethod', align: 'start' },
  { title: 'Ngày Đặt Hàng', key: 'orderDate', align: 'start' },
  { width: 1, key: 'data-table-expand', align: 'end' },
])
const items = ref([])

// fetch previous orders
async function fetchOrderHistory() {
  try {
    const res = await OrderService.getAllOrdersByEmail()
    items.value = res.content
  } catch (error) {
    console.error('Error fetching user order history', error)
  }
}

onMounted(() => {
  fetchOrderHistory()
})
</script>

<template>

  <v-sheet class="pa-3 justify-center align-center">

    <v-data-table
      :headers="headers"
      :items="items"
    >

      <template v-slot:item.data-table-expand="{ internalItem, isExpanded, toggleExpand }">

        <v-btn
          :append-icon="isExpanded(internalItem) ? 'mdi-chevron-up' : 'mdi-chevron-down'"
          :text="isExpanded(internalItem) ? 'Thu gọn' : 'Thêm thông tin'"
          class="text-none"
          color="medium-emphasis"
          size="small"
          variant="text"
          width="105"
          slim
          @click="toggleExpand(internalItem)"
        ></v-btn>

      </template>

      <template v-slot:expanded-row="{ columns, item }">

        <tr>

          <td
            :colspan="columns.length"
            class="py-2"
          >

            <v-sheet rounded="lg">

              <v-row class="ma-4">

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
                   {{ item.name }}
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
                   {{ item.street + ' ' + item.commune + ' ' + item.city }}
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
                   {{ formatPriceVNLocale(item.itemsTotal) }} VNĐ
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
                   {{ formatPriceVNLocale(item.shippingFee) }} VNĐ
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
                   {{ formatPriceVNLocale(item.orderTotal) }} VNĐ
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

                  <v-col v-for="i in item.items">

                    <v-card variant="flat">

                      <v-card-title> {{ i.book.title }} </v-card-title>

                      <v-card-subtitle>

                        <p> Giá thành: {{ formatPriceVNLocale(i.priceAtPurchase) }} VNĐ </p>

                        <p> Số lượng: {{ i.quantity }} </p>

                      </v-card-subtitle>

                    </v-card>

                  </v-col>

                </v-col>

              </v-row>

            </v-sheet>

          </td>

        </tr>

      </template>

    </v-data-table>

  </v-sheet>

</template>
