<script setup>
import CommonLayout from '@/components/CommonLayout.vue';
import BreadCrumbs from '@/components/common/BreadCrumbs.vue';

const items = [
    { title: 'Trang Chủ', disabled: false, href: '/' },
    { title: 'Đăng Nhập', disabled: true, href: '/login' }
  ]
</script>

<template>
  <CommonLayout :items="items">
    User Login View
  </CommonLayout>
</template>