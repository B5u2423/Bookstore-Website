<script setup>
import { AuthService } from '@/api/auth-api'
import { ref } from 'vue'

const email = ref('')
const rules = {
  required: (v) => !!v || 'Không được bỏ trống trường',
  email: (v) => /[\w\d.-]{6,30}@[\w\d.-]+/.test(v) || 'Email không hợp lệ',
}
const form = ref(null)
const isValidForm = ref(false)
const isLoading = ref(false)

async function handleResetPassword() {
  try {
    const res = await AuthService.resetPassword({ email: email.value })
  } catch {
    console.error('Error reset password')
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

      <p class="text-center text-h5 text-black">Khôi phục mật khẩu</p>

      <div class="my-2">

        <v-form
          ref="form"
          v-model="isValidForm"
          @submit.prevent="handleResetPassword"
        >

          <div class="text-subtitle-1 text-medium-emphasis">Email</div>

          <v-text-field
            density="compact"
            min-width="400"
            variant="outlined"
            v-model="email"
            :rules="[rules.required, rules.email]"
          ></v-text-field>

          <v-btn
            color="green"
            class="mb-3 mt-2"
            width="100%"
            type="submit"
            :disabled="!isValidForm"
            :loading="isLoading"
          >
             Gửi thông tin
          </v-btn>

        </v-form>

      </div>

    </div>

  </v-sheet>

</template>

