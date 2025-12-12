<script setup>
import { useCartStore } from '@/stores/cart-store'
import { onMounted, ref } from 'vue'
import { PaymentService, OrderService, AddressInfoService } from '@/api/cart-api'
import { useAuthStore } from '@/stores/auth-store'
import { useUserProfileStore } from '@/stores/user-profile-store'
import router from '@/router'

const cartStore = useCartStore()
const authStore = useAuthStore()
const userProfileStore = useUserProfileStore()
const paymentMethod = ref('COD')
const shipping = ref('')

// select box data for input shipping info
const cities = ref([])
const communes = ref([])

const shippingInfo = ref({
  cityId: '',
  communeId: '',
  cityName: '',
  communeName: '',
  street: '',
  amount: cartStore.totalAmount,
  email: userProfileStore.userInfo.email,
  phone: userProfileStore.userInfo.phone,
  firstName: userProfileStore.userInfo.firstName,
  lastName: userProfileStore.userInfo.lastName,
  info: '',
})

async function fetchCities() {
  try {
    const res = await AddressInfoService.getCities()
    cities.value = res
  } catch (error) {
    console.error('Error fetching cities')
  }
}

async function fetchCommunes() {
  try {
    shippingInfo.value.communeId = ''
    const res = await AddressInfoService.getCommunes(shippingInfo.value.cityId)
    communes.value = res
  } catch (error) {
    console.error('Error fetching communes')
  }
}

async function confirmCheckout() {
  // change to payment page
  try {
    // create order in db
    // const orderResponse = await OrderService.createOrder(shippingInfo.value, authStore.accessToken)

    if (paymentMethod.value === 'COD') {
      router.push({ name: 'callback' })
      console.log('ok')
    } else {
      // create payment url
      // const res = await PaymentService.createPaymentPage(
      //   {
      //     amount: cartStore.totalAmount,
      //     info: shippingInfo.info,
      //   },
      //   authStore.accessToken,
      // )
      // // redirect
      // window.location.href = res.paymentUrl
      // cartStore.reset()
    }
  } catch (error) {
    console.error('Error checkout', error)
  }
}

onMounted(() => {
  fetchCities()
})
</script>

<template>

  <v-container class="mb-8 bg-white">

    <v-row>

      <v-col
        cols="12"
        md="9"
      >

        <v-row>

          <v-col
            cols="12"
            md="6"
          >

            <p class="text-h5 text-high-emphasis">Thông tin khách hàng</p>

            <v-divider
              thickness="2"
              class="my-2"
            ></v-divider>

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

            <v-autocomplete
              density="compact"
              hide-details="true"
              v-model="shippingInfo.cityId"
              :items="cities"
              item-title="name"
              item-value="code"
              variant="outlined"
              @update:modelValue="fetchCommunes"
            ></v-autocomplete>

            <div class="text-subtitle-1 text-high-emphasis">Xã phường</div>

            <v-autocomplete
              density="compact"
              hide-details="true"
              v-model="shippingInfo.communeId"
              :items="communes"
              item-title="name"
              item-value="code"
              variant="outlined"
              :disabled="!shippingInfo.cityId"
            ></v-autocomplete>

            <div class="text-subtitle-1 text-high-emphasis">Địa chỉ (số nhà, đường ngõ,...)</div>

            <v-text-field
              variant="outlined"
              v-model="shippingInfo.street"
              density="compact"
              hide-details="true"
            ></v-text-field>

            <div class="text-subtitle-1 text-high-emphasis">Ghi chú (tùy chọn)</div>

            <v-textarea
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

            <v-divider
              thickness="2"
              class="my-2"
            ></v-divider>

            <v-radio-group v-model="shipping">

              <v-radio
                :disabled="!(shippingInfo.amount >= 500000)"
                label="Miễn phí cho đơn trên 500K"
                value="MORE500"
              ></v-radio>

              <template v-if="shippingInfo.cityId === ''"></template>

              <template v-else-if="shippingInfo.cityId === 1">

                <v-radio
                  label="Nội thành Hà Nội (1-3 ngày)"
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

            <v-divider
              thickness="2"
              class="my-2"
            ></v-divider>

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
        class="bg-grey-lighten-3"
        cols="12"
        md="3"
      >
         Cart items here
        <v-divider></v-divider>

        <p>Tạm tính {{ shippingInfo.amount }}</p>

        <p>Phí vận chuyển {{ shippingInfo.amount }}</p>

        <v-divider></v-divider>

        <p>Tổng cộng {{ shippingInfo.amount }}</p>

        <v-divider class="py-2"></v-divider>

        <v-btn
          color="primary"
          @click="confirmCheckout"
        >
           Hoàn tất đơn hàng
        </v-btn>

        <v-btn
          variant="plain"
          :to="{ name: 'cart' }"
          prepend-icon="mdi-chevron-left"
        >
           Quay lại giỏ hàng
        </v-btn>

      </v-col>

    </v-row>

  </v-container>

</template>

