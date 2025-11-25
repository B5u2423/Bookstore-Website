<script setup>
const services = [
  { name: 'Về chúng tôi' },
  { name: 'Hướng dẫn mua sách' },
  { name: 'FAQ' },
  { name: 'Liên hệ' },
]
const supports = [
  { name: 'Điều khoản sử dụng' },
  { name: 'Chính sách bảo mật' },
  { name: 'Chính sách vận chuyển' },
  { name: 'Chính sách đổi trả' },
  { name: 'Hình thức thanh toán' },
]
const accounts = [
  { name: 'Đăng nhập/Tạo mới tài khoản' },
  { name: 'Thay đổi địa chỉ khách hàng' },
  { name: 'Chi tiết tài khoản' },
  { name: 'Lịch sử mua hàng' },
]
</script>

<template>

  <v-container
    fluid
    class="pa-0"
  >

    <v-row class="bg-red align-center justify-center py-5 px-16">

      <v-card
        title="Đăng ký nhận thông tin"
        color="transparent"
        flat
        subtitle="Thông tin sách mới, sách bán"
      ></v-card>

      <v-text-field
        max-width="666"
        variant="outlined"
        density="compact"
        placeholder="Nhập email của bạn"
        class="mx-3 mb-n6"
      ></v-text-field>

      <v-btn variant="outlined">ĐĂNG KÝ</v-btn>

    </v-row>

    <v-row>

      <v-col sm="4"> bar with something </v-col>

      <v-col>

        <v-row offset="2">

          <v-col>

            <h3>THÔNG TIN CHI TIẾT</h3>

            <v-list density="compact">

              <v-list-item
                class="cursor-pointer"
                v-for="item in services"
                :key="item"
                :title="item.name"
              ></v-list-item>

            </v-list>

            <v-sheet> </v-sheet>

          </v-col>

          <v-col>

            <h3>CHÍNH SÁCH</h3>

            <v-list density="compact">

              <v-list-item
                class="cursor-pointer"
                v-for="item in supports"
                :key="item"
                :title="item.name"
              ></v-list-item>

            </v-list>

          </v-col>

          <v-col>

            <h3>TÀI KHOẢN CỦA TÔI</h3>

            <v-list density="compact">

              <v-list-item
                class="cursor-pointer"
                v-for="item in accounts"
                :key="item"
                :title="item.name"
              ></v-list-item>

            </v-list>

          </v-col>

        </v-row>

      </v-col>

    </v-row>

  </v-container>

</template>

