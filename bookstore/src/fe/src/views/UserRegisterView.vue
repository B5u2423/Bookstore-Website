<script setup>
import CommonLayout from '@/components/CommonLayout.vue';
import { reactive, ref } from 'vue';
import { emailRules, nameRules, phoneRules, passwordRules } from '@/utils/validationRules';

const prop = {
  name: 'Đăng ký tài khoản',
  items: [
    { title: 'Trang Chủ', disabled: false, href: '/' },
    { title: 'Đăng Ký', disabled: true, href: '/register' }
  ]
}

const form = ref(false)
const showPassword1 = ref(false)
const showPassword2 = ref(false)
const passwordRetype = ref('')

// User inputs
const user = reactive({
  name: '',
  email: '',
  phone: '',
  password: ''
})

// Validations retype password
const retypePasswordRules = [
  value => {
    if (value) return true
    return 'Please retype password'
  },
  value => {
    if (value === user.password) return true
    return 'The string MUST match previously typed password'
  }
]
</script>

<template>
  <CommonLayout :prop="prop">
    <v-form class="my-5" v-model="form">
      <v-container width="30%">
        <v-text-field
          v-model="user.name"
          :rules="nameRules"
          :counter="255"
          label="Họ và tên*"
          required
          density="compact"
        ></v-text-field>

        <v-text-field
          v-model="user.email"
          :rules="emailRules"
          label="Email*"
          required
          density="compact"
        ></v-text-field>

        <v-text-field
          v-model="user.phone"
          :rules="phoneRules"
          label="Số điện thoại*"
          required
          density="compact"
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
        ></v-text-field>

        <v-text-field
          v-model="user.passwordRetype"
          :type="showPassword2 ? 'text' : 'password'"
          :append-inner-icon="showPassword2 ? 'mdi-eye' : 'mdi-eye-off'"
          label="Nhập lại mật khẩu*"
          :rules="retypePasswordRules"
          required
          @click:append-inner="showPassword2 = !showPassword2"
          density="compact"
        ></v-text-field>

        <v-btn color="success" 
        class="px-12 mt-3"
        :disabled="!form && (passwordRetype === user.password)"
        >Đăng ký</v-btn>
      </v-container>
    </v-form>
  {{ user.name }}
  </CommonLayout>
</template>