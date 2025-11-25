<script setup>
import { ref } from 'vue'

const username = ref('')
const password = ref('')

function handleLogin() {
  console.log('Username and password :: ', username.value, password.value)
}
</script>

<template>

  <v-container
    class="pa-0 ma-0 fill-height d-flex"
    fluid
  >

    <v-row class="fill-height">

      <v-col
        cols="12"
        md="6"
        class="bg-grey d-flex justify-center"
      >

        <v-img src="/panel.svg"></v-img>

      </v-col>

      <v-col
        cols="12"
        md="6"
        class="d-flex justify-center align-center"
      >

        <v-card
          class="pa-5"
          max-height="600px"
          min-width="400px"
        >

          <v-card-title class="text-center">

            <span class="headline">Đăng nhập</span>

          </v-card-title>

          <v-card-text>

            <v-form>

              <!-- username/email -->

              <v-text-field
                v-model="username"
                label="Tên đăng nhập"
                variant="solo-filled"
                full-width
                :rules="[(v) => !!v || 'Tên đăng nhập không được bỏ trống']"
              />

              <!-- password -->

              <v-text-field
                v-model="password"
                label="Mật khẩu"
                type="password"
                variant="solo-filled"
                full-width
                :rules="[(v) => !!v || 'Mật khẩu không được bỏ trống']"
              />

            </v-form>

          </v-card-text>

          <v-card-actions>

            <!-- login button -->

            <v-btn
              color="primary"
              variant="outlined"
              block
              @click="handleLogin"
            >
               Đăng nhập
            </v-btn>

          </v-card-actions>

        </v-card>

      </v-col>

    </v-row>

  </v-container>

</template>

