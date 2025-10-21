<script setup>
import CommonLayout from '@/components/CommonLayout.vue';
import { ref, reactive } from 'vue';

const prop = {
  name: 'Đăng nhập tài khoản',
  items: [
    { title: 'Trang Chủ', disabled: false, href: '/' },
    { title: 'Đăng Nhập', disabled: true, href: '/login' }
  ]
}

const showPassword = ref(false)
const user = reactive({
  email: '',
  password: ''
})
const form = ref(false)

</script>

<template>
  <CommonLayout :prop="prop">
    <v-form class="my-5" v-model="form">
      <v-container width="30%">
        <v-text-field
          v-model="user.email"
          label="Email*"
          required
          density="compact"
        ></v-text-field>

        <v-text-field
          v-model="user.password"
          :type="showPassword ? 'text' : 'password'"
          :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
          label="Mật khẩu*"
          required
          @click:append-inner="showPassword = !showPassword"
          density="compact"
        ></v-text-field>

        <v-btn color="success" 
        class="px-12 mt-3"
        :disabled="!form"
        >Đăng nhập</v-btn>
      </v-container>
    </v-form>
  </CommonLayout>
</template>