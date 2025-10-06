<script setup>
import CommonLayout from '@/components/CommonLayout.vue';

const prop = {
  name: 'Đăng nhập tài khoản',
  items: [
    { title: 'Trang Chủ', disabled: false, href: '/' },
    { title: 'Đăng Nhập', disabled: true, href: '/login' }
  ]
}
</script>

<template>
  <CommonLayout :prop="prop">
    User Login View
  </CommonLayout>
</template>