<script setup>
import { ref } from 'vue'

const visible = ref(false)
const visibleRetype = ref(false)

const rules = {
  required: (v) => !!v || 'Không được bỏ trống trường',
  email: (v) => /[\w\d.-]{6,30}@[\w\d.-]+/.test(v) || 'Email không hợp lệ',
}
</script>

<template>

  <v-sheet
    class="d-flex align-center justify-center mx-auto pa-5 mb-6"
    elevation="4"
    max-width="800"
    width="100%"
    rounded
  >

    <div>

      <h2 class="text-h5 text-black text-center">Đăng ký tài khoản</h2>

      <div class="my-2">

        <v-text-field
        class="my-3"
          density="compact"
          min-width="400"
          label="Họ"
          variant="outlined"
        ></v-text-field>

        <v-text-field
        class="my-3"
          density="compact"
          label="Tên"
          variant="outlined"
        ></v-text-field>

        <v-text-field
        class="my-3"
          density="compact"
          label="Email"
          variant="outlined"
          :rules="[rules.email, rules.required]"
        ></v-text-field>

        <v-text-field
        class="my-3"
          density="compact"
          label="Mật khẩu"
          variant="outlined"
          :rules="[rules.required]"
          :type="visible ? 'text' : 'password'"
          :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
          @click:append-inner="visible = !visible"
        ></v-text-field>

        <v-text-field
          density="compact"
          label="Nhập lại mật khẩu"
          variant="outlined"
          :rules="[rules.required]"
          :type="visibleRetype ? 'text' : 'password'"
          :append-inner-icon="visibleRetype ? 'mdi-eye-off' : 'mdi-eye'"
          @click:append-inner="visibleRetype = !visibleRetype"
        ></v-text-field>

      </div>

      <v-btn
        color="green"
        class="mb-3"
        width="100%"
      >
         ĐĂNG KÝ
      </v-btn>

      <div class="text-center">

      <p class="text-black"> Hoặc đăng nhập bằng</p>

      <v-btn
        color="blue"
        class="ma-2 pa-2"
        prepend-icon="mdi-facebook"
      >
         Facebook
      </v-btn>

      <v-btn
        color="red"
        class="ma-2 pa-2"
        prepend-icon="mdi-google"
      >
         Google
      </v-btn>

      </div>
    </div>

  </v-sheet>

</template>

