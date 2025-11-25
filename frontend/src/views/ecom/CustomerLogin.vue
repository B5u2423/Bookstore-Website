<script setup>
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth-store'

const authStore = useAuthStore()

const email = ref('')
const password = ref('')
const isValidForm = ref(false)
const visible = ref(false)
const form = ref(null)

const rules = {
  required: (v) => !!v || 'Không được bỏ trống trường',
  email: (v) => /[\w\d.-]{6,30}@[\w\d.-]+/.test(v) || 'Email không hợp lệ',
}

async function handleLogin() {
  // login
  const result = await authStore.login({
    email: email.value,
    password: password.value,
  })

  // handle response
  if (result.success) {
    console.log(result)
    console.log('Logged in')
    // route to home page
    setTimeout(() => {
      router.push('/')
    }, 1500)
  } else {
    console.log(email, password)
    console.log('Failed')
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

      <h2 class="text-center text-h5 text-black">Đăng nhập</h2>

      <div class="my-2">

        <v-form
          ref="form"
          v-model="isValidForm"
          @submit.prevent="handleLogin"
        >

          <div class="text-subtitle-1 text-medium-emphasis">Email</div>

          <v-text-field
            density="compact"
            min-width="400"
            variant="outlined"
            v-model="email"
            :rules="[rules.required, rules.email]"
          ></v-text-field>

          <div class="text-subtitle-1 text-medium-emphasis">Mật khẩu</div>

          <v-text-field
            density="compact"
            variant="outlined"
            v-model="password"
            :rules="[rules.required]"
            :type="visible ? 'text' : 'password'"
            :append-inner-icon="visible ? 'mdi-eye-off' : 'mdi-eye'"
            @click:append-inner="visible = !visible"
          ></v-text-field>

          <v-btn
            color="green"
            class="mb-3 mt-2"
            width="100%"
            type="submit"
            :disabled="!isValidForm"
          >
             ĐĂNG NHẬP
          </v-btn>

        </v-form>

      </div>

      <div class="text-center">

        <p class="text-black"> Hoặc đăng nhập bằng </p>

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

