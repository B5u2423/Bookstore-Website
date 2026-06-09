<script setup>
import { Doughnut } from 'vue-chartjs'

const props = defineProps({
  data: Object,
  options: Object,
})
</script>

<template>

  <Doughnut
    :data="data"
    :options="options"
  />

</template>

