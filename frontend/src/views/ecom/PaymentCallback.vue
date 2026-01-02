<script setup>
import { useRoute } from 'vue-router'
import { ref, computed } from 'vue'

const route = useRoute()

const responseCode = ref(route.query.vnp_ResponseCode)
const responseCodeMessage = computed(() => {
  const responseCodes = {
    '07': 'Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).',
    '09': 'Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.',
    10: 'Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần.',
    11: 'Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.',
    12: 'Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.',
    13: 'Giao dịch không thành công do Quý khách nhập sai mật khẩu xác thực giao dịch (OTP). Xin quý khách vui lòng thực hiện lại giao dịch.',
    24: 'Giao dịch không thành công do: Khách hàng hủy giao dịch.',
    51: 'Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.',
    65: 'Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.',
    75: 'Ngân hàng thanh toán đang bảo trì.',
    79: 'Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch.',
    99: 'Các lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê).',
  }

  // Return the corresponding message from the dictionary
  return responseCodes[this.responseCode] || null // Return null if code not found
})
</script>

<template>

  <v-container>

    <v-row justify="center">

      <v-col
        cols="12"
        md="8"
      >

        <template v-if="route.query.vnp_ResponseCode === '00'">

          <v-card>

            <v-card-title>

              <v-img
                class="ma-3"
                src="https://res.cloudinary.com/dmyfjlom1/image/upload/v1767328458/checkmark_l3gt4r.png"
                max-width="80"
              ></v-img>

              <p> Thanh toán thành công! </p>

            </v-card-title>

            <v-card-subtitle> Đơn hàng của bạn đã thanh toán thành công </v-card-subtitle>

            <v-divider></v-divider>

            <v-card-text>

              <v-row>

                <v-col
                  cols="12"
                  md="6"
                >

                  <v-list-item>

                    <v-list-item-content>

                      <v-list-item-title>ID thanh toán:</v-list-item-title>

                      <v-list-item-subtitle>{{ transactionId }}</v-list-item-subtitle>

                    </v-list-item-content>

                  </v-list-item>

                </v-col>

                <v-col
                  cols="12"
                  md="6"
                >

                  <v-list-item>

                    <v-list-item-content>

                      <v-list-item-title>Tổng số tiền:</v-list-item-title>

                      <v-list-item-subtitle>{{ amount }} VND</v-list-item-subtitle>

                    </v-list-item-content>

                  </v-list-item>

                </v-col>

              </v-row>

            </v-card-text>

            <v-divider></v-divider>

            <v-card-actions>

              <v-btn :to="{ name: 'landing' }">Tiếp tục mua sắm</v-btn>

            </v-card-actions>

          </v-card>

        </template>

        <template v-else>

          <v-card>

            <v-card-title>

              <v-img
                class="ma-3"
                src="https://res.cloudinary.com/dmyfjlom1/image/upload/v1767328458/xmark_bic2ml.png"
                max-width="70"
              ></v-img>

              <p>Thanh toán thất bại</p>

            </v-card-title>

            <v-card-subtitle>
               Đơn hàng của bạn đã không thể thể hoàn tất thanh toán
            </v-card-subtitle>

            <v-card-text>

              <template v-if="responseCode"> Lý do: {{ responseCodeMessage }} </template>

              <template v-else> Lý do: Lỗi không xác định </template>

            </v-card-text>

            <v-divider></v-divider>

            <v-card-actions>

              <v-btn
                prepend-icon="mdi-chevron-left"
                variant="flat"
                color="primary"
                :to="{ name: 'landing' }"
              >
                 Tiếp tục mua sắm
              </v-btn>

            </v-card-actions>

          </v-card>

        </template>

      </v-col>

    </v-row>

  </v-container>

</template>

