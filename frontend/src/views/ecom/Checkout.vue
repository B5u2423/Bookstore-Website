<script setup>
import { useCartStore } from '@/stores/cart-store'
import { computed, onMounted, ref } from 'vue'
import { PaymentService, OrderService, AddressInfoService } from '@/api/cart-api'
import { useAuthStore } from '@/stores/auth-store'
import { useUserProfileStore } from '@/stores/user-profile-store'
import router from '@/router'
import HorizontalBookCard from '@/components/books/HorizontalBookCard.vue'
import { formatPriceVNLocale } from '@/utils/utils'

function bootStrapValues() {
  return {
    cityId: null,
    communeId: null,
    cityName: '',
    communeName: '',
    street: '',
    itemsTotal: cartStore.totalAmount,
    shippingFee: 0,
    orderTotal: 0,
    couponCode: '',
    email: userProfileStore.userInfo.email,
    phone: userProfileStore.userInfo.phone,
    name: userProfileStore.userInfo.name,
    paymentMethod: 'COD',
    info: '',
  }
}

const cartStore = useCartStore()
const authStore = useAuthStore()
const userProfileStore = useUserProfileStore()
const shipping = ref('')

const rules = {
  required: (v) => !!v || 'Không được bỏ trống trường',
  phone: (v) =>
    /^\d{10,11}$/.test(v) ||
    'Số điện thoại không được chứa chữ cái hay kí tự, độ dài phải nhỏ hơn 11',
}
// select box data for input shipping info
const cities = ref([])
const communes = ref([])

const shippingInfo = ref(bootStrapValues())

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
    // mapping cityId and communeId
    const selectedCity = cities.value.find((obj) => obj?.code === shippingInfo.value.cityId)
    shippingInfo.value.cityName = selectedCity.name
    const selectedCommune = communes.value.find((obj) => obj?.code === shippingInfo.value.communeId)
    shippingInfo.value.communeName = selectedCommune.name
    // update total order value
    shippingInfo.value.orderTotal = tmpOrderTotal

    // create order in db
    const orderResponse = await OrderService.createOrder(shippingInfo.value, authStore.accessToken)

    if (shippingInfo.value.paymentMethod === 'COD') {
      cartStore.reset()
      router.push('/')
    } else {
      // create payment url
      const res = await PaymentService.createPaymentPage(
        {
          amount: cartStore.totalAmount,
          info: shippingInfo.value.info,
        },
        authStore.accessToken,
      )
      // redirect
      window.location.href = res?.paymentUrl
      // call back to page after
      cartStore.reset()
    }
  } catch (error) {
    console.error('Error checkout', error)
  }
}

const isPhoneValid = computed(() => {
  return /^\d{10,11}$/.test(shippingInfo.value.phone)
})

const isShippingValid = computed(() => {
  return (
    isPhoneValid.value &&
    !!shippingInfo.value.communeId &&
    !!shippingInfo.value.cityId &&
    !!shippingInfo.value.street &&
    !!shippingInfo.value.phone &&
    !!shipping.value
  )
})

const tmpOrderTotal = computed(() => {
  return shippingInfo.value.shippingFee + shippingInfo.value.itemsTotal
})

onMounted(() => {
  fetchCities()
})
</script>

<template>

  <v-container class="mb-8 bg-white">

    <v-row>

      <v-col
        cols="12"
        md="7"
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

            <!-- Full name and email get from user profile so no change -->

            <v-text-field
              variant="outlined"
              v-model="shippingInfo.email"
              density="compact"
              hide-details="true"
              :disabled="true"
            ></v-text-field>

            <div class="text-subtitle-1 text-high-emphasis">Tên</div>

            <v-text-field
              variant="outlined"
              v-model="shippingInfo.name"
              density="compact"
              hide-details="true"
              :disabled="true"
            ></v-text-field>

            <div class="text-subtitle-1 text-high-emphasis">
               Số điện thoại
              <span class="text-red">*</span>

            </div>

            <v-text-field
              variant="outlined"
              v-model="shippingInfo.phone"
              density="compact"
              :rules="[rules.required, rules.phone]"
              :disabled="
                userProfileStore.userInfo.phone !== '' && userProfileStore.userInfo.phone !== null
              "
            ></v-text-field>

            <div class="text-subtitle-1 text-high-emphasis">
               Tỉnh thành
              <span class="text-red">*</span>

            </div>

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

            <div class="text-subtitle-1 text-high-emphasis">
               Xã phường
              <span class="text-red">*</span>

            </div>

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

            <div class="text-subtitle-1 text-high-emphasis">
               Địa chỉ (số nhà, đường ngõ,...)
              <span class="text-red">*</span>

            </div>

            <v-text-field
              variant="outlined"
              v-model="shippingInfo.street"
              density="compact"
              :rules="[rules.required]"
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
                :disabled="!(shippingInfo.itemsTotal >= 500000)"
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

            <v-radio-group v-model="shippingInfo.paymentMethod">

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

      <!-- Third col -->

      <v-col
        class="bg-grey-lighten-3"
        cols="12"
        md="5"
      >

        <v-row>

          <v-col cols="12">

            <v-infinite-scroll height="300">

              <div>

                <template
                  v-for="item in cartStore.activeCart"
                  :key="item"
                >

                  <HorizontalBookCard :product="item" />

                </template>

              </div>

              <template v-slot:loading></template>

            </v-infinite-scroll>

          </v-col>

          <v-col cols="12">

            <v-row class="mb-3">

              <v-col md="8">

                <v-text-field
                  variant="outlined"
                  v-model="shippingInfo.couponCode"
                  density="compact"
                  hide-details="true"
                  label="Mã giảm giá"
                ></v-text-field>

              </v-col>

              <v-col
                md="4"
                class="text-end justify-end"
              >

                <v-btn
                  width="100%"
                  color="primary"
                  @click="confirmCheckout"
                >
                   ÁP DỤNG
                </v-btn>

              </v-col>

            </v-row>

            <v-divider></v-divider>

            <v-row>

              <v-col md="6">

                <p>Tạm tính</p>

              </v-col>

              <v-col
                md="6"
                class="text-end"
              >
                 {{ formatPriceVNLocale(shippingInfo.itemsTotal) }} VND
              </v-col>

            </v-row>

            <v-row>

              <v-col md="6">

                <p>Phí vận chuyển</p>

              </v-col>

              <v-col
                md="6"
                class="text-end"
              >

                <template v-if="shippingInfo.shippingFee > 0">
                   {{ formatPriceVNLocale(shippingInfo.value.shippingFee) }}
                </template>

                <template v-else>-</template>

              </v-col>

            </v-row>

            <v-row>

              <v-col md="6">

                <p>TỔNG CỘNG</p>

              </v-col>

              <v-col
                md="6"
                class="text-end"
              >
                 {{ formatPriceVNLocale(tmpOrderTotal) }} VND
              </v-col>

            </v-row>

            <v-divider class="py-2"></v-divider>

            <v-row class="mb-9 pt-3">

              <v-btn
                class="mt-3"
                variant="plain"
                :to="{ name: 'cart' }"
                prepend-icon="mdi-chevron-left"
                density="compact"
              >
                 Quay lại giỏ hàng
              </v-btn>

              <v-spacer></v-spacer>

              <v-btn
                color="primary"
                @click="confirmCheckout"
                class="mr-3"
                :disabled="!isShippingValid"
              >
                 Hoàn tất đơn hàng
              </v-btn>

            </v-row>

          </v-col>

        </v-row>

      </v-col>

    </v-row>

  </v-container>

</template>

