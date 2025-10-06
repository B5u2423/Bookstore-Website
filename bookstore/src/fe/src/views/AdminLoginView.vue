<script setup>
</script>

<template>
  <v-form class="my-5" v-model="form">
    <v-container width="30%">
      <v-text-field
        label="Email*"
        required
        density="compact"
      ></v-text-field>

      <v-text-field
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