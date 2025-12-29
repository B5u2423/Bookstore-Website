<script setup>
import { CustomerService, UserService } from '@/api/customer-api'
import { useAuthStore } from '@/stores/auth-store'
import { useCartStore } from '@/stores/cart-store'
import { useUserProfileStore } from '@/stores/user-profile-store'
import { onBeforeMount, onMounted, ref, watch } from 'vue'
import { AddressInfoService } from '@/api/cart-api.js'
import SnackBarOnFailure from '@/components/common/SnackBarOnFailure.vue'
import SnackBarOnSuccess from '@/components/common/SnackBarOnSuccess.vue'

const userProfileStore = useUserProfileStore()
const cartStore = useCartStore()
const authStore = useAuthStore()

const isFieldsEnabled = ref(false)
const isUpdated = ref(false)
const cities = ref([])
const communes = ref([])
const address = ref({
  cityId: '',
  cityName: '',
  communeId: '',
  communeName: '',
  street: '',
})

const dialog = ref(false)
// snackbars
const isError = ref(false)
const isSuccess = ref(false)
const message = ref('')

const currentUserProfileSnapshot = ref({
  name: userProfileStore.userInfo.name,
  email: userProfileStore.userInfo.email,
  phoneNumber: userProfileStore.userInfo.phone,
})

// address data table headers
const headers = ref(
  { title: 'Tỉnh thành', key: 'city', align: 'start' },
  { title: 'Xã phường', key: 'commune', align: 'start' },
  { title: 'Địa chỉ cụ thể (số nhà, đường, ngõ,...)', key: 'street', align: 'start' },
)

watch(
  currentUserProfileSnapshot,
  () => {
    isUpdated.value = true
  },
  { deep: true },
)

function enableFieldsForUpdate() {
  isFieldsEnabled.value = true
}

function discardChanges() {
  // restore snapshot
  currentUserProfileSnapshot.value.name= userProfileStore.userInfo.name
  currentUserProfileSnapshot.value.email = userProfileStore.userInfo.email
  currentUserProfileSnapshot.value.phoneNumber = userProfileStore.userInfo.phone

  // return to original state
  isUpdated.value = false
  isFieldsEnabled.value = false
}

async function updateChanges() {
  try {
    // API call
    const res = await UserService.updateUserProfile(
      authStore.accessToken,
      currentUserProfileSnapshot.value,
    )
    // update immediate view
    userProfileStore.updateUserInfo(currentUserProfileSnapshot.value)
    isSuccess.value = true
    message.value = 'Cập nhật thông tin người dùng thành công'
    return res
  } catch (error) {
    console.error('Error updating user info', error)
    isError.value = true
    message.value = 'Lỗi xảy ra khi cập nhật thông tin người dùng'
    throw error
  } finally {
    // return to original state
    isUpdated.value = false
    isFieldsEnabled.value = false
  }
}

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
    address.value.communeId = ''
    const res = await AddressInfoService.getCommunes(address.value.cityId)
    communes.value = res
  } catch (error) {
    console.error('Error fetching communes')
  }
}

// add address
async function save() {
  try {
    const res = CustomerService.setAddress(
      {
        city: cities.value.find((obj) => obj.code === address.value.cityId)?.name,
        commune: communes.value.find((obj) => obj.code === address.value.communeId)?.name,
        street: address.value.street,
      },
      authStore.accessToken,
    )
    isSuccess.value = true
    message.value = 'Thêm địa chỉ thành công! Vui lòng tải lại trang'
  } catch (error) {
    isError.value = true
    message.value = 'Lỗi xảy khi thêm địa chỉ'
    console.error('Error adding new address')
  } finally {
    // close dialog box
    dialog.value = false
  }
}

// sync cart on load
onMounted(() => {
  cartStore.syncCartWithBackEnd({ token: authStore.accessToken })
  fetchCities()
})
</script>

