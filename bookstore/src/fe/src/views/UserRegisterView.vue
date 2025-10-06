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
    User Register View
  </CommonLayout>
</template>