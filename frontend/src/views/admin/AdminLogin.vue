<script setup>
import SnackBar from '@/components/common/SnackBar.vue'
import { useAdminAuthStore } from '@/stores/admin-auth-store'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const adminAuthStore = useAdminAuthStore()
const router = useRouter()

const email = ref('')
const password = ref('')
const isLoading = ref(false)

const snackbar = ref({
  show: false,
  message: '',
  color: 'success',
})

async function handleLogin() {
  isLoading.value = true
  const result = await adminAuthStore.adminLogin({
    email: email.value,
    password: password.value,
  })

  // handle response
  if (result.success) {
    snackbar.value = {
      show: true,
      message: 'Đăng nhập thành công!',
      color: 'success',
    }
    setTimeout(() => {
      router.push({ name: 'admin-dashboard' })
      isLoading.value = false
    }, 1500)
  } else {
    isLoading.value = false
    snackbar.value = {
      show: true,
      message: result.error || 'Đăng nhập thất bại',
      color: 'error',
    }
  }
}
</script>

<template>

  <v-container
    class="pa-0 ma-0 fill-height d-flex"
    fluid
  >

    <v-row class="fill-height">

      <v-col
        cols="12"
        md="6"
        class="bg-grey d-flex justify-center"
      >

        <v-img src="/panel.svg"></v-img>

      </v-col>

      <v-col
        cols="12"
        md="6"
        class="d-flex justify-center align-center"
      >

        <v-card
          class="pa-5"
          max-height="600px"
          min-width="400px"
        >

          <v-card-title class="text-center">

            <span class="headline">Đăng nhập</span>

          </v-card-title>

          <v-card-text>

            <v-form>

              <!-- username/email -->

              <v-text-field
                v-model="email"
                label="Tên đăng nhập"
                variant="solo-filled"
                full-width
                :rules="[(v) => !!v || 'Tên đăng nhập không được bỏ trống']"
              />

              <!-- password -->

              <v-text-field
                v-model="password"
                label="Mật khẩu"
                type="password"
                variant="solo-filled"
                full-width
                :rules="[(v) => !!v || 'Mật khẩu không được bỏ trống']"
              />

            </v-form>

          </v-card-text>

          <v-card-actions>

            <!-- login button -->

            <v-btn
              color="primary"
              variant="outlined"
              block
              :loading="isLoading"
              @click="handleLogin"
            >
               Đăng nhập
            </v-btn>

          </v-card-actions>

        </v-card>

      </v-col>

    </v-row>

    <SnackBar :snackbar="snackbar" />

  </v-container>

</template>

