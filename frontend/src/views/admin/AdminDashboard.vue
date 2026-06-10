<script setup>
import { ref, onMounted, computed } from 'vue'
import { AdminService } from '@/api/admin-api'
import DoughnutChart from '@/components/charts/DoughnutChart.vue'

const metrics = ref(true)
const loading = ref(false)

async function getMetrics() {
  try {
    loading.value = true
    metrics.value = await AdminService.getDashboardMetrics()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
})

</script>

<template>

  <v-container fluid>

    <v-progress-linear
      v-if="loading"
      indeterminate
      color="primary"
    />

    <template v-if="metrics">

    </template>


  </v-container>

</template>

