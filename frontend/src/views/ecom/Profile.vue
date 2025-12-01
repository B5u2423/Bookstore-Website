<script setup>
import { ref } from 'vue'

const tab = ref('Thông tin cá nhân')
const items = [
  { tab: 'Thông tin', nameRoute: 'user-info' },
  { tab: 'Mã giảm giá', nameRoute: 'vouchers' },
  { tab: 'Lịch sử đơn hàng', nameRoute: 'history' },
]
</script>

<template>

  <v-sheet elevation="3">

    <v-tabs
      v-model="tab"
      grow
    >

      <v-tab
        v-for="(t, i) in items"
        :key="i"
        :text="t.tab"
        :value="t"
        :to="{ name: t.nameRoute }"
      ></v-tab>

    </v-tabs>

    <router-view></router-view>

    <!-- <v-tabs-window v-model="tab">

      <v-tabs-window-item
        v-for="item in items"
        :key="item"
        :value="item"
      >

        <v-card
          flat
        >
        <v-card-text>


        {{ text }}

        </v-card-text>

        </v-card>

      </v-tabs-window-item>

    </v-tabs-window> -->

  </v-sheet>

</template>

