<script setup>
import { useCartStore } from '@/stores/cart-store'
import { ref } from 'vue'
import { PaymentService, OrderService } from '@/api/cart-api'
import { useAuthStore } from '@/stores/auth-store'
import { useUserProfileStore } from '@/stores/user-profile-store'

const cartStore = useCartStore()
const authStore = useAuthStore()
const userProfileStore = useUserProfileStore()
const paymentMethod = ref('COD')
const shipping = ref('')

const shippingInfo = ref({
  city: '',
  commune: '',
  street: '',
  amount: cartStore.totalAmount,
  email: userProfileStore.lastName,
  phone: userProfileStore.phone,
  firstName: userProfileStore.firstName,
  lastName: userProfileStore.lastName,
  info: '',
})

async function confirmCheckout() {
  // change to payment page
  try {
    // create order in db
    const orderResponse = await OrderService.createOrder(shippingInfo.value, authStore.accessToken)

    // create payment url
    const res = await PaymentService.createPaymentPage(
      {
        amount: cartStore.totalAmount,
        info: shippingInfo.info,
      },
      authStore.accessToken,
    )
    // redirect
    window.location.href = res.paymentUrl
    cartStore.reset()
  } catch (error) {
    console.error('Error checkout', error)
  }
}
</script>

<template>

  <v-row>

    <v-col
      cols="12"
      md="9"
      class="bg-red"
    >

      <v-row>

        <v-col cols="12">

          <div class="text-subtitle-1 text-high-emphasis">Email</div>

          <!-- Fist name, last name and email get from user profile so no change -->

          <v-text-field
            variant="outlined"
            v-model="shippingInfo.email"
            density="compact"
            hide-details="true"
            :disabled="true"
          ></v-text-field>

          <div class="text-subtitle-1 text-high-emphasis">Họ</div>

          <v-text-field
            variant="outlined"
            v-model="shippingInfo.lastName"
            density="compact"
            hide-details="true"
            :disabled="true"
          ></v-text-field>

          <div class="text-subtitle-1 text-high-emphasis">Tên</div>

          <v-text-field
            variant="outlined"
            v-model="shippingInfo.firstName"
            density="compact"
            hide-details="true"
            :disabled="true"
          ></v-text-field>

          <div class="text-subtitle-1 text-high-emphasis">Số điện thoại</div>

          <v-text-field
            variant="outlined"
            v-model="shippingInfo.phone"
            density="compact"
            hide-details="true"
            :disabled="true"
          ></v-text-field>

          <div class="text-subtitle-1 text-high-emphasis">Tỉnh thành</div>

          <v-text-field
            variant="outlined"
            v-model="shippingInfo.city"
            density="compact"
            hide-details="true"
          ></v-text-field>

          <div class="text-subtitle-1 text-high-emphasis">Xã phường</div>

          <v-text-field
            variant="outlined"
            v-model="shippingInfo.commune"
            density="compact"
            hide-details="true"
          ></v-text-field>

          <div class="text-subtitle-1 text-high-emphasis">Địa chỉ (số nhà, đường ngõ,...)</div>

          <v-text-field
            variant="outlined"
            v-model="shippingInfo.street"
            density="compact"
            hide-details="true"
          ></v-text-field>

          <div class="text-subtitle-1 text-high-emphasis">Ghi chú</div>

          <v-textarea
            label="Ghi chú (tùy chọn)"
            v-model="shippingInfo.info"
            hide-details="true"
            density="compact"
            variant="outlined"
          ></v-textarea>

        </v-col>

        <v-col
          cols="12"
          md="6"
        >

          <div class="text-h5 text-high-emphasis">Vận chuyển</div>

          <p>Selected Button: {{ shipping }}</p>

          <v-radio-group v-model="shipping">

            <v-radio
              label="Miễn phí đơn trên 500K"
              value="MORE500"
            ></v-radio>

            <template v-if="true">

              <v-radio
                label="Hà Nội (1-3 ngày)"
                value="HANOI"
              ></v-radio>

            </template>

            <template v-else>

              <v-radio
                label="Tỉnh thành khác (2-6 ngày)"
                value="OTHERS"
              ></v-radio>

            </template>

          </v-radio-group>

          <div class="text-h5 text-high-emphasis">Thanh toán</div>

          <p>Selected Button: {{ paymentMethod }}</p>

          <v-radio-group v-model="paymentMethod">

            <v-radio
              label="Thanh toán khi giao hàng (Cash On Delivery)"
              value="COD"
            ></v-radio>

            <v-radio
              label="Thanh toán qua VNPAY (QR, Banking)"
              value="ONLINE"
            ></v-radio>

          </v-radio-group>

        </v-col>

      </v-row>

    </v-col>

    <v-col
      cols="12"
      md="3"
      class="bg-blue"
    >
       Cart items here
      <v-divider></v-divider>

      <h2>Tạm tính {{ shippingInfo.amount }}</h2>

      <h2>Phí vận chuyển {{ shippingInfo.amount }}</h2>

      <v-divider></v-divider>

      <h1>Tổng cộng {{ shippingInfo.amount }}</h1>

      <v-btn :to="{ name: 'cart' }">Quay lại giỏ hàng</v-btn>

      <v-btn color="primary">Hoàn tất đơn hàng</v-btn>

    </v-col>

  </v-row>

</template>