<template>

  <v-sheet class="pa-3 justify-center align-center mb-6">

    <v-card
      variant="flat"
      class="pa-2"
    >

      <v-card-title><h4>Thông tin cá nhân</h4></v-card-title>

      <v-divider></v-divider>

      <v-container>

        <v-row>

          <v-col class="py-0">

            <div class="text-subtitle-1 text-medium-emphasis">
               Họ và tên
              <span class="text-red">*</span>

            </div>

            <v-text-field
              variant="outlined"
              density="compact"
              placeholder="Tên"
              :disabled="!isFieldsEnabled"
              v-model="currentUserProfileSnapshot.name"
            ></v-text-field>

          </v-col>

        </v-row>

        <v-row>

          <v-col class="py-0">

            <div class="text-subtitle-1 text-medium-emphasis">
               Email
              <span class="text-red">*</span>

            </div>

            <v-text-field
              variant="outlined"
              density="compact"
              placeholder="Email"
              :disabled="!isFieldsEnabled"
              v-model="currentUserProfileSnapshot.email"
            ></v-text-field>

          </v-col>

          <v-col class="py-0">

            <div class="text-subtitle-1 text-medium-emphasis">Số điện thoại</div>

            <v-text-field
              variant="outlined"
              density="compact"
              placeholder="Số điện thoại"
              :disabled="!isFieldsEnabled"
              v-model="currentUserProfileSnapshot.phoneNumber"
            ></v-text-field>

          </v-col>

        </v-row>

        <v-row>

          <v-col class="py-0">

            <v-btn
              class="my-3"
              color="primary"
              :disabled="isFieldsEnabled"
              @click="enableFieldsForUpdate()"
            >
               Thay đổi thông tin
            </v-btn>

          </v-col>

          <v-col>

            <div v-show="isFieldsEnabled">

              <v-btn
                :disabled="!isUpdated"
                class="mr-3"
                color="success"
                @click="updateChanges"
              >
                 Lưu
              </v-btn>

              <v-btn
                color="warning"
                @click="discardChanges"
              >
                 Hủy
              </v-btn>

            </div>

          </v-col>

        </v-row>

      </v-container>

    </v-card>

    <v-card
      variant="flat"
      class="pa-2"
    >

      <v-card-title class="d-inline-block">

        <h4>Địa chỉ</h4>

        <v-btn
          class="my-3"
          prepend-icon="mdi-plus"
          color="primary"
          @click="dialog = !dialog"
        >
           Thêm địa chỉ
        </v-btn>

      </v-card-title>

      <v-divider></v-divider>

      <v-card-text>

        <template v-if="userProfileStore.userInfo.addressList.length === 0">
           Không có địa chỉ mới
        </template>

        <template v-else>

          <v-data-table
            :headers="headers"
            :items="userProfileStore.userInfo.addressList"
            hide-default-footer
          >

          </v-data-table>

        </template>

      </v-card-text>

    </v-card>

  </v-sheet>

  <v-dialog
    v-model="dialog"
    max-width="500"
  >

    <v-card
      title="Tạo địa chỉ"
      subtitle="Thêm mới địa chỉ giao hàng"
    >

      <v-card-text>

        <v-row class="px-3">

          <v-col cols="12">

            <div class="text-subtitle-1 text-high-emphasis">Tỉnh thành</div>

            <v-autocomplete
              density="compact"
              hide-details="true"
              v-model="address.cityId"
              :items="cities"
              item-title="name"
              item-value="code"
              variant="outlined"
              @update:modelValue="fetchCommunes"
            ></v-autocomplete>

          </v-col>

          <v-col cols="12">

            <div class="text-subtitle-1 text-high-emphasis">Xã phường</div>

            <v-autocomplete
              density="compact"
              hide-details="true"
              v-model="address.communeId"
              :items="communes"
              item-title="name"
              item-value="code"
              variant="outlined"
              :disabled="!address.cityId"
            ></v-autocomplete>

          </v-col>

          <v-col cols="12">

            <div class="text-subtitle-1 text-high-emphasis">
               Địa chỉ (số nhà, đường, thôn, ngõ,...)
            </div>

            <v-text-field
              variant="outlined"
              v-model="address.street"
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
          @click="save"
        >
           Lưu
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

  <SnackBarOnFailure
    :show="isError"
    :mesasge="message"
  />

  <SnackBarOnSuccess
    :show="isSuccess"
    :message="message"
  />

</template>

