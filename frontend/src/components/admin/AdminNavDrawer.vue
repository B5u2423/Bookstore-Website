<script setup>
const homepage = [{ title: 'Thống kê', icon: 'mdi-chart-bar', nameRoute: 'admin-dashboard' }]
const inventory = [
  { title: 'Thông tin sách', icon: 'mdi-book', nameRoute: 'i-books' },
  { title: 'Danh mục', icon: 'mdi-shape', nameRoute: 'i-categories' },
  { title: 'Bộ sưu tập', icon: 'mdi-view-dashboard', nameRoute: 'i-collections' },
  { title: 'Mã giảm giá', icon: 'mdi-ticket-percent', nameRoute: 'i-coupons' },
]

const accounts = [
  { title: 'Khách hàng', icon: 'mdi-account-multiple', nameRoute: 'man-customers' },
  { title: 'Nhân viên', icon: 'mdi-account-group', nameRoute: 'man-staffs' },
]

const orders = [{ title: 'Đơn trực tuyến', icon: 'mdi-truck', nameRoute: 'i-orders' }]
</script>

<template>

  <v-navigation-drawer
    color="blue"
    class="pa-4"
  >

    <div>

      <h4>Trang chủ</h4>

    </div>

    <v-list>

      <v-list-item
        v-for="(item, i) in homepage"
        :key="i"
        :title="item.title"
        link
        :to="{ name: item.nameRoute }"
        :prepend-icon="item.icon"
        density="compact"
      ></v-list-item>

    </v-list>

    <v-divider></v-divider>

    <div>

      <h4>Quản lý kho</h4>

    </div>

    <v-list>

      <v-list-item
        v-for="(item, i) in inventory"
        :key="i"
        :title="item.title"
        link
        :to="{ name: item.nameRoute }"
        :prepend-icon="item.icon"
        density="compact"
      ></v-list-item>

    </v-list>

    <v-divider></v-divider>

    <div>

      <h4>Quản lý tài khoản</h4>

    </div>

    <v-list>

      <v-list-item
        v-for="(item, i) in accounts"
        :key="i"
        :title="item.title"
        link
        :to="{ name: item.nameRoute }"
        :prepend-icon="item.icon"
        density="compact"
      ></v-list-item>

    </v-list>

    <v-divider></v-divider>

    <div>

      <h4>Quản lý đơn hàng</h4>

    </div>

    <v-list>

      <v-list-item
        v-for="(item, i) in orders"
        :key="i"
        :title="item.title"
        link
        :to="{ name: item.nameRoute }"
        :prepend-icon="item.icon"
        density="compact"
      ></v-list-item>

    </v-list>

  </v-navigation-drawer>

</template>

