<script setup>
import { useAuthStore } from '@/stores/auth-store'
import { ref } from 'vue'
import SnackBarOnFailure from '@/components/common/SnackBarOnFailure.vue'
import SnackBarOnSuccess from '@/components/common/SnackBarOnSuccess.vue'

const authStore = useAuthStore()

const visible = ref(false)
const visibleRetype = ref(false)

const formData = ref({
  name: '',
  email: '',
  password: '',
  userType: 'CUSTOMER',
})

// snackbars
const isError = ref(false)
const isSuccess = ref(false)
const message = ref('')

const rules = {
  required: (v) => !!v || 'Không được bỏ trống trường',
  email: (v) => /[\w\d.-]{6,30}@[\w\d.-]+/.test(v) || 'Email không hợp lệ',
}

async function handleRegister() {
  const res = await authStore.register(formData.value)

  if (res.success) {
    isSuccess.value = true
    message.value = 'Đăng ký thành công! Vui lòng truy cập trang đăng nhập!'
  } else {
    ;((isError.value = true), (message.value = 'Đăng ký thất bại!'))
  }
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
          width="500"
          density="compact"
          label="Họ và tên"
          variant="outlined"
          :rules="[rules.required]"
          v-model="formData.name"
        ></v-text-field>

        <v-text-field
          class="my-3"
          density="compact"
          label="Email"
          variant="outlined"
          :rules="[rules.email, rules.required]"
          v-model="formData.email"
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
          v-model="formData.password"
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
        @click="handleRegister"
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

    <SnackBarOnFailure
      :show="isError"
      :message="message"
    />

    <SnackBarOnSuccess
      :show="isSuccess"
      :message="message"
    />

  </v-sheet>

</template>

