<script setup>
import { OrderService } from '@/api/cart-api';
import { onMounted } from 'vue';

const headers = [
  { title: 'Tiêu đề', key: 'voucherTitle' },
  { title: 'Mã', key: 'voucherCode' },
  { title: 'Loại', key: 'voucherType' },
  { title: 'Giá trị', key: 'value' },
  { title: 'Thời gian hết hạn', key: 'expiration' },
]
const items = ref([])

// fetch previous orders
async function fetchOrderHistory() {
  try {
    const res = await OrderService.getAllOrdersByEmail()
    items.value = res
  } catch (error) {
    console.error('Error fetching user order history', error)
  }
}

onMounted(() => {
  fetchOrderHistory()
})
</script>

<template>

  <v-sheet class="pa-3 justify-center align-center">

    <v-data-table
      class="custom-header-color"
      :headers="headers"
      :items="items"
    ></v-data-table>

  </v-sheet>

</template>

<style>
.v-data-table.custom-header-color .v-table__wrapper > table > thead > tr th,
.v-data-table.custom-header-color .v-table__wrapper > table > thead > tr th:hover,
.v-data-table.custom-header-color .v-table__wrapper > table tbody > tr th {
  background-color: rgb(100, 100, 194);
  color: white;
}

.v-data-table.custom-header-color .v-table__wrapper > table > thead > tr th:focus {
  background-color: rgb(100, 100, 194);
  color: white;
  font-weight: bolder;
}
</style>

