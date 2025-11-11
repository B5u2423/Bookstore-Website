<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth-store';
import { emailRules } from '@/utils/validationRules';

const router = useRouter();
const authStore = useAuthStore();

const showPassword = ref(false)
const admin = reactive({
  email: '',
  password: ''
})
const form = ref(null)
const isFormValid = ref(false)
const showAlert = ref(false)
const alertMessage = ref('')
const alertType = ref('error')

const passwordRules = [
  value => {
    if (value) return true
    return 'Mật khẩu là bắt buộc'
  },
  value => {
    if (value.length >= 4) return true
    return 'Mật khẩu phải có ít nhất 4 ký tự'
  }
]

async function handleAdminLogin() {
  const { valid } = await form.value.validate()
  if (!valid) {
    showAlert.value = true
    alertMessage.value = 'Vui lòng điền đầy đủ thông tin hợp lệ'
    alertType.value = 'error'
    return
  }

  showAlert.value = false
  authStore.clearError()
  
  const result = await authStore.adminLogin({
    email: admin.email,
    password: admin.password
  })

  if (result.success) {
    showAlert.value = true
    alertMessage.value = 'Đăng nhập quản trị viên thành công!'
    alertType.value = 'success'
    setTimeout(() => {
      router.push('/admin/dashboard') // Redirect to admin dashboard
    }, 1500)
  } else {
    showAlert.value = true
    alertMessage.value = typeof result.error === 'string' ? result.error : 'Đăng nhập thất bại. Vui lòng kiểm tra lại email và mật khẩu.'
    alertType.value = 'error'
  }
}
</script>

<template>
  <v-container fluid class="fill-height">
    <v-row justify="center" align="center">
      <v-col cols="12" sm="8" md="4">
        <v-card class="elevation-12">
          <v-card-title class="text-center py-6">
            <h2>Đăng nhập Quản trị viên</h2>
          </v-card-title>
          <v-card-text>
            <v-form ref="form" v-model="isFormValid" @submit.prevent="handleAdminLogin">
              <!-- Alert for success/error messages -->
              <v-alert
                v-if="showAlert"
                :type="alertType"
                :text="alertMessage"
                class="mb-4"
                closable
                @click:close="showAlert = false"
              ></v-alert>

              <v-text-field
                v-model="admin.email"
                :rules="emailRules"
                label="Email*"
                required
                density="compact"
                :disabled="authStore.isLoading"
                prepend-inner-icon="mdi-account"
              ></v-text-field>

              <v-text-field
                v-model="admin.password"
                :rules="passwordRules"
                :type="showPassword ? 'text' : 'password'"
                :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                label="Mật khẩu*"
                required
                @click:append-inner="showPassword = !showPassword"
                density="compact"
                :disabled="authStore.isLoading"
                prepend-inner-icon="mdi-lock"
              ></v-text-field>

              <div class="text-center mt-4">
                <v-btn 
                  color="primary" 
                  class="px-12"
                  :disabled="!isFormValid || authStore.isLoading"
                  :loading="authStore.isLoading"
                  @click="handleAdminLogin"
                  type="submit"
                  size="large"
                >
                  {{ authStore.isLoading ? 'Đang đăng nhập...' : 'Đăng nhập' }}
                </v-btn>
              </div>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>