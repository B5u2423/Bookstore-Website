<script setup>
const items = [
  {
    voucherTitle: 'African Elephant',
    voucherCode: 'Loxodonta africana',
    voucherType: 'B',
    value: 'Savanna, Forests',
    expiration: 'Savanna, Forests'
  },
  {
    voucherTitle: 'African Elephant',
    voucherCode: 'Loxodonta africana',
    voucherType: 'A',
    value: 'Savanna, Forests',
    expiration: 'Savanna, Forests'
  },
  {
    voucherTitle: 'African Elephant',
    voucherCode: 'Loxodonta africana',
    voucherType: 'C',
    value: 'Savanna, Forests',
    expiration: 'Savanna, Forests'
  },
  {
    voucherTitle: 'African Elephant',
    voucherCode: 'Loxodonta africana',
    voucherType: 'D',
    value: 'Savanna, Forests',
    expiration: 'Savanna, Forests'
  },
]

const headers = [
  {title: 'Tiêu đề', key: 'voucherTitle'},
  {title: 'Mã', key: 'voucherCode'},
  {title: 'Loại', key: 'voucherType'},
  {title: 'Giá trị', key: 'value'},
  {title: 'Thời gian hết hạn', key: 'expiration'}
]
</script>

<template>

  <v-sheet class="pa-3 justify-center align-center">

    <v-data-table
    class="custom-header-color"
    hide-default-footer=""
    :headers="headers"
    :items="items"
    item-key="voucherTitle"
    >
  
  </v-data-table>

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
