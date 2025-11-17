<script setup>
import CommonLayout from '@/components/CommonLayout.vue'
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth-store'
import { emailRules } from '@/utils/validationRules'

const router = useRouter()
const authStore = useAuthStore()

const prop = {
  name: 'Đăng nhập tài khoản',
  items: [
    { title: 'Trang Chủ', disabled: false, href: '/' },
    { title: 'Đăng Nhập', disabled: true, href: '/login' },
  ],
}

const showPassword = ref(false)
const user = reactive({
  email: '',
  password: '',
})
const form = ref(null)
const isFormValid = ref(false)
const showAlert = ref(false)
const alertMessage = ref('')
const alertType = ref('error')

const passwordRules = [
  (value) => {
    if (value) return true
    return 'Mật khẩu là bắt buộc'
  },
  (value) => {
    if (value.length >= 4) return true
    return 'Mật khẩu phải có ít nhất 4 ký tự'
  },
]

async function handleLogin() {
  const { valid } = await form.value.validate()
  if (!valid) {
    showAlert.value = true
    alertMessage.value = 'Vui lòng điền đầy đủ thông tin hợp lệ'
    alertType.value = 'error'
    return
  }

  showAlert.value = false
  authStore.clearError()

  const result = await authStore.login({
    email: user.email,
    password: user.password,
  })

  if (result.success) {
    showAlert.value = true
    alertMessage.value = 'Đăng nhập thành công!'
    alertType.value = 'success'
    setTimeout(() => {
      router.push('/')
    }, 1500)
  } else {
    showAlert.value = true
    alertMessage.value =
      typeof result.error === 'string'
        ? result.error
        : 'Đăng nhập thất bại. Vui lòng kiểm tra lại email và mật khẩu.'
    alertType.value = 'error'
  }
}
</script>

<template>
   <CommonLayout :prop="prop"
    > <v-form class="my-5" ref="form" v-model="isFormValid" @submit.prevent="handleLogin"
      > <v-container class="d-flex justify-center" fluid
        > <v-col cols="12" sm="8" md="6" lg="4"
          > <!-- Alert --> <v-alert
            v-if="showAlert"
            :type="alertType"
            :text="alertMessage"
            class="mb-4"
            closable
            @click:close="showAlert = false"
          /> <!-- Email --> <v-text-field
            v-model="user.email"
            :rules="emailRules"
            label="Email*"
            required
            density="compact"
            :disabled="authStore.isLoading"
          /> <!-- Password --> <v-text-field
            v-model="user.password"
            :rules="passwordRules"
            :type="showPassword ? 'text' : 'password'"
            :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
            label="Mật khẩu*"
            required
            density="compact"
            :disabled="authStore.isLoading"
            @click:append-inner="showPassword = !showPassword"
          /> <!-- Login button --> <v-btn
            color="success"
            class="px-12 mt-3 mx-auto d-block"
            :disabled="!isFormValid || authStore.isLoading"
            :loading="authStore.isLoading"
            type="submit"
            > {{ authStore.isLoading ? 'Đang đăng nhập...' : 'Đăng nhập' }} </v-btn
          > <!-- Register link -->
          <div class="mt-3 text-center">
             <span class="text-body-2">Chưa có tài khoản? </span> <router-link
              to="/register"
              class="text-decoration-none"
              :class="authStore.isLoading ? 'text-grey' : 'text-primary'"
              > Đăng ký ngay </router-link
            >
          </div>
           </v-col
        > </v-container
      > </v-form
    > </CommonLayout
  >
</template>
