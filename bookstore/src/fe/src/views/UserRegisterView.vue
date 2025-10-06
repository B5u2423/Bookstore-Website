<script setup>
import CommonLayout from '@/components/CommonLayout.vue';
const items = [
    { title: 'Trang Chủ', disabled: false, href: '/' },
    { title: 'Đăng Ký', disabled: true, href: '/register' }
  ]
</script>

<template>
  <CommonLayout :items="items">
    User Register View
  </CommonLayout>
</template>