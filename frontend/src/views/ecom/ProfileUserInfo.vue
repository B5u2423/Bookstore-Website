<script setup>
import { useAuthStore } from '@/stores/auth-store'
import { useCartStore } from '@/stores/cart-store'
import { useUserProfileStore } from '@/stores/user-profile-store'
import { cityNames } from '@/utils/province-info'
import { onBeforeMount, onMounted, ref, watch } from 'vue'

const userProfileStore = useUserProfileStore()
const cartStore = useCartStore()
const authStore = useAuthStore()

const isFieldsEnabled = ref(false)
const isUpdated = ref(false)

const dialog = ref(false)

const currentUserProfileSnapshot = ref({
  firstName: userProfileStore.userInfo.firstName,
  lastName: userProfileStore.userInfo.lastName,
  email: userProfileStore.userInfo.email,
  phone: userProfileStore.userInfo.phone,
})

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
  currentUserProfileSnapshot.value.firstName = userProfileStore.userInfo.firstName
  currentUserProfileSnapshot.value.lastName = userProfileStore.userInfo.lastName
  currentUserProfileSnapshot.value.email = userProfileStore.userInfo.email
  currentUserProfileSnapshot.value.phone = userProfileStore.userInfo.phone

  // return to original state
  isUpdated.value = false
  isFieldsEnabled.value = false
}

function updateChanges() {
  userProfileStore.updateUserInfo(currentUserProfileSnapshot.value)
  // return to original state
  isUpdated.value = false
  isFieldsEnabled.value = false
}

// sync cart on load
onMounted(() => {
  cartStore.syncCartWithBackEnd({ token: authStore.accessToken })
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

            <div class="text-subtitle-1 text-medium-emphasis">Họ</div>

            <v-text-field
              variant="outlined"
              density="compact"
              placeholder="Họ"
              :disabled="!isFieldsEnabled"
              v-model="currentUserProfileSnapshot.lastName"
            ></v-text-field>

          </v-col>

          <v-col class="py-0">

            <div class="text-subtitle-1 text-medium-emphasis">Tên</div>

            <v-text-field
              variant="outlined"
              density="compact"
              placeholder="Tên"
              :disabled="!isFieldsEnabled"
              v-model="currentUserProfileSnapshot.firstName"
            ></v-text-field>

          </v-col>

        </v-row>

        <v-row>

          <v-col class="py-0">

            <div class="text-subtitle-1 text-medium-emphasis">Email</div>

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
              v-model="currentUserProfileSnapshot.phone"
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
                @click="updateChanges()"
              >
                 Lưu
              </v-btn>

              <v-btn
                color="warning"
                @click="discardChanges()"
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

      <v-card-text> </v-card-text>

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
              variant="outlined"
              density="compact"
              hide-details="true"
              :items="cityNames"
            >

            </v-autocomplete>

          </v-col>

          <v-col cols="12">

            <div class="text-subtitle-1 text-high-emphasis">Xã phường</div>

            <v-text-field
              variant="outlined"
              density="compact"
              hide-details="true"
            ></v-text-field>

          </v-col>

          <v-col cols="12">

            <div class="text-subtitle-1 text-high-emphasis">
               Địa chỉ (số nhà, đường, thôn, ngõ,...)
            </div>

            <v-text-field
              variant="outlined"
              v-model="titleCaps"
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

</template>

