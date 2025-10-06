<script setup>
import CommonLayout from '@/components/CommonLayout.vue';

const prop = {
  name: 'Đăng ký tài khoản',
  items: [
    { title: 'Trang Chủ', disabled: false, href: '/' },
    { title: 'Đăng Ký', disabled: true, href: '/register' }
  ]
}
</script>

<template>
  <CommonLayout :prop="prop">
    <v-container width="30%">
      <v-card>
        <v-card-text>
          <v-text-field label="Email"></v-text-field>
          <v-text-field label="Password"></v-text-field>
        </v-card-text>

        <v-card-actions class="justify-center border">
          <v-btn primary size="large" class="bg-red">Hello</v-btn>
        </v-card-actions>
      </v-card>
    </v-container>
  </CommonLayout>
</template>