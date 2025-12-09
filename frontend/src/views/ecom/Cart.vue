<script setup>
import { OrderService, PaymentService } from '@/api/cart-api'
import { useAuthStore } from '@/stores/auth-store'
import { useCartStore } from '@/stores/cart-store'
import { formatPriceVNLocale } from '@/utils/utils'
import { shallowRef, ref } from 'vue'
import { useRouter } from 'vuetify/lib/composables/router'

const cartStore = useCartStore()
const authStore = useAuthStore()
const router = useRouter()

const shippingInfo = ref(
  {
    city: '',
    commune: '',
    street: '',
    amount: cartStore.totalAmount
  }
)
const dialog = shallowRef(false)

// Methods

function continueShopping() {
  router.push('/')
}

async function checkout() {
  // if not logged in
  if (!authStore.isAuthenticated) {
    router.push({ name: 'login' })
  } else {
    dialog.value = !dialog.value
  }
}

async function confirmCheckout() {
  // change to payment page
  try {
    // create order in db
    const orderResponse = await OrderService.createOrder(shippingInfo.value, authStore.accessToken)
    
    // create payment url
  const res = await PaymentService.createPaymentPage({
    amount: cartStore.totalAmount,
    info: "hello"
  }, authStore.accessToken);
  // redirect 
  window.location.href = res.paymentUrl
  cartStore.reset()
  } catch (error) {
    console.error('Error checkout', error)
  }
}
</script>

<template>

  <v-container class="mb-8">

    <v-row>

      <h2 class="font-weight-medium">Giỏ hàng của bạn</h2>

    </v-row>

    <v-container class="bg-white border mt-8">

      <div
        v-if="cartStore.cartItemsCount === 0"
        class="text-center pa-4"
      >
         Không có sản phẩm nào trong giỏ hàng. Quay lại cửa hàng để tiếp tục mua sắm.
      </div>

      <template v-else>

        <v-table>

          <thead>

            <tr>

              <th class="text-left font-weight-bold"></th>

              <th class="text-left font-weight-bold">Sản phẩm</th>

              <th class="text-left font-weight-bold">Giá</th>

              <th class="text-left font-weight-bold">Số lượng</th>

              <th class="text-left font-weight-bold">Thành tiền</th>

            </tr>

          </thead>

          <tbody>

            <tr
              v-for="item in cartStore.activeCart"
              :key="item.id"
            >

              <td width="5">

                <v-btn
                  class="mx-3"
                  icon="mdi-close"
                  size="small"
                  @click="cartStore.removeItemFromCart({ itemId: item.id })"
                  variant="flat"
                ></v-btn>

              </td>

              <td>

                <div class="d-flex align-center">

                  <v-img
                    :src="item.image"
                    max-width="100"
                    class="ma-2"
                  ></v-img>

                  <span class="ml-3">{{ item.title }}</span>

                </div>

              </td>

              <td>{{ formatPriceVNLocale(item.price) }}₫</td>

              <td>

                <v-text-field
                  v-model.number="item.quantity"
                  type="number"
                  density="compact"
                  hide-details
                  style="max-width: 100px"
                  min="1"
                ></v-text-field>

              </td>

              <td>{{ formatPriceVNLocale(item.price * item.quantity) }}₫</td>

            </tr>

          </tbody>

        </v-table>

        <v-row class="mt-6">

          <v-col
            cols="12"
            class="d-flex justify-end align-center"
          >

            <span class="text-h6 mr-4">Tổng số thành tiền:</span>

            <span class="text-h6 text-primary">
               {{ formatPriceVNLocale(cartStore.totalAmount) }}₫
            </span>

          </v-col>

        </v-row>

        <v-row>

          <v-col
            cols="12"
            class="d-flex justify-end"
          >

            <v-btn
              color="grey-lighten-1"
              class="mr-4"
              @click="continueShopping"
            >
               Tiếp tục mua hàng
            </v-btn>

            <v-btn
              color="primary"
              @click="checkout"
            >
               Tiến hành thanh toán
            </v-btn>

          </v-col>

        </v-row>

      </template>

    </v-container>

  </v-container>

  <v-dialog
    v-model="dialog"
    max-width="800"
  >

    <v-card
      title="Chỉnh sửa thông tin đơn hàng"
      subtitle="Cập nhật địa chỉ"
    >

      <v-card-text>

        <v-row>

          <v-col
            cols="12"
            md="6"
          >

            <div class="text-subtitle-1 text-high-emphasis">Tỉnh thành</div>

            <v-text-field
              variant="outlined"
              v-model="shippingInfo.city"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="6"
          >

            <div class="text-subtitle-1 text-high-emphasis">Xã phường</div>

            <v-text-field
              variant="outlined"
              v-model="shippingInfo.commune"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="6"
          >

            <div class="text-subtitle-1 text-high-emphasis">Địa chỉ (số nhà, ngõ, phố,...)</div>

            <v-text-field
              variant="outlined"
              v-model="shippingInfo.street"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col
            cols="12"
            md="6"
          >

            <div class="text-subtitle-1 text-high-emphasis">Tổng giá trị đơn hàng</div>

            <v-text-field
              variant="outlined"
              v-model="shippingInfo.amount"
              :disabled="true"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

        </v-row>

      </v-card-text>

      <v-card-actions>

        <v-btn
          color="green-darken-1"
          variant="elevated"
          @click="confirmCheckout"
        >
           Đồng ý thanh toán
        </v-btn>

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

</template>

