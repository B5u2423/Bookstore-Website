<script setup>
import { Bar } from 'vue-chartjs'

const props = defineProps({
  data: Object,
  options: Object,
})
</script>

<template>

  <Bar
    :data="data"
    :options="options"
  />

</template>

