<script setup>
import { ref, reactive } from 'vue';

const showPassword = ref(false)

const admin = reactive({
  username: '',
  password: ''
})
const form = ref(false)
</script>

<template>
  <v-form class="my-5" v-model="form">
    <v-container width="30%">
      <v-text-field
        v-model="admin.username"
        label="Email*"
        required
        density="compact"
      ></v-text-field>

      <v-text-field
        v-model="admin.password"
        :type="showPassword ? 'text' : 'password'"
        :append-inner-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
        label="Mật khẩu*"
        required
        @click:append-inner="showPassword = !showPassword"
        density="compact"
      ></v-text-field>

      <v-btn color="success" 
      class="px-12 mt-3"
      >Đăng nhập</v-btn>
    </v-container>
  </v-form>
</template>