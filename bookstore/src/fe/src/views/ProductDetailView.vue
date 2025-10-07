<script setup>
import CommonLayout from '@/components/CommonLayout.vue';

</script>

<template>
  <CommonLayout>
    <p>Book id param is {{ $route.params.id }}</p>
  </CommonLayout>
</template>