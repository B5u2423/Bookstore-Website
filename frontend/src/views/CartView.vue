<script setup>
import CommonLayout from '@/components/CommonLayout.vue';
const prop = {
  name: 'Giỏ Hàng',
  items: [
    { title: 'Trang Chủ', disabled: false, href: '/' },
    { title: 'Giỏ Hàng', disabled: true, href: '/cart' }
  ]
}
</script>

<template>
  <CommonLayout :prop="prop">
    <h1>Giỏ hàng</h1>
  </CommonLayout>
</template>