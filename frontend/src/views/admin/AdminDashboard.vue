<script setup>
import { AdminService } from '@/api/admin-api';
import { onMounted } from 'vue';


const metrics = ref(null)

async function getMetrics() {
  metrics.value = await AdminService.getDashboardMetrics()
}

onMounted(() => {
  getMetrics
})
</script>

<template>

  <p>{{ metrics }}</p>
  <v-row>

    <v-col cols="3">

      <v-card>

        <v-card-title>Số lượng sách được bày bán</v-card-title>

        <v-card-text></v-card-text>

      </v-card>

    </v-col>

    <v-col cols="3">

      <v-card>

        <v-card-title>Số lượng đơn hàng</v-card-title>

        <v-card-text></v-card-text>

      </v-card>

    </v-col>

    <v-col cols="3">

      <v-card>

        <v-card-title>Số lượng danh mục</v-card-title>

        <v-card-text></v-card-text>

      </v-card>

    </v-col>

  </v-row>

</template>

