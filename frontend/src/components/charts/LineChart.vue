<script setup>
import { Line } from 'vue-chartjs'

const props = defineProps({
  data: Object,
  options: Object,
})
</script>

<template>
    <Line :data="data" :options="options" />
</template>