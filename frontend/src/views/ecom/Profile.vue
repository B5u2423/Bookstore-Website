<script setup>
import { useUserProfileStore } from '@/stores/user-profile-store'
import { onMounted, ref } from 'vue'

const userInfoStore = useUserProfileStore()

const tab = ref('Thông tin cá nhân')
const items = [
  { tab: 'Thông tin', nameRoute: 'user-info' },
  // remove this later
  // { tab: 'Mã giảm giá', nameRoute: 'vouchers' },
  { tab: 'Lịch sử đơn hàng', nameRoute: 'history' },
]

onMounted(() => {
  userInfoStore.getUserInfo()
})
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

  </v-sheet>

</template>

