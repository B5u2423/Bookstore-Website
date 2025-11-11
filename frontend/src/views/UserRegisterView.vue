<script setup>
import CommonLayout from '@/components/CommonLayout.vue';
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth-store';
import { emailRules, nameRules, passwordRules } from '@/utils/validationRules';

const router = useRouter();
const authStore = useAuthStore();

const prop = {
  name: 'Đăng ký tài khoản',
  items: [
    { title: 'Trang Chủ', disabled: false, href: '/' },
    { title: 'Đăng Ký', disabled: true, href: '/register' }
  ]
}

const form = ref(null)
const isFormValid = ref(false)
const showPassword1 = ref(false)
const showPassword2 = ref(false)
const passwordRetype = ref('')
const showAlert = ref(false)
const alertMessage = ref('')
const alertType = ref('error')

const user = reactive({
  firstName: '',
  lastName: '',
  email: '',
  password: ''
})

function parseFullName(fullName) {
  const nameParts = fullName.trim().split(' ');
  if (nameParts.length === 1) {
    return { firstName: nameParts[0], lastName: '' };
  }
  const firstName = nameParts[0];
  const lastName = nameParts.slice(1).join(' ');
  return { firstName, lastName };
}

const retypePasswordRules = [
  value => {
    if (value) return true
    return 'Vui lòng nhập lại mật khẩu'
  },
  value => {
    if (value === user.password) return true
    return 'Mật khẩu nhập lại không khớp'
  }
]

async function handleRegister() {
  const { valid } = await form.value.validate()
  if (!valid) {
    showAlert.value = true
    alertMessage.value = 'Vui lòng điền đầy đủ thông tin hợp lệ'
    alertType.value = 'error'
    return
  }

  if (passwordRetype.value !== user.password) {
    showAlert.value = true
    alertMessage.value = 'Mật khẩu nhập lại không khớp'
    alertType.value = 'error'
    return
  }

  showAlert.value = false
  authStore.clearError()
  
  let firstName = user.firstName;
  let lastName = user.lastName;
  
  if (!firstName && !lastName && user.fullName) {
    const parsed = parseFullName(user.fullName);
    firstName = parsed.firstName;
    lastName = parsed.lastName;
  }

  const result = await authStore.register({
    firstName: firstName || 'User',
    lastName: lastName || '',
    email: user.email,
    password: user.password,
    userType: 'CUSTOMER'
  })

  if (result.success) {
    showAlert.value = true
    alertMessage.value = 'Đăng ký thành công! Vui lòng đăng nhập.'
    alertType.value = 'success'
    setTimeout(() => {
      router.push('/login')
    }, 2000)
  } else {
    showAlert.value = true
    alertMessage.value = typeof result.error === 'string' ? result.error : 'Đăng ký thất bại. Email có thể đã được sử dụng.'
    alertType.value = 'error'
  }
}

</script>

<template>
  <CommonLayout :prop="prop">
    <v-form class="my-5" ref="form" v-model="isFormValid" @submit.prevent="handleRegister">
      <v-container width="30%">
        <!-- Alert for success/error messages -->
        <v-alert
          v-if="showAlert"
          :type="alertType"
          :text="alertMessage"
          class="mb-4"
          closable
          @click:close="showAlert = false"
        ></v-alert>

        <v-row>
          <v-col cols="6">
            <v-text-field
              v-model="user.firstName"
              :rules="nameRules"
              label="Tên*"
              required
              density="compact"
              :disabled="authStore.isLoading"
            ></v-text-field>
          </v-col>
          <v-col cols="6">
            <v-text-field
              v-model="user.lastName"
              :rules="nameRules"
              label="Họ*"
              required
              density="compact"
              :disabled="authStore.isLoading"
            ></v-text-field>
          </v-col>
        </v-row>

        <v-text-field
          v-model="user.email"
          :rules="emailRules"
          label="Email*"
          required
          density="compact"
          :disabled="authStore.isLoading"
        ></v-text-field>

        <v-text-field
          v-model="user.password"
          :type="showPassword1 ? 'text' : 'password'"
          :append-inner-icon="showPassword1 ? 'mdi-eye' : 'mdi-eye-off'"
          label="Mật khẩu*"
          :rules="passwordRules"
          required
          @click:append-inner="showPassword1 = !showPassword1"
          density="compact"
          :disabled="authStore.isLoading"
        ></v-text-field>

        <v-text-field
          v-model="passwordRetype"
          :type="showPassword2 ? 'text' : 'password'"
          :append-inner-icon="showPassword2 ? 'mdi-eye' : 'mdi-eye-off'"
          label="Nhập lại mật khẩu*"
          :rules="retypePasswordRules"
          required
          @click:append-inner="showPassword2 = !showPassword2"
          density="compact"
          :disabled="authStore.isLoading"
        ></v-text-field>

        <v-btn 
          color="success" 
          class="px-12 mt-3 mx-auto d-block"
          :disabled="!isFormValid || authStore.isLoading"
          :loading="authStore.isLoading"
          @click="handleRegister"
          type="submit"
        >
          {{ authStore.isLoading ? 'Đang đăng ký...' : 'Đăng ký' }}
        </v-btn>

        <!-- Link to login -->
        <div class="mt-3 text-center">
          <span class="text-body-2">Đã có tài khoản? </span>
          <router-link 
            to="/login" 
            class="text-decoration-none"
            :class="authStore.isLoading ? 'text-grey' : 'text-primary'"
          >
            Đăng nhập ngay
          </router-link>
        </div>
      </v-container>
    </v-form>
  </CommonLayout>
</template>